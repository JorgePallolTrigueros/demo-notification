package com.shoppingcart.notification.campaign.dto;

import com.shoppingcart.notification.dao.entity.ProductEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class CampaignRequestDto {

    protected String name;
    protected String description;
    protected List<Long> users =  new ArrayList<>();
    protected Double discount;
    protected List<Long> products = new ArrayList<>();
    protected List<ProductEntity> productEntityList = new ArrayList<>();
    protected int daysDuration;

}

