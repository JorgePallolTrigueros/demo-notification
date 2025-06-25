package com.shoppingcart.notification.campaign.dto;

import com.shoppingcart.notification.dao.entity.ProductEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class CampaignRequestDto {
    // Nombre de la campania
    protected String name;
    // Descripcion de la campania
    protected String description;
    // Usuarios seleccionados para la campania, en caso de estar vacia, notificar a todos los usuarios registrados
    protected List<String> users =  new ArrayList<>();
    // Porcentaje de descuento donde 1 equivale al 100%
    protected Double discount;
    // Identificadores de productos afectados, en caso de estar vaciar esta lista afectara a todos los productos
    protected List<Long> products = new ArrayList<>();
    // Dias de duaracion de la campania
    protected int daysDuration;

}

