package com.shoppingcart.notification.dao.repository;

import com.shoppingcart.notification.dao.entity.UserCampaignEntity;
import com.shoppingcart.notification.dao.entity.UserCampaignEntityPk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCampaignRepository extends JpaRepository<UserCampaignEntity, UserCampaignEntityPk> {

}