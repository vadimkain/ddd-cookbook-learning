package com.example.domain.subscriberDataUpdate.events;

import com.example.domain.subscriberDataUpdate.common.Result;
import com.example.domain.subscriberDataUpdate.valueobject.DataUpdateId;

public record ValidatedDataUpdateRequest(DataUpdateId dataUpdateId) {
    public static Result<ValidatedDataUpdateRequest> emerge(UnvalidatedDataUpdateRequest request) {
        return DataUpdateId.emerge(request.dataUpdateId()).map(it -> new ValidatedDataUpdateRequest(it));
    }
}
