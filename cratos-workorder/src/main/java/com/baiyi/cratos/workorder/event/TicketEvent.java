package com.baiyi.cratos.workorder.event;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/21 16:41
 * &#064;Version 1.0
 */
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketEvent<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = -2657819212188448241L;

    public static final TicketEvent<Boolean> NO_EVENT = new TicketEvent<>(true);

    public static <T> TicketEvent<T> of(T body) {
        return new TicketEvent<>(body);
    }

    private T body;

}
