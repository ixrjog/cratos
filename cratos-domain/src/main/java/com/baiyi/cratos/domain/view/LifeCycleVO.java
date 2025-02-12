package com.baiyi.cratos.domain.view;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/12 09:55
 * &#064;Version 1.0
 */
public class LifeCycleVO {

    public static void invoke(HasLifeCycleExpires hasLifeCycleExpires) {
        Expires expires = Expires.builder()
                .expiredTime(hasLifeCycleExpires.getExpiredTime())
                .build();
        if (hasLifeCycleExpires.getExpiredTime() == null) {
            expires.setSurvivalRate(100);
        } else {
            int days = LifeCycleVO.calculateDateDifferenceByDay(new Date(), hasLifeCycleExpires.getExpiredTime());
            if (days <= 0) {
                expires.setSurvivalRate(0);
            } else {
                if (days >= 30) {
                    expires.setSurvivalRate(100);
                } else {
                    expires.setSurvivalRate(days * 100 / 30);
                }
            }
        }
        hasLifeCycleExpires.setLifeCycleExpires(expires);
    }

    private static int calculateDateDifferenceByDay(Date from, Date to) {
        long subTime = to.getTime() - from.getTime();
        if (subTime < 0) {
            throw new RuntimeException("计算时间有误!");
        }
        long diff = subTime / TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);
        return (int) diff;
    }

    public interface HasLifeCycleExpires {
        Date getExpiredTime();

        void setLifeCycleExpires(Expires expires);
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class Expires implements Serializable {
        @Serial
        private static final long serialVersionUID = 287487054916730530L;
        private Date expiredTime;
        private int survivalRate;
    }

}
