package com.baiyi.cratos.common.util;

import lombok.NoArgsConstructor;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/8/22 14:41
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class SshIdUtils {

    private static final String CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyz";
    private static final int CHARSET_SIZE = CHARACTERS.length();

    public static String generateID() {
        return SshIdUtils.generateID(8);
    }

    private static String generateID(int length) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return IntStream.range(0, length)
                .map(i -> random.nextInt(CHARSET_SIZE))
                .mapToObj(index -> String.valueOf(CHARACTERS.charAt(index)))
                .collect(Collectors.joining());
    }

}
