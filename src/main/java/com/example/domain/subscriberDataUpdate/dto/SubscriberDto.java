package com.example.domain.subscriberDataUpdate.dto;

public record SubscriberDto (
        String subscriberId,
        String msisdn,
        String mobileRegionId
) {
}
