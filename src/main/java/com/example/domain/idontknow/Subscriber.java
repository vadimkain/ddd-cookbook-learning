package com.example.domain.idontknow;

import com.example.domain.common.Result;
import com.example.domain.dto.SubscriberDto;
import com.example.domain.valueobject.MobileRegionId;
import com.example.domain.valueobject.Msisdn;
import com.example.domain.valueobject.SubscriberId;

public record Subscriber(
        SubscriberId subscriberId,
        Msisdn msisdn,
        MobileRegionId mobileRegionId
) {
    public static Result<Subscriber> emerge(SubscriberDto subscriberDto) throws Throwable {
        return Result.zip(
                SubscriberId.emerge(subscriberDto.subscriberId()),
                Msisdn.emerge(subscriberDto.msisdn()),
                MobileRegionId.emerge(subscriberDto.mobileRegionId())
        ).map(result -> new Subscriber(
                        result.getT1(),
                        result.getT2(),
                        result.getT3()
                )
        );
    }
}