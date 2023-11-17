
package com.zph.programmer.springdemo.utils.jsoncomparer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;


public class DateDeserializer extends JsonDeserializer<Date> {

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Override
    public Date deserialize(JsonParser p, DeserializationContext ctxt) {
        try {
            if (StringUtils.length(p.getValueAsString()) == StringUtils.length(DATE_TIME_FORMAT)) {
                return DateUtils.parseDate(p.getValueAsString(), DATE_TIME_FORMAT);

            }
            if (StringUtils.length(p.getValueAsString()) == StringUtils.length(DATE_FORMAT)) {
                return DateUtils.parseDate(p.getValueAsString(), DATE_FORMAT);
            }
            if (StringUtils.length(p.getValueAsString()) == (StringUtils.length(DATE_FORMAT) + StringUtils.length(TIME_FORMAT) + 1)) {
                if (StringUtils.containsIgnoreCase(p.getValueAsString(), "T")) {


                    String pattern = DATE_FORMAT + "T" + TIME_FORMAT;
                    return DateUtils.parseDate(p.getValueAsString(), pattern);
                } else {
                    String pattern = DATE_FORMAT + " " + TIME_FORMAT;

                    return DateUtils.parseDate(p.getValueAsString(), pattern);
                }
            }
            Date date = DateUtils.parseDate(p.getValueAsString());
            return date;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
