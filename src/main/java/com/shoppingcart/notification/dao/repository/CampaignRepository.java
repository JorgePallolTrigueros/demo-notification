package com.shoppingcart.notification.dao.repository;

import com.shoppingcart.notification.dao.entity.CampaignEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignRepository extends JpaRepository<CampaignEntity,Long > {

    @Query("SELECT c FROM CampaignEntity c WHERE c.enabled = :enabled")
    List<CampaignEntity> findAllEnabledCampaigns(final boolean enabled);

}
