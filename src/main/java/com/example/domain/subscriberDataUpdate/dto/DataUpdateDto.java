package com.example.domain.subscriberDataUpdate.dto;

public record DataUpdateDto(
        String dataUpdateId,
        String subscriberId,
        String msisdn,
        String mobileRegionId
) {
}
