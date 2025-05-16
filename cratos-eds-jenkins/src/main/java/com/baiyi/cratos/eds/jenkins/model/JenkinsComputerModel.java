package com.baiyi.cratos.eds.jenkins.model;

import com.baiyi.cratos.eds.jenkins.sdk.model.ComputerLabel;
import com.baiyi.cratos.eds.jenkins.sdk.model.Executor;
import com.baiyi.cratos.eds.jenkins.sdk.model.OfflineCause;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/16 10:56
 * &#064;Version 1.0
 */
public class JenkinsComputerModel {

    @SuppressWarnings("rawtypes")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Computer implements Serializable {
        @Serial
        private static final long serialVersionUID = -980664752063102952L;

        private String displayName;
        private List actions; //TODO: What kind of List?
        private List<Executor> executors;
        private List<ComputerLabel> assignedLabels;
        private Boolean idle;
        private Boolean jnlp;
        private Boolean launchSupported;
        private Boolean manualLaunchAllowed;
        private Map monitorData; //TODO: What kind of map?
        private Integer numExecutors;
        private Boolean offline;
        private OfflineCause offlineCause;
        private String offlineCauseReason;
        private List oneOffExecutors; //TODO: What kind of List?
        private Boolean temporarilyOffline;
    }

}
