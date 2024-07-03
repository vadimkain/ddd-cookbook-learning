package com.example.domain.idontknow;

import com.example.domain.common.Result;
import com.example.domain.dto.DataUpdateDto;
import com.example.domain.valueobject.DataUpdateId;
import com.example.domain.valueobject.MobileRegionId;
import com.example.domain.valueobject.Msisdn;
import com.example.domain.valueobject.SubscriberId;
import reactor.util.function.Tuple4;

public record DataUpdate(
        DataUpdateId dataUpdateId,
        SubscriberId subscriberId,
        Msisdn msisdn,
        MobileRegionId mobileRegionId
) {
    public static Result<DataUpdate> emerge(DataUpdateDto dataUpdateDto) throws Throwable {
        return wrapPrimitivesToValues(dataUpdateDto)
                .map(DataUpdate::tuple4ToDataUpdate);
    }

    private static DataUpdate tuple4ToDataUpdate(Tuple4<DataUpdateId, SubscriberId, Msisdn, MobileRegionId> tuple4) {
        return new DataUpdate(
                tuple4.getT1(),
                tuple4.getT2(),
                tuple4.getT3(),
                tuple4.getT4()
        );
    }

    private static Result<Tuple4<DataUpdateId, SubscriberId, Msisdn, MobileRegionId>> wrapPrimitivesToValues(DataUpdateDto it) throws Throwable {
        return Result.zip(
                DataUpdateId.emerge(it.dataUpdateId()),
                SubscriberId.emerge(it.subscriberId()),
                Msisdn.emerge(it.msisdn()),
                MobileRegionId.emerge(it.mobileRegionId())
        );
    }


}
