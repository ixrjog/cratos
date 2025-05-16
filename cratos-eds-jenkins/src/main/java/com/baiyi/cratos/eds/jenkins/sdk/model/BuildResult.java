/*
 * Copyright (c) 2013 Cosmin Stejerean, Karl Heinz Marbaise, and contributors.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.baiyi.cratos.eds.jenkins.sdk.model;

public enum BuildResult {

    /**
     * 构建结果
     */
    FAILURE, UNSTABLE, REBUILDING, BUILDING,
    /**
     * This means a job was already running and has been aborted.
     */
    ABORTED,
    /**
     * 
     */
    SUCCESS,
    /**
     * ?
     */
    UNKNOWN,
    /**
     * This is returned if a job has never been built.
     */
    NOT_BUILT,
    /**
     * This will be the result of a job in cases where it has been cancelled
     * during the time in the queue.
     */
    CANCELLED

}