package com.example.domain.service;

import com.example.domain.*;
import com.example.domain.common.Result;
import com.example.domain.idontknow.DataUpdate;
import com.example.domain.idontknow.SubscriberDataUpdate;
import com.example.domain.idontknow.SubscriberDataUpdateResponse;
import com.example.domain.events.UnvalidatedDataUpdateRequest;
import com.example.domain.events.ValidatedDataUpdateRequest;
import com.example.domain.idontknow.SubscriberUpdateRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class SubscriberDataUpdateService {
    private final SubscriberGateway subscribersClient;

//    private final Function<ValidatedDataUpdateRequest, Mono<Result<DataUpdate>>> requestSubscriberUpdate = validRequest -> subscribersClient.findDataUpdate(validRequest);

    public SubscriberDataUpdateService(SubscriberGateway subscribersClient) {
        this.subscribersClient = subscribersClient;
    }


    public Mono<Result<SubscriberDataUpdateResponse>> subscriberUpdate(UnvalidatedDataUpdateRequest unvalidatedDataUpdateRequest) {
        return validateRequest(unvalidatedDataUpdateRequest)
//                .flatMap(result -> requestSubscriberUpdate(result.getValue()))
//                .flatMap(result -> requestCurrentSubscriber(result.getValue()))
//                .flatMap(result -> Mono.just(result.getValue().prepareUpdateRequest()))
//                .flatMap(result -> updateSubscriber(result.getValue()));
                .flatMap(validatedRequestResult -> validatedRequestResult.fold(
                        this::requestSubscriberUpdate,
                        exception -> Mono.just(Result.failure(exception))
                ))
                .flatMap(dataUpdateResult -> dataUpdateResult.fold(
                        value -> requestCurrentSubscriber((DataUpdate) value),
                        exception -> Mono.just(Result.failure(exception))
                ))
                .flatMap(dataUpdateResult -> dataUpdateResult.fold(
                        value -> Mono.just(((SubscriberDataUpdate) value).prepareUpdateRequest()),
                        exception -> Mono.just(Result.failure(exception))
                ))
                .flatMap(subscriberUpdateRequestResult -> subscriberUpdateRequestResult.fold(
                        value -> updateSubscriber(((SubscriberUpdateRequest) value)),
                        exception -> Mono.just(Result.failure(exception))
                ));
    }

    private Mono<Result<ValidatedDataUpdateRequest>> validateRequest(UnvalidatedDataUpdateRequest unvalidatedDataUpdateRequest) {
        return Mono.just(ValidatedDataUpdateRequest.emerge(unvalidatedDataUpdateRequest));
    }

    private Mono<Result<DataUpdate>> requestSubscriberUpdate(ValidatedDataUpdateRequest validRequest) {
        return subscribersClient.findDataUpdate(validRequest);
    }

    private Mono<Result<SubscriberDataUpdate>> requestCurrentSubscriber(DataUpdate dataUpdate) {
        return subscribersClient
                .findSubscriber(dataUpdate.subscriberId())
                .map(subscriberResult -> SubscriberDataUpdate.emerge(dataUpdate, subscriberResult));
    }

    private Mono<Result<SubscriberDataUpdateResponse>> updateSubscriber(SubscriberUpdateRequest subscriberUpdateRequest) {
        return Mono.just(Result.success(new SubscriberDataUpdateResponse(
                subscriberUpdateRequest.getSubscriberId(),
                subscriberUpdateRequest.getDataUpdateId()
        )));
    }
}
