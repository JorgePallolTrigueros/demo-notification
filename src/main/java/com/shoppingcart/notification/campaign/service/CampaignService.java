package com.shoppingcart.notification.campaign.service;

import com.shoppingcart.notification.campaign.dto.CampaignRequestDto;
import com.shoppingcart.notification.campaign.dto.CampaignResponseDto;
import com.shoppingcart.notification.dao.entity.ProductEntity;
import com.shoppingcart.notification.dao.repository.ProductRepository;
import com.shoppingcart.notification.dao.repository.UserRepository;
import com.shoppingcart.notification.mail.MailSenderService;
import com.shoppingcart.notification.util.EmailUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
        final long users = userRepository.count();
        log.info("Creando campaign: {} para todos los usuarios: {}",requestDto,users);

        if(requestDto.getUsers()==null ||  requestDto.getUsers().isEmpty())
        userRepository.findAll().forEach(
                userEntity -> {
                    mailSenderService.sendHtmlMailAsync(userEntity.getEmail(),
                            requestDto.getName(),
                            EmailUtil.buildCampaignEmail(requestDto),
                            Optional.empty());
                }
        );


        //si hay descuento que aplicar entramos a la condicion
        if(requestDto.getDiscount()!=null && requestDto.getDiscount()>0){
            //miro que productos se veran afectados, si no ha seleccionado ninguno entonces se aplica a todos
            if(requestDto.getProducts()==null || requestDto.getProducts().isEmpty()){
                final List<ProductEntity> products = productRepository.findAll();

                products.forEach(productEntity -> {
                    // precio producto descuento = precio actual - (precio actual * descuento)
                    //                = 100 - (100 * 0.05)
                    //                = 100  - 5
                    //      descuento = 95
                    productEntity.setPrice(productEntity.getPrice() - (productEntity.getPrice() * requestDto.getDiscount())  );
                });
                productRepository.saveAll(products);
            }else{
                //else solo a ciertos productos
                final List<ProductEntity> products = productRepository.findAllById(requestDto.getProducts());
                products.forEach(productEntity -> {
                    productEntity.setPrice(productEntity.getPrice() - (productEntity.getPrice() * requestDto.getDiscount())  );
                });
            }
        }

        return CampaignResponseDto
                .builder()
                .enabled(true)
                .build();
    }

}
