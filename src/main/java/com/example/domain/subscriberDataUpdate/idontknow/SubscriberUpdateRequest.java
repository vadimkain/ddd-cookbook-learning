package com.example.domain.subscriberDataUpdate.idontknow;

import com.example.domain.subscriberDataUpdate.aggregate.SubscriberDataUpdate;

public class SubscriberUpdateRequest {
    private final String subscriberId;
    private final String msisdn;
    private final String mobileRegionId;
    private final SubscriberDataUpdate dataUpdate;
    private final String dataUpdateId;

    public SubscriberUpdateRequest(String subscriberId, String msisdn, String mobileRegionId, SubscriberDataUpdate dataUpdate) {
        this.subscriberId = subscriberId;
        this.msisdn = msisdn;
        this.mobileRegionId = mobileRegionId;
        this.dataUpdate = dataUpdate;
        this.dataUpdateId = dataUpdate.getDataUpdateId().value();
    }

    public String getSubscriberId() {
        return subscriberId;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public String getMobileRegionId() {
        return mobileRegionId;
    }

    public SubscriberDataUpdate getDataUpdate() {
        return dataUpdate;
    }

    public String getDataUpdateId() {
        return dataUpdateId;
    }
}
