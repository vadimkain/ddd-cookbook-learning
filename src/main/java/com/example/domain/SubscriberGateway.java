package com.example.domain;

import com.example.controller.RestResponse;
import com.example.domain.subscriberDataUpdate.common.Result;
import com.example.domain.subscriberDataUpdate.dto.DataUpdateDto;
import com.example.domain.subscriberDataUpdate.dto.SubscriberDto;
import com.example.domain.subscriberDataUpdate.idontknow.DataUpdate;
import com.example.domain.subscriberDataUpdate.events.ValidatedDataUpdateRequest;
import com.example.domain.subscriberDataUpdate.idontknow.Subscriber;
import com.example.domain.subscriberDataUpdate.valueobject.SubscriberId;
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

    public Mono<Result<Subscriber>> findSubscriber(SubscriberId subscriberId) {
        return restClient
                .findSubscriber(subscriberId.value())
                .flatMap(this::mapSubscriberRestResultToDomain);
    }

    private Mono<Result<DataUpdate>> mapDataUpdateRestResultToDomain(Result<RestResponse<DataUpdateDto>> restResponseResult) {
        return Mono.just(restResponseResult)
                .filter(Result::isSuccess)
                .map(result -> {
                    try {
//                        TODO: добавить метод где просто берём значение без пробрасывания
                        return unwrapRestResponse(result.getOrThrow());
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(this::mapDataUpdateDtoToDomain)
                .switchIfEmpty(forwardErrorDataUpdate(restResponseResult));
    }

    private Mono<Result<DataUpdate>> forwardErrorDataUpdate(Result<RestResponse<DataUpdateDto>> restResponseResult) {
        return Mono.just(
                Result.failure(
                        restResponseResult.exceptionOrNull() != null
                                ? restResponseResult.exceptionOrNull()
                                : new RuntimeException("Dumb error failInSubscriberDataUpdate")
                )
        );
    }

    private Result<DataUpdate> mapDataUpdateDtoToDomain(Result<DataUpdateDto> dataUpdateDtoResult) {
        return dataUpdateDtoResult.flatMap(dataUpdateDto -> {
//                        TODO: добавить метод где просто берём значение без пробрасывания
            try {
                return DataUpdate.emerge(dataUpdateDto);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        });
    }

    private Mono<Result<Subscriber>> mapSubscriberRestResultToDomain(Result<RestResponse<SubscriberDto>> restResponseResult) {
        return Mono.just(restResponseResult)
                .filter(Result::isSuccess)
                .map(result -> {
                    try {
                        return unwrapRestResponse(result.getOrThrow());
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(this::mapSubscriberDtoToDomain)
                .switchIfEmpty(forwardErrorSubscriber(restResponseResult));
    }

    private Mono<Result<Subscriber>> forwardErrorSubscriber(Result<RestResponse<SubscriberDto>> restResponseResult) {
        return Mono.just(
                Result.failure(
                        restResponseResult.exceptionOrNull() != null
                                ? restResponseResult.exceptionOrNull()
                                : new RuntimeException("Dumb error failInSubscriberDataUpdate")
                )
        );
    }

    private Result<Subscriber> mapSubscriberDtoToDomain(Result<SubscriberDto> subscriberDtoResult) {
        return subscriberDtoResult.flatMap(restClient -> {
            try {
                return Subscriber.emerge(restClient);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        });
    }

    private <T> Result<T> unwrapRestResponse(RestResponse<T> restResponseResult) {
        if (restResponseResult.isSuccess()) {
            return Result.success(restResponseResult.getData());
        }

        return Result.failure(new RuntimeException(restResponseResult.getError().errorMessage()));
    }
}
