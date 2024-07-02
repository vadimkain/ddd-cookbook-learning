package com.example.domain.events;

import com.example.domain.common.Result;
import com.example.domain.valueobject.DataUpdateId;

public record ValidatedDataUpdateRequest(DataUpdateId dataUpdateId) {
    public static Result<ValidatedDataUpdateRequest> emerge(UnvalidatedDataUpdateRequest request) {
        return DataUpdateId.emerge(request.dataUpdateId()).map(it -> new ValidatedDataUpdateRequest(it));
    }
}
