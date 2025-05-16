/*
 * Copyright (c) 2013 Cosmin Stejerean, Karl Heinz Marbaise, and contributors.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.baiyi.cratos.eds.jenkins.sdk.model;

import com.baiyi.cratos.eds.jenkins.sdk.client.JenkinsHttpConnection;
import lombok.Getter;

import java.util.function.Predicate;

/**
 * The base model.
 */
@Getter
public class BaseModel {

    /**
     * The class.
     * -- GETTER --
     *  Get the class.
     *
     * @return class

     */
    private String _class;

    /**
     * -- GETTER --
     *  Get the HTTP client.
     *
     * @return client
     */
    //TODO: We should make this private
    protected JenkinsHttpConnection client;


    /**
     * Set the HTTP client.
     * @param client {@link JenkinsHttpConnection}.
     */
    public BaseModel setClient(final JenkinsHttpConnection client) {
        this.client = client;
        return this;
    }

    protected static Predicate<? super Build> isBuildNumberEqualTo(int buildNumber) {
        return build -> build.getNumber() == buildNumber;
    }

}