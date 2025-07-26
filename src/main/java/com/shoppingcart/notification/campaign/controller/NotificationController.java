package com.shoppingcart.notification.campaign.controller;


import com.shoppingcart.notification.campaign.dto.CampaignRequestDto;
import com.shoppingcart.notification.campaign.dto.CampaignResponseDto;
import com.shoppingcart.notification.campaign.service.CampaignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final CampaignService campaignService;

    @PostMapping("/campaign")
    public ResponseEntity<CampaignResponseDto> createCampaign(@RequestBody CampaignRequestDto requestDto){
        return ResponseEntity.ok(campaignService.createCampaign(requestDto));
    }

    @GetMapping("/campaign")
    public ResponseEntity<List<CampaignResponseDto>> getAllCampaign(@PathVariable(required = false) Boolean oldCampaigns){
        return ResponseEntity.ok(campaignService.getAllCampaigns(oldCampaigns));
    }

    @GetMapping("/campaign/{userId}")
    public ResponseEntity<List<CampaignResponseDto>> getCampaignByUserId(@PathVariable(required = true) String userId){
        return ResponseEntity.ok(campaignService.getCampaignByUserId(userId));
    }


}
