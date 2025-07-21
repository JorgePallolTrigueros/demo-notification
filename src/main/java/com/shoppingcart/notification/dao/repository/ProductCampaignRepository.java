package com.shoppingcart.notification.dao.repository;

import com.shoppingcart.notification.dao.entity.ProductCampaignEntity;
import com.shoppingcart.notification.dao.entity.ProductCampaignEntityPk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCampaignRepository extends JpaRepository<ProductCampaignEntity, ProductCampaignEntityPk> {

}
