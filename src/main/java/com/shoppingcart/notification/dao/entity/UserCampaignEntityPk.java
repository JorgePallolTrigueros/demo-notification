package com.shoppingcart.notification.dao.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class UserCampaignEntityPk {

    @Basic(optional = false)
    @Column(name = "user_id")
    private String userId;

    @Basic(optional = false)
    @Column(name = "campaign_id")
    private Long campaignId;


    public UserCampaignEntityPk() {
    }

    public UserCampaignEntityPk(String userId, Long campaignId) {
        this.userId = userId;
        this.campaignId = campaignId;
    }

}
