package com.shoppingcart.notification.campaign.service;

import com.shoppingcart.notification.campaign.dto.CampaignRequestDto;
import com.shoppingcart.notification.campaign.dto.CampaignResponseDto;
import com.shoppingcart.notification.dao.entity.CampaignEntity;
import com.shoppingcart.notification.dao.entity.ProductEntity;
import com.shoppingcart.notification.dao.entity.UserEntity;
import com.shoppingcart.notification.dao.repository.CampaignRepository;
import com.shoppingcart.notification.dao.repository.ProductRepository;
import com.shoppingcart.notification.dao.repository.UserRepository;
import com.shoppingcart.notification.mail.MailSenderService;
import com.shoppingcart.notification.util.EmailUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
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
    private final CampaignRepository campaignRepository;

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
                productEntity.setOldPrice(productEntity.getPrice());// el precio viejo es el precio actual sin calculos
                productEntity.setPrice(productEntity.getPrice() - (productEntity.getPrice() * requestDto.getDiscount()) );// el precio actual se calcula mediante la formula de descuento
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

        CampaignEntity campaignEntity = new CampaignEntity(
                response.getName(),
                response.getDescription(),
                BigDecimal.valueOf(response.getDiscount()),
                        response.getDaysDuration(),
                true
                );
        campaignEntity.setUsers(userToSendEmails);
        campaignEntity.setProducts(products);

        log.info("Guardando campaignEntity: {}" ,campaignEntity);

        campaignEntity = campaignRepository.saveAndFlush(campaignEntity);


        log.info("Guardado campaignEntity: {}" ,campaignEntity);

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

    public List<CampaignResponseDto> getAllCampaigns(Boolean oldCampaigns) {
        if(oldCampaigns==null){
            oldCampaigns = true;
        }

        if(oldCampaigns){
            return campaignRepository.findAll().stream().map(
                    campaignEntity -> {
                        CampaignResponseDto response = new CampaignResponseDto();
                        response.setName(campaignEntity.getName());
                        response.setDescription(campaignEntity.getDescription());
                        response.setDiscount(campaignEntity.getDiscount().doubleValue());
                        response.setDaysDuration(campaignEntity.getDaysDuration());
                        response.setUsers(campaignEntity.getUsers().stream().map(UserEntity::getEmail).toList());
                        response.setProducts( campaignEntity.getProducts().stream().map(ProductEntity::getId).toList() );

                        return response;
                    }
            ).toList();
        }

        return campaignRepository.findAllEnabledCampaigns(oldCampaigns).stream().map(
                campaignEntity -> {
                    CampaignResponseDto response = new CampaignResponseDto();
                    response.setName(campaignEntity.getName());
                    response.setDescription(campaignEntity.getDescription());
                    response.setDiscount(campaignEntity.getDiscount().doubleValue());
                    response.setDaysDuration(campaignEntity.getDaysDuration());
                    response.setUsers(campaignEntity.getUsers().stream().map(UserEntity::getEmail).toList());
                    response.setProducts( campaignEntity.getProducts().stream().map(ProductEntity::getId).toList() );

                    return response;
                }
        ).toList();
    }
}
