package com.zph.programmer.springboot.servlet;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;

@Slf4j
public class BodyReaderResponseWrapper extends HttpServletResponseWrapper {
    private final ByteArrayOutputStream output;

    public BodyReaderResponseWrapper(HttpServletResponse response) {
        super(response);
        output = new ByteArrayOutputStream();
    }

    /**
     */
    @Override
    public ServletOutputStream getOutputStream() {
         return new ServletOutputStream() {
            @Override
            public void write(int b) {
                output.write(b);
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setWriteListener(WriteListener writeListener) {
            }
        };
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return new PrintWriter(new OutputStreamWriter(this.getOutputStream()));
    }

    /**
     * 获取返回值
     */
    public String getBody() throws UnsupportedEncodingException {
        return output.toString();
    }
}




