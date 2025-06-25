package com.shoppingcart.notification.campaign.service;

import com.shoppingcart.notification.campaign.dto.CampaignRequestDto;
import com.shoppingcart.notification.campaign.dto.CampaignResponseDto;
import com.shoppingcart.notification.dao.entity.ProductEntity;
import com.shoppingcart.notification.dao.entity.UserEntity;
import com.shoppingcart.notification.dao.repository.ProductRepository;
import com.shoppingcart.notification.dao.repository.UserRepository;
import com.shoppingcart.notification.mail.MailSenderService;
import com.shoppingcart.notification.util.EmailUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CampaignService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final MailSenderService mailSenderService;

    public CampaignResponseDto createCampaign(CampaignRequestDto requestDto){

        final List<UserEntity> userToSendEmails = getUserAffectedByCampaign(requestDto);

        log.info("Creando campaign: {} para los usuarios: {}",requestDto,userToSendEmails.size());

        List<ProductEntity> products;

        //si hay descuento que aplicar entramos a la condicion
        if(requestDto.getDiscount()!=null && requestDto.getDiscount()>0){
            //miro que productos se veran afectados, si no ha seleccionado ninguno entonces se aplica a todos
            products = getProductAffected(requestDto);

            products.forEach(productEntity -> {
                // precio producto descuento = precio actual - (precio actual * descuento)
                //                = 100 - (100 * 0.05)
                //                = 100  - 5
                //      descuento = 95
                productEntity.setPrice(productEntity.getPrice() - (productEntity.getPrice() * requestDto.getDiscount())  );
            });
            productRepository.saveAll(products);
        } else {
            products = new ArrayList<>();
        }

        userToSendEmails.forEach(
                userEntity -> mailSenderService
                        .sendHtmlMailAsync(
                                userEntity.getEmail(),
                                requestDto.getName(),
                                EmailUtil.buildCampaignEmail(requestDto,products),
                        Optional.empty()
                        )
        );

        final List<String> userIdsAffected = userToSendEmails
                .stream()
                .map(UserEntity::getEmail)
                .toList();

        final List<Long> productIdsAffected = products
                .stream()
                .map(ProductEntity::getId)
                .toList();

        CampaignResponseDto response = CampaignResponseDto
                .builder()
                .enabled(true)
                .build();

        response.setName(requestDto.getName());
        response.setDescription(requestDto.getDescription());
        response.setUsers(userIdsAffected);
        response.setProducts(productIdsAffected);
        response.setDiscount(requestDto.getDiscount());
        response.setDaysDuration(requestDto.getDaysDuration());

        return response;
    }

    private List<ProductEntity> getProductAffected(CampaignRequestDto requestDto) {
        final List<ProductEntity> products;
        if(CollectionUtils.isEmpty(requestDto.getProducts())){
            products = productRepository.findAll();
        }else{
            //else solo a ciertos productos
            products = productRepository.findAllById(requestDto.getProducts());
        }
        return products;
    }

    private List<UserEntity> getUserAffectedByCampaign(CampaignRequestDto requestDto) {
        List<UserEntity> userToSendEmails;


        final List<String> usersIds = requestDto.getUsers();
        if(CollectionUtils.isEmpty(usersIds)){
            //afectara a todos los usuarios
            userToSendEmails = userRepository.findAll();
        }else{
            //afectara SOLO a ciertos usuarios
            userToSendEmails = userRepository.findAllById(usersIds);
        }
        return userToSendEmails;
    }

}
