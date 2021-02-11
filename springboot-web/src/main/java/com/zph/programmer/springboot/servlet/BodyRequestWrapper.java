package com.zph.programmer.springboot.servlet;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

@Slf4j
public class BodyRequestWrapper extends HttpServletRequestWrapper {
    @Getter
    private String body=null;

    public BodyRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        if (request.getInputStream() != null) {
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
                StringBuilder stringBuilder = new StringBuilder();
                char[] charBuffer = new char[128];
                int bytesRead;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
                body = stringBuilder.toString();
            } catch (Exception ex) {
                log.error("获取请求体body异常：", ex);
                throw ex;
            }
        }
    }

    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes());
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return true;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }
            @Override
            public int read() {
                return byteArrayInputStream.read();
            }
        };
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }
}