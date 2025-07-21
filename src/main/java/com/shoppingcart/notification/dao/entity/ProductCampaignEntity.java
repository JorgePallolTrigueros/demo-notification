package com.shoppingcart.notification.dao.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "product_campaign")
public class ProductCampaignEntity {

    @EmbeddedId
    private ProductCampaignEntityPk pk;

    public ProductCampaignEntity() {
    }

    public ProductCampaignEntity(Long productId,Long campaignId) {
        this.pk = new ProductCampaignEntityPk(productId,campaignId);
    }

    public Long getProductId(){
        return this.pk !=null ? this.pk.getProductId():null;
    }

    public Long campaignId(){
        return this.pk != null ? this.pk.getCampaignId():null;
    }

}
