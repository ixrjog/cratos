package com.baiyi.cratos.facade.auth.wrapper;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 响应包装器，捕获响应体用于加密
 */
public class EncryptedResponseWrapper extends HttpServletResponseWrapper {

    private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    private ServletOutputStream outputStream;
    private PrintWriter writer;

    public EncryptedResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (outputStream == null) {
            outputStream = new ServletOutputStream() {
                @Override
                public boolean isReady() { return true; }

                @Override
                public void setWriteListener(WriteListener listener) {}

                @Override
                public void write(int b) throws IOException {
                    buffer.write(b);
                }
            };
        }
        return outputStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (writer == null) {
            writer = new PrintWriter(getOutputStream());
        }
        return writer;
    }

    public byte[] getResponseBody() {
        if (writer != null) {
            writer.flush();
        }
        return buffer.toByteArray();
    }

}
