package com.baiyi.cratos.eds.jenkins.sdk.model;

import lombok.Getter;

@Getter
public class ExtractHeader extends BaseModel {

    private String location;

    public ExtractHeader setLocation(String value) {
        location = value;
        return this;
    }

}