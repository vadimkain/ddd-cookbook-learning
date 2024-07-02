package com.example.domain.dto;

public record DataUpdateDto(
        String dataUpdateId,
        String subscriberId,
        String msisdn,
        String mobileRegionId
) {
}
