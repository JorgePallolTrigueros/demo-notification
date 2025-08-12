package com.shoppingcart.notification.campaign.service;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CampaignService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final MailSenderService mailSenderService;
    private final CampaignRepository campaignRepository;
    private final ObjectMapper objectMapper;

    public CampaignResponseDto createCampaign(CampaignRequestDto requestDto) {

        final List<UserEntity> userToSendEmails = getUserAffectedByCampaign(requestDto);

        log.info("Creando campaign: {} para los usuarios: {}", requestDto, userToSendEmails.size());

        List<ProductEntity> products;

        //si hay descuento que aplicar entramos a la condicion
        if (requestDto.getDiscount() != null && requestDto.getDiscount() > 0) {
            //miro que productos se veran afectados, si no ha seleccionado ninguno entonces se aplica a todos
            products = getProductAffected(requestDto);

            products.forEach(productEntity -> {
                // precio producto descuento = precio actual - (precio actual * descuento)
                //                = 100 - (100 * 0.05)
                //                = 100  - 5
                //      descuento = 95
                //productEntity.setOldPrice(productEntity.getPrice());// el precio viejo es el precio actual sin calculos
                //productEntity.setPrice(productEntity.getPrice() - (productEntity.getPrice() * requestDto.getDiscount()) );// el precio actual se calcula mediante la formula de descuento
            });
            productRepository.saveAll(products);
        } else {
            products = new ArrayList<>();
        }


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

        log.info("Guardando campaignEntity: {}", campaignEntity);

        campaignEntity = campaignRepository.saveAndFlush(campaignEntity);


        log.info("Guardado campaignEntity: {}", campaignEntity);


        /**
         * userId=jorgepallol1@gmail.com
         *   - campaignId=0
         *     description="campania promocional especial para jorge de bienvenida"
         *     discount=0.20
         *     productIds=[28,29,30,31,32,33,....]
         *     active=true
         *     duration=7
         *   - campaignId=1
         *     description="campania promocional especial para jorge por cliente frecuente"
         *     discount=0.10
         *     productIds=[28,29,30]
         *     active=true
         *     duration=7
         *
         *
         *     --> [28,29,30] = 0.20               -> descuento base si tenia campania
         *     --> [28,29,30] = 0.0                -> descuento base zero por que no tiene campania
         *
         *
         *     --> [28,29,30] = 0.00 + 0.10        -> descuento nuevo sumando los descuentos anteriores para ese producto
         *
         * userId=jonathan@gmail.com
         *   - campaignId=2
         *     description="campania promocional especial para jonathan"
         *     discount=0.05
         *     productIds=[31,32,33]
         *     active=true
         *     duration=7
         */

        CampaignEntity finalCampaignEntity = campaignEntity;
        userToSendEmails.forEach(
                userEntity -> {
                    // 1) Campañas activas previas del usuario (antes de crear la nueva)
                    final List<CampaignEntity> activeUserCampaigns = campaignRepository
                            .findByUserId(userEntity.getEmail())
                            .stream()
                            .filter(CampaignEntity::isEnabled)
                            .filter(campaignEntitySaved -> !campaignEntitySaved.getId().equals(finalCampaignEntity.getId()))
                            .toList();
                    final List<ProductEntity> personalizedProducts = products.stream().map(product -> {
                        BigDecimal basePrice = BigDecimal.valueOf(product.getPrice());
                        BigDecimal previousDiscount = sumDiscountsForProduct(activeUserCampaigns, product.getId());
                        BigDecimal newDiscount = BigDecimal.valueOf(requestDto.getDiscount() != null ? requestDto.getDiscount() : 0.0);

                        boolean newCampaignApplies = appliesToProduct(requestDto, product.getId());
                        BigDecimal totalDiscount = previousDiscount.add(newCampaignApplies ? newDiscount : BigDecimal.ZERO);
                        totalDiscount = capDiscount(totalDiscount, new BigDecimal("0.90"));

                        BigDecimal finalPrice = calcDiscountedPrice(basePrice, totalDiscount);

                        // Clone "desacoplado" para el email
                        ProductEntity copy = objectMapper.convertValue(product, ProductEntity.class);
                        copy.setOldPrice(basePrice.doubleValue());
                        copy.setPrice(finalPrice.doubleValue());

                        return copy;
                    }).toList();

                    // 3) Notificar al usuario con el detalle (ajusta el método a tu MailSenderService)
                    try {
                        mailSenderService
                                .sendHtmlMailAsync(
                                        userEntity.getEmail(),
                                        requestDto.getName(),
                                        EmailUtil.buildCampaignEmail(requestDto, personalizedProducts),
                                        Optional.empty()
                                );
                    } catch (Exception e) {
                        log.warn("No se pudo enviar email a {}: {}", userEntity.getEmail(), e.getMessage());
                    }
                }


        );

        return response;
    }

    private List<ProductEntity> getProductAffected(CampaignRequestDto requestDto) {
        final List<ProductEntity> products;
        if (CollectionUtils.isEmpty(requestDto.getProducts())) {
            products = productRepository.findAll();
        } else {
            //else solo a ciertos productos
            products = productRepository.findAllById(requestDto.getProducts());
        }
        return products;
    }

    private List<UserEntity> getUserAffectedByCampaign(CampaignRequestDto requestDto) {
        List<UserEntity> userToSendEmails;


        final List<String> usersIds = requestDto.getUsers();
        if (CollectionUtils.isEmpty(usersIds)) {
            //afectara a todos los usuarios
            userToSendEmails = userRepository.findAll();
        } else {
            //afectara SOLO a ciertos usuarios
            userToSendEmails = userRepository.findAllById(usersIds);
        }
        return userToSendEmails;
    }

    public List<CampaignResponseDto> getAllCampaigns(Boolean oldCampaigns) {
        if (oldCampaigns == null) {
            oldCampaigns = true;
        }

        if (oldCampaigns) {
            return campaignRepository.findAll().stream().map(
                    CampaignService::mapCampaignEntity2Dto
            ).toList();
        }

        return campaignRepository.findAllEnabledCampaigns(oldCampaigns).stream().map(
                CampaignService::mapCampaignEntity2Dto
        ).toList();
    }

    private static CampaignResponseDto mapCampaignEntity2Dto(CampaignEntity campaignEntity) {
        CampaignResponseDto response = new CampaignResponseDto();
        response.setName(campaignEntity.getName());
        response.setDescription(campaignEntity.getDescription());
        response.setDiscount(campaignEntity.getDiscount().doubleValue());
        response.setDaysDuration(campaignEntity.getDaysDuration());
        response.setUsers(campaignEntity.getUsers().stream().map(UserEntity::getEmail).toList());
        response.setProducts(campaignEntity.getProducts().stream().map(ProductEntity::getId).toList());
        return response;
    }

    public List<CampaignResponseDto> getCampaignByUserId(String userId) {
        return campaignRepository
                .findByUserId(userId)
                .stream()
                .map(CampaignService::mapCampaignEntity2Dto)// operador :: , es el operador de resolucion de ambitos que me permite acceder a los metodos de la clase referenciada y pasar los argumentos
                .toList();
    }

    // ===================== Helpers =====================

    /**
     * Suma los descuentos de las campañas activas del usuario que aplican a un producto.
     * La suma es lineal (0.05 + 0.10 = 0.15).
     */
    private BigDecimal sumDiscountsForProduct(List<CampaignEntity> activeCampaigns, Long productId) {
        return activeCampaigns.stream()
                .filter(c -> campaignAppliesToProduct(c, productId))
                .map(CampaignEntity::getDiscount) // BigDecimal
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Comprueba si una campaña aplica a un producto.
     * Si la campaña no tiene lista de productos (o está vacía), asumimos que aplica a TODOS.
     */
    private boolean campaignAppliesToProduct(CampaignEntity c, Long productId) {
        List<ProductEntity> prods = c.getProducts();
        if (prods == null || prods.isEmpty()) return true;
        return prods.stream().anyMatch(p -> Objects.equals(p.getId(), productId));
    }

    /**
     * Comprueba si el request de la nueva campaña aplica al producto.
     * Si no se enviaron productIds en el request, aplica a todos.
     */
    private boolean appliesToProduct(CampaignRequestDto requestDto, Long productId) {
        List<Long> ids = requestDto.getProducts();
        return (ids == null || ids.isEmpty()) || ids.contains(productId);
    }

    /**
     * Calcula precio final: base * (1 - totalDiscount), con redondeo a 2 decimales.
     */
    private BigDecimal calcDiscountedPrice(BigDecimal basePrice, BigDecimal totalDiscount) {
        BigDecimal factor = BigDecimal.ONE.subtract(totalDiscount);
        if (factor.compareTo(BigDecimal.ZERO) < 0) factor = BigDecimal.ZERO;
        return basePrice.multiply(factor).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Limita el descuento máximo permitido (por defecto 90%).
     */
    private BigDecimal capDiscount(BigDecimal discount, BigDecimal max) {
        return discount.compareTo(max) > 0 ? max : discount;
    }


    @Getter
    @Setter
    @AllArgsConstructor
    public static class UserProductPriceView {
        private Long productId;
        private String productName;
        private BigDecimal basePrice;
        private BigDecimal previousDiscount; // suma de campañas activas previas
        private BigDecimal newDiscount;      // descuento solo de la campaña que estamos creando (si aplica)
        private BigDecimal totalDiscount;    // previous + new (capeado)
        private BigDecimal finalPrice;       // basePrice * (1 - totalDiscount)
    }


}
