package com.example.domain.subscriberDataUpdate.aggregate;

import com.example.domain.subscriberDataUpdate.common.Result;
import com.example.domain.subscriberDataUpdate.idontknow.DataUpdate;
import com.example.domain.subscriberDataUpdate.model.Subscriber;
import com.example.domain.subscriberDataUpdate.idontknow.SubscriberUpdateRequest;
import com.example.domain.subscriberDataUpdate.valueobject.DataUpdateId;
import com.example.domain.subscriberDataUpdate.valueobject.SubscriberId;

//AggregateRoot
public class SubscriberDataUpdate {
    private final DataUpdate dataUpdate;
    private final Subscriber subscriber;
    private final SubscriberId subscriberId;
    private final DataUpdateId dataUpdateId;

    private SubscriberDataUpdate(DataUpdate dataUpdate, Subscriber subscriber) {
        this.dataUpdate = dataUpdate;
        this.subscriber = subscriber;
        this.subscriberId = subscriber.subscriberId();
        this.dataUpdateId = dataUpdate.dataUpdateId();
    }

    public DataUpdateId getDataUpdateId() {
        return this.dataUpdateId;
    }

    public DataUpdate getDataUpdate() {
        return dataUpdate;
    }

    public Subscriber getSubscriber() {
        return subscriber;
    }

    public static Result<SubscriberDataUpdate> emerge(DataUpdate dataUpdate, Result<Subscriber> subscriberResult) {
        return subscriberResult.map(subscriber -> new SubscriberDataUpdate(dataUpdate, subscriber));
    }

    public Result<SubscriberUpdateRequest> prepareUpdateRequest() {
        return isUpdateRequired()
                ? createSubscriberUpdateRequest()
                : failNoUpdateRequired();
    }

    private boolean isUpdateRequired() {
        return !subscriber.mobileRegionId().equals(dataUpdate.mobileRegionId());
    }

    private Result<SubscriberUpdateRequest> createSubscriberUpdateRequest() {
        return Result.success(
                new SubscriberUpdateRequest(
                        subscriberId.value(),
                        dataUpdate.msisdn().value(),
                        dataUpdate.mobileRegionId().value(),
                        this)
        );
    }

    private Result<SubscriberUpdateRequest> failNoUpdateRequired() {
        return Result.failure(new RuntimeException("No UpdateRequred"));
    }
}
