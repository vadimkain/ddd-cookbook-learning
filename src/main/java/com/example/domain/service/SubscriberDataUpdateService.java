package com.example.domain.service;

import com.example.domain.*;
import com.example.domain.common.Result;
import com.example.domain.maybe_entity_or_command.DataUpdate;
import com.example.domain.maybe_entity_or_command.SubscriberDataUpdateResponse;
import com.example.domain.events.UnvalidatedDataUpdateRequest;
import com.example.domain.events.ValidatedDataUpdateRequest;
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
                .flatMap(result -> result.fold(
                        value -> requestSubscriberUpdate(value),
                        exception -> Mono.just(Result.failure(exception))
                ))
                .flatMap(result -> result.fold(
                        dataUpdate -> requestCurrentSubscriber(dataUpdate),
                        exception -> Mono.just(Result.failure(exception))
                ));
    }

    private Mono<Result<ValidatedDataUpdateRequest>> validateRequest(UnvalidatedDataUpdateRequest unvalidatedDataUpdateRequest) {
        return Mono.just(ValidatedDataUpdateRequest.emerge(unvalidatedDataUpdateRequest));
    }

    private Mono<Result<DataUpdate>> requestSubscriberUpdate(ValidatedDataUpdateRequest validRequest) {
        return subscribersClient.findDataUpdate(validRequest);
    }
}
