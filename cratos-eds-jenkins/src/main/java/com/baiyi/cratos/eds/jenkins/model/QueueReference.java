package com.baiyi.cratos.eds.jenkins.model;

public class QueueReference extends BaseModel {

    private final String queueItem;

    public QueueReference(String location) {
        queueItem = location;
    }

    public String getQueueItemUrlPart() {
        return queueItem;
    }

}