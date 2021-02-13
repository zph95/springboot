package com.zph.programmer.springboot.utils;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

/**
 * @Author: zengpenghui
 * @Date: 20-7-22 下午5:42
 * @Version 1.0
 */
public class LogFilterUtil extends Filter<ILoggingEvent> {
    @Override
    public FilterReply decide(ILoggingEvent event) {
        if (event.getLoggerName() != null) {
            if (event.getLoggerName().startsWith("com.zph.programmer.springboot.dao")) {
                return FilterReply.DENY;
            } else {
                return FilterReply.ACCEPT;
            }
        } else {
            return FilterReply.NEUTRAL;
        }
    }
}

