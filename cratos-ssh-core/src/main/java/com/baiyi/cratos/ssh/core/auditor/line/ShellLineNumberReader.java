package com.baiyi.cratos.ssh.core.auditor.line;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/9 14:48
 * &#064;Version 1.0
 */
public class ShellLineNumberReader extends LineNumberReader {

    public ShellLineNumberReader(Reader in) {
        super(in);
    }

    public String readShellLine() throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = super.readLine()) != null) {
            sb.append(line);
            if (!line.endsWith("\\n")) {
                break;
            }
        }
        return !sb.isEmpty() ? sb.toString() : null;
    }

}
