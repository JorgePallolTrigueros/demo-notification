package com.shoppingcart.notification.dao.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name = "user_campaign")
public class UserCampaignEntity implements Serializable {


    @EmbeddedId
    private UserCampaignEntityPk pk;

    public UserCampaignEntity() {
    }

    public UserCampaignEntity(String userId, Long campaignId) {
        this.pk = new UserCampaignEntityPk(userId,campaignId);
    }

    public String userId(){
        return this.pk != null ? this.pk.getUserId():null;
    }

    public Long campaignId(){
        return this.pk != null ? this.pk.getCampaignId():null;
    }
}