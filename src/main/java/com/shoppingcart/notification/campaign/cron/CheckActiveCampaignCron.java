package com.shoppingcart.notification.campaign.cron;

import com.shoppingcart.notification.dao.entity.CampaignEntity;
import com.shoppingcart.notification.dao.repository.CampaignRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CheckActiveCampaignCron {
    private final CampaignRepository campaignRepository;

    public CheckActiveCampaignCron(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    // Ejecutar cada minuto
    @Scheduled(cron = "0 * * * * *") // Cada minuto exacto
    public void checkCampaigns() {
        List<CampaignEntity> activeCampaigns = campaignRepository.findAllEnabledCampaigns(true);
        Date now = new Date();

        List<CampaignEntity> endedCampaigns = activeCampaigns.stream()
                .filter(c -> c.getEndedAt() != null && !c.getEndedAt().after(now))
                .peek(c -> c.setEnabled(false))
                .collect(Collectors.toList());

        if (!endedCampaigns.isEmpty()) {
            campaignRepository.saveAll(endedCampaigns);
            System.out.println("✔ Campaign disables: " + endedCampaigns.size());
        }
    }

}
