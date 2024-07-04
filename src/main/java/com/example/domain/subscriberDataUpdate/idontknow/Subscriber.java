package com.example.domain.subscriberDataUpdate.idontknow;

import com.example.domain.subscriberDataUpdate.common.Result;
import com.example.domain.subscriberDataUpdate.dto.SubscriberDto;
import com.example.domain.subscriberDataUpdate.valueobject.MobileRegionId;
import com.example.domain.subscriberDataUpdate.valueobject.Msisdn;
import com.example.domain.subscriberDataUpdate.valueobject.SubscriberId;

public record Subscriber(
        SubscriberId subscriberId,
        Msisdn msisdn,
        MobileRegionId mobileRegionId
) {
    public static Result<Subscriber> emerge(SubscriberDto subscriberDto) throws Throwable {
//        если нырнуть, то проверяем чтобы ни единый аргумент не был isFailure, а в ValueObjects проходит валидация
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