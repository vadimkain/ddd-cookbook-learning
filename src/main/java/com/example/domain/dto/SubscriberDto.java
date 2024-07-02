package com.example.domain.dto;

public record SubscriberDto (
        String subscriberId,
        String msisdn,
        String mobileRegionId
) {
}
