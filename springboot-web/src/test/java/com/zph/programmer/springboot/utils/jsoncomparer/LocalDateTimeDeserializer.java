package com.zph.programmer.springboot.utils.jsoncomparer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext deserializationContext)
            throws IOException {
        if (StringUtils.length(p.getValueAsString()) == StringUtils.length(DATE_TIME_FORMAT)) {
            return LocalDateTime.parse(p.getValueAsString(), DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
        }
        if (StringUtils.length(p.getValueAsString()) == StringUtils.length(DATE_FORMAT)) {
            LocalDate localDate = LocalDate.parse(p.getValueAsString(), DateTimeFormatter.ofPattern(DATE_FORMAT));
            return LocalDateTime.of(localDate, LocalTime.MIN);
        }
        if (StringUtils.length(p.getValueAsString()) == (StringUtils.length(DATE_FORMAT) + StringUtils.length(TIME_FORMAT) + 1)) {
            if (StringUtils.containsIgnoreCase(p.getValueAsString(), "T")) {
                LocalDate localDate = LocalDate.parse(p.getValueAsString(), DateTimeFormatter.ofPattern(DATE_FORMAT + "T" + TIME_FORMAT));
                return LocalDateTime.of(localDate, LocalTime.MIN);
            } else {
                LocalDate localDate = LocalDate.parse(p.getValueAsString(), DateTimeFormatter.ofPattern(DATE_FORMAT + " " + TIME_FORMAT));
                return LocalDateTime.of(localDate, LocalTime.MIN);
            }
        }

        return LocalDateTime.parse(p.getValueAsString());
    }
}