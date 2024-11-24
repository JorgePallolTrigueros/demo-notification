package com.shoppingcart.notification.campaign.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampaignResponseDto extends CampaignRequestDto{
    private boolean enabled;
}
