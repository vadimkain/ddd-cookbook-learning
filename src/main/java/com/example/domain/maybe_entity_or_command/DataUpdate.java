package com.example.domain.maybe_entity_or_command;

import com.example.domain.valueobject.DataUpdateId;
import com.example.domain.valueobject.MobileRegionId;
import com.example.domain.valueobject.Msisdn;
import com.example.domain.valueobject.SubscriberId;

public record DataUpdate (
        DataUpdateId dataUpdateId,
        SubscriberId subscriberId,
        Msisdn msisdn,
        MobileRegionId mobileRegionId
) {
}
