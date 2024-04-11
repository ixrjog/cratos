package com.baiyi.cratos.eds.aws.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author baiyi
 * @Date 2024/4/11 下午2:44
 * @Version 1.0
 */
public class InstanceModel {

    @Data
    @JsonIgnoreProperties
    public static class EC2InstanceType implements Serializable {

        @Serial
        private static final long serialVersionUID = 5987490614893113442L;

        private String memory; // GiB,

        private String clockSpeed; //Up to 3.3 GHz",
        private String physicalProcessor; //"Intel Xeon Family",
        private int vcpu; //1,
        private boolean currentGeneration;//true,
        @JsonIgnore
        private String prices;
        @JsonIgnore
        private String regions;
        @JsonIgnore
        private String ecu;
        @JsonIgnore
        private String gpu;
        private boolean enhancedNetworkingSupported;
        private String dedicatedEbsThroughput;
        private String networkPerformance;//"Low",
        private int normalizationSizeFactor;//0,
        private String storage;//EBS only",

//        public Integer acqMemory() {
//            try {
//                double number = (Double.parseDouble(memory.split(" ")[0]) * 1024);
//                return (int) number;
//            } catch (Exception e) {
//                return 0;
//            }
//        }
    }

}
