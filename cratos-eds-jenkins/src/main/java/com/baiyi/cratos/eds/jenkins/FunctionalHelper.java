package com.baiyi.cratos.eds.jenkins;

import com.baiyi.cratos.eds.jenkins.sdk.client.JenkinsHttpConnection;
import com.baiyi.cratos.eds.jenkins.sdk.model.BaseModel;

import java.util.function.Function;

public final class FunctionalHelper {

    private FunctionalHelper() {
        // intentionally empty.
    }

    public static <T extends BaseModel> Function<T, T> SET_CLIENT(JenkinsHttpConnection client) {
        return s -> {
            s.setClient(client);
            return s;
        };
    }

}