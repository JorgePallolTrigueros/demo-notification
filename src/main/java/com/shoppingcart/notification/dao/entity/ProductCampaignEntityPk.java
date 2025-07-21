package com.shoppingcart.notification.dao.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class ProductCampaignEntityPk {
    @Basic(optional = false)
    @Column(name = "product_id")
    private Long productId;

    @Basic(optional = false)
    @Column(name = "campaign_id")
    private Long campaignId;

    public ProductCampaignEntityPk() {
    }

    public ProductCampaignEntityPk(Long productId, Long campaignId) {
        this.productId = productId;
        this.campaignId = campaignId;
    }
}
