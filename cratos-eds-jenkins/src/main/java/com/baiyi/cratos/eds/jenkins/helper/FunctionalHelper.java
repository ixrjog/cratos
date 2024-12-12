package com.baiyi.cratos.eds.jenkins.helper;

import com.baiyi.cratos.eds.jenkins.client.JenkinsHttpConnection;
import com.baiyi.cratos.eds.jenkins.model.BaseModel;

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