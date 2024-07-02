package com.example.domain;

import com.example.controller.RestResponse;
import com.example.domain.common.Result;
import com.example.domain.dto.DataUpdateDto;
import com.example.domain.maybe_entity_or_command.DataUpdate;
import com.example.domain.events.ValidatedDataUpdateRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class SubscriberGateway {

    private final SubscriberRestClient restClient;

    public SubscriberGateway(SubscriberRestClient restClient) {
        this.restClient = restClient;
    }

    public Mono<Result<DataUpdate>> findDataUpdate(ValidatedDataUpdateRequest validRequest) {
        return restClient
                .findDataUpdate(validRequest.dataUpdateId().value())
                .flatMap(this::mapDataUpdateRestResultToDomain);
    }

    private Mono<Result<DataUpdate>> mapDataUpdateRestResultToDomain(Result<RestResponse<DataUpdateDto>> restResponseResult) {
        return Mono.just(restResponseResult)
                .filter(Result::isSuccess)
                .map(result -> unwrapRestResponse(result.getOrThrow()))
                .map(this::mapDataUpdateDtoToDomain);
    }

    private Result<DataUpdate> mapDataUpdateDtoToDomain(Result<DataUpdateDto> dataUpdateDtoResult) {
//        return dataUpdateDtoResult.map(dataUpdateDto -> DataUpdate(
//                dataUpdateDto.dataUpdateId())
//        )
//        );
        return null;
    }

    private <T> Result<T> unwrapRestResponse(RestResponse<T> restResponseResult) {
        if (restResponseResult.isSuccess()) {
            return Result.success(restResponseResult.getData());
        }

        return Result.failure(new RuntimeException(restResponseResult.getError().errorMessage()));
    }
}
