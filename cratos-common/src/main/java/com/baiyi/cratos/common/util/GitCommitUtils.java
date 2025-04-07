package com.baiyi.cratos.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/7 11:47
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GitCommitUtils {

    public static String readGitProperties() {
        ClassLoader classLoader = GitCommitUtils.class.getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream("git.properties")) {
            return readFromInputStream(inputStream);
        } catch (Exception e) {
            return "Unknown version.";
        }
    }

    private static String readFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder result = new StringBuilder();
        try (BufferedReader br = new BufferedReader((new InputStreamReader(inputStream, StandardCharsets.UTF_8)))) {
            String line;
            while ((line = br.readLine()) != null) {
                result.append(line)
                        .append(System.lineSeparator());
            }
        }
        return result.toString();
    }

}
