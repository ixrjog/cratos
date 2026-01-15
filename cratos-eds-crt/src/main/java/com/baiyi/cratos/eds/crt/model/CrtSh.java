package com.baiyi.cratos.eds.crt.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * &#064;Author  baiyi
 * &#064;Date  2026/1/14 17:54
 * &#064;Version 1.0
 */
public class CrtSh {

    public static class FlexibleDateDeserializer extends JsonDeserializer<Date> {
        private static final SimpleDateFormat FORMAT_WITH_MILLIS = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        private static final SimpleDateFormat FORMAT_WITHOUT_MILLIS = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        static {
            FORMAT_WITH_MILLIS.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
            FORMAT_WITHOUT_MILLIS.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
        }

        @Override
        public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String date = p.getText();
            try {
                return date.contains(".") ? FORMAT_WITH_MILLIS.parse(date) : FORMAT_WITHOUT_MILLIS.parse(date);
            } catch (ParseException e) {
                throw new IOException("Failed to parse date: " + date, e);
            }
        }
    }

    @Data
    public static class CertificateLog implements Serializable {
        @Serial
        private static final long serialVersionUID = -3308544018520221606L;
        private Long id;
        @JsonProperty("issuer_ca_id")
        private Long issuerCaId;
        @JsonProperty("issuer_name")
        private String issuerName;
        @JsonProperty("common_name")
        private String commonName;
        @JsonProperty("name_value")
        private String nameValue;
        @JsonProperty("not_after")
        @JsonFormat(timezone = "UTC", pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private Date notAfter;
        @JsonProperty("not_before")
        @JsonFormat(timezone = "UTC", pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private Date notBefore;
        @JsonProperty("entry_timestamp")
        @JsonDeserialize(using = FlexibleDateDeserializer.class)
        private Date entryTimestamp;
        @JsonProperty("result_count")
        private Integer resultCount;
        @JsonProperty("serial_number")
        private String serialNumber;
        private String domainName;
    }

}
