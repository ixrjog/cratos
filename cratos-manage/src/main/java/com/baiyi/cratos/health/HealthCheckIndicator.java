package com.baiyi.cratos.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/10/28 15:00
 * &#064;Version 1.0
 */
@Component
public class HealthCheckIndicator implements HealthIndicator {

    @Override
    public Health health() {
        int errorCode = check();
        if (errorCode != 0) {
            return Health.down()
                    .withDetail("errorCode", errorCode)
                    .build();
        }
        return Health.up()
                .build();
    }

    private int check() {
        return 0;
    }

}
