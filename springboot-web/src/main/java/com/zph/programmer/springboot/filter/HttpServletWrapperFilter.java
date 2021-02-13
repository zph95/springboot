package com.zph.programmer.springboot.filter;

import com.zph.programmer.api.enums.RestCallLogStatusEnum;
import com.zph.programmer.springboot.dao.RestCallLogRecordMapper;
import com.zph.programmer.springboot.po.RestCallLogRecord;
import com.zph.programmer.springboot.servlet.BodyRequestWrapper;
import com.zph.programmer.springboot.servlet.BodyResponseWrapper;
import com.zph.programmer.springboot.utils.SpringBeanUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;


@Slf4j
public class HttpServletWrapperFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("init filter 打印REST接口日志过滤器");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        RestCallLogRecordMapper restCallLogRecordMapper= SpringBeanUtils.getBean(RestCallLogRecordMapper.class);
        BodyRequestWrapper requestWrapper = null;
        HttpServletRequest httpServletRequest = null;
        BodyResponseWrapper responseWrapper = null;
        HttpServletResponse httpServletResponse = null;
        if (request instanceof HttpServletRequest) {
            httpServletRequest = (HttpServletRequest) request;
            requestWrapper = new BodyRequestWrapper(httpServletRequest);
        }
        if (response instanceof HttpServletResponse) {
            httpServletResponse = (HttpServletResponse) response;
            responseWrapper = new BodyResponseWrapper(httpServletResponse);
        }
        RestCallLogRecord record=new RestCallLogRecord();
        if (httpServletRequest != null) {
            StringBuilder requestParamsBuilder = new StringBuilder();
            if (httpServletRequest.getQueryString() != null) {
                //针对param入参
                requestParamsBuilder.append(URLDecoder.decode(httpServletRequest.getQueryString(), StandardCharsets.UTF_8));
            } else {
                //针对body入参
                requestParamsBuilder.append(requestWrapper.getBody());
            }
            String url=httpServletRequest.getScheme() + "://" + httpServletRequest.getServerName() + ":" + httpServletRequest.getServerPort()
                    + httpServletRequest.getRequestURI();
            String sb = "\n********************************* " + LocalDateTime.now().toString() + "\n" +
                    "ServerAddress :  " + url +"  "+ httpServletRequest.getMethod() + "\n" +
                    "ContentType   :  " + httpServletRequest.getContentType() + "\n" +
                    "RequestParams :  " + requestParamsBuilder.toString() + "\n" +
                    "********************************* Request\n";
            log.info(sb);
            record.setUri(url).setMethod(httpServletRequest.getMethod()).setRequest(requestParamsBuilder.toString())
                    .setStatus(RestCallLogStatusEnum.Start.getCode());
            restCallLogRecordMapper.insert(record);
        }
        Long start = System.currentTimeMillis();
        chain.doFilter(requestWrapper != null ? requestWrapper : request,
                responseWrapper != null ? responseWrapper : response);
        Long end = System.currentTimeMillis();
        if (httpServletResponse != null) {
            String body = responseWrapper.getBody();
            String sb = "\n********************************* " + LocalDateTime.now().toString() + "\n" +
                    "status        :  " + httpServletResponse.getStatus() + "\n" +
                    "ContentType   :  " + httpServletResponse.getContentType() + "\n" +
                    "Response      :  " + body + "\n" +
                    "********************************* Response cost=" + (end - start) + "ns \n";
            log.info(sb);
            record.setResponse(body).setCostTime(end - start)
                    .setStatus(httpServletResponse.getStatus());
            restCallLogRecordMapper.updateByPrimaryKey(record);
            //重新 writer 返回值，重要！！！
            response.getOutputStream().write(body.getBytes());
        }

    }

    @Override
    public void destroy() {

    }
}


