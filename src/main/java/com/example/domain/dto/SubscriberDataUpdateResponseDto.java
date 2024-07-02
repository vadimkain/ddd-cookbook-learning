package com.example.domain.dto;

import com.example.domain.maybe_entity_or_command.SubscriberDataUpdateResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class SubscriberDataUpdateResponseDto {
    private final String subscriberId;
    private final String dataUpdateId;

    public SubscriberDataUpdateResponseDto(String subscriberId, String dataUpdateId) {
        this.subscriberId = subscriberId;
        this.dataUpdateId = dataUpdateId;
    }

    @JsonIgnore
    public static SubscriberDataUpdateResponseDto from(SubscriberDataUpdateResponse dataUpdateResult) {
        return new SubscriberDataUpdateResponseDto(
                dataUpdateResult.subscriberId(),
                dataUpdateResult.dataUpdateId()
        );
    }
}
