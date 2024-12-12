package com.baiyi.cratos.eds.jenkins.model;

import lombok.Getter;

/**
 * Represents build console log
 */
@Getter
public class ConsoleLog {

    private String consoleLog;
    private Boolean hasMoreData;
    private Integer currentBufferSize;

    public ConsoleLog(String consoleLog, Boolean hasMoreData, Integer currentBufferSize) {
        this.consoleLog = consoleLog;
        this.hasMoreData = hasMoreData;
        this.currentBufferSize = currentBufferSize;
    }

    public ConsoleLog setConsoleLog(String consoleLog) {
        this.consoleLog = consoleLog;
        return this;
    }

    public ConsoleLog setHasMoreData(Boolean hasMoreData) {
        this.hasMoreData = hasMoreData;
        return this;
    }

    public ConsoleLog setCurrentBufferSize(Integer currentBufferSize) {
        this.currentBufferSize = currentBufferSize;
        return this;
    }

}