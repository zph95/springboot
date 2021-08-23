package com.zph.programmer.springboot.utils;


import java.time.ZoneOffset;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 * Java 中的 Unix 时间
 * Java 确保：每天 24 小时、每小时 60 分、每分钟 60 秒。
 * <p>
 * Java 中获取 “当前” 时间的方法，其底层实现，全部由 java.lang.System.currentTimeMillis() 提供自 UTC 1970-01-01T00:00:00 的毫秒数。java.lang.System.currentTimeMillis() 作为 native 方法，其实现与 JVM 所在的机器相关（通常使用 NTP 协议保持更新）。
 * <p>
 * LocalDate、LocalTime、LocalDateTime
 * java.time.LocalDate 用于表示 “本地日期”，无 “时间”。LocalDate 不承载时区信息。
 * <p>
 * java.time.LocalTime 用于表示 “本地时间”，无 “日期”。LocalTime 不承载时区信息。
 * <p>
 * java.time.LocalDateTime 用于表示 “本地日期与时间”。LocalDateTime 不承载时区信息。
 * <p>
 * LocalDate 实例与 LocalTime 实例能够共同构建 LocalDateTime 实例，由 LocalDateTime 实例能够获取 LocalDate 实例与 LocalTime 实例。
 * <p>
 * 由于 LocalDateTime 不承载时区信息，因此，其不能与 Instant 相互转换，必须提供时区信息。
 */
@Slf4j
public class DateUtil {

    public static final String YEAR_MONTH_DATE_PATTEN = "yyyyMMdd";

    public static final String YEAR_MONTH_PATTEN = "yyyyMM";

    public static final String DEFAULT_DETE_PATTERN = "yyyy-MM-dd";

    public static final String COMPLETE_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final String DETE_PATTERN_TIGHT = "yyyyMMddHHmmss";

    private static final Map<String, DateTimeFormatter> DATE_TIME_FORMATTER_MAP = new HashMap<>();

    static {
        DATE_TIME_FORMATTER_MAP.put(YEAR_MONTH_DATE_PATTEN, DateTimeFormatter.ofPattern(YEAR_MONTH_DATE_PATTEN));
        DATE_TIME_FORMATTER_MAP.put(YEAR_MONTH_PATTEN, DateTimeFormatter.ofPattern(YEAR_MONTH_PATTEN));
        DATE_TIME_FORMATTER_MAP.put(DEFAULT_DETE_PATTERN, DateTimeFormatter.ofPattern(DEFAULT_DETE_PATTERN));
        DATE_TIME_FORMATTER_MAP.put(COMPLETE_DATE_PATTERN, DateTimeFormatter.ofPattern(COMPLETE_DATE_PATTERN));
        DATE_TIME_FORMATTER_MAP.put(DETE_PATTERN_TIGHT, DateTimeFormatter.ofPattern(DETE_PATTERN_TIGHT));
    }

    public static void main(String[] args) {
        System.out.println(getNow());
    }

    public static LocalDateTime getNow() {
        ZoneId zoneId = ZoneId.ofOffset("UTC", ZoneOffset.of("+0"));
        LocalDateTime now = LocalDateTime.now(zoneId);;
        System.out.println(now);

        ZonedDateTime convertTime = ZonedDateTime.now(zoneId);
        System.out.println(convertTime);
        return convertTime.toLocalDateTime();
    }


    /**
     * 时间戳转 date
     *
     * @param date    date
     * @param pattern pattern
     * @return result
     */
    public static String formatDate(LocalDateTime date, String pattern) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = date.atZone(zoneId);
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(Date.from(zonedDateTime.toInstant()));

    }

    /**
     * 日期格式化
     *
     * @param date    日期LocalDate
     * @param pattern 格式
     * @return result
     */
    public static String formatDate(LocalDate date, String pattern) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = date.atStartOfDay(zoneId);
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(Date.from(zonedDateTime.toInstant()));

    }

    /**
     * 日期格式化
     *
     * @param date   日期Date
     * @param format 格式
     * @return result
     */
    public static String formatDate(Date date, String format) {
        return new SimpleDateFormat(format).format(date);

    }

    /**
     * 日期格式化
     *
     * @param date   Date日期
     * @param format 格式
     * @return result
     */
    public static LocalDate formatDate(String date, String format) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(format));
    }

    /**
     * 获取今天之后365个自然日的日期
     *
     * @param day 天数
     * @return result
     */
    public static String getDayAfter365(String day) {
        return LocalDate.parse(day, DateTimeFormatter.ofPattern("yyyyMMdd")).plusDays(365L).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }


    /**
     * 根据日期获取前n季度
     *
     * @param date 日期
     * @param term 季度
     * @return result
     */
    public static String calculateDateByTerm(String date, int term) {
        LocalDate date2 = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyyMMdd"));
        date2 = date2.minusMonths(3L * term).with(TemporalAdjusters.lastDayOfMonth());
        return date2.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    /**
     * getFormatDate
     *
     * @param date date
     * @return result
     */
    public static String getFormatDate(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyyMMdd")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    /**
     * getYearsBetweenDate
     *
     * @param date1 date1
     * @param date2 date2
     * @return result
     */
    public static int getYearsBetweenDate(String date1, String date2) {
        LocalDate localDate1 = LocalDate.parse(date1, DateTimeFormatter.ofPattern("yyyyMMdd"));
        LocalDate localDate2 = LocalDate.parse(date2, DateTimeFormatter.ofPattern("yyyyMMdd"));
        return localDate1.until(localDate2).getYears();

    }

    /**
     * getToday
     *
     * @return result
     */
    public static String getToday() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    /**
     * getYearMonthDate
     *
     * @param date   date
     * @param format format
     * @return result
     */
    public static String getYearMonthDate(String date, String format) {
        if (StringUtils.isEmpty(date)) {
            return null;
        }
        LocalDate localDate = DateUtil.parseLocalDate(date);
        if (localDate != null) {
            return localDate.format(DATE_TIME_FORMATTER_MAP.get(format));
        } else {
            return null;
        }

    }

    /**
     * getYearMonthDate
     *
     * @param localDateTime localDateTime
     * @return result
     */
    public static String getYearMonthDate(LocalDateTime localDateTime) {

        if (localDateTime != null) {
            return localDateTime.format(DATE_TIME_FORMATTER_MAP.get(YEAR_MONTH_DATE_PATTEN));
        } else {
            return null;
        }
    }

    /**
     * getYearMonthDate
     *
     * @param localDate localDate
     * @return result
     */
    public static String getYearMonthDate(LocalDate localDate) {

        if (localDate != null) {
            return localDate.format(DATE_TIME_FORMATTER_MAP.get(YEAR_MONTH_DATE_PATTEN));
        } else {
            return null;
        }
    }

    /**
     * <p>解析日期字段，注意目前仅支持日期在前，如果存在时间部分则日期和时间的间隔符是 ”或“T”。
     * 日期格式是yyyy-MM-dd，yyyy/MM/dd</p>
     *
     * @param jsonValue 日期字符串
     * @return 返回解析到的日期形式
     */
    public static LocalDate parseLocalDate(String jsonValue) {
        if (StringUtils.isBlank(jsonValue)) {
            return null;
        }
        if (jsonValue.length() == 8 && StringUtils.isAlphanumeric(jsonValue)) {
            return LocalDate.parse(jsonValue, DATE_TIME_FORMATTER_MAP.get(YEAR_MONTH_DATE_PATTEN));
        } else if (jsonValue.length() == 6) {
            return LocalDate.parse(jsonValue, DATE_TIME_FORMATTER_MAP.get(YEAR_MONTH_PATTEN));
        }
        String[] patterns = new String[]{"T", " "};
        for (String pattern : patterns) {
            int tIndex = jsonValue.indexOf(pattern);
            if (tIndex > 0) {
                String datePart = jsonValue.substring(0, tIndex);
                String[] dateCharacters = new String[]{"-", "/"};
                for (String dateCharacter : dateCharacters) {
                    if (datePart.contains(dateCharacter)) {
                        String[] dateParts = datePart.split(dateCharacter);
                        if (dateParts.length == 3) {
                            return LocalDate.of(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]));
                        }
                    }
                }
            }
        }
        if (jsonValue.length() >= 10) {
            String datePart = jsonValue.substring(0, 10);
            String[] dateCharacters = new String[]{"-", "/"};
            for (String dateCharacter : dateCharacters) {
                if (datePart.contains(dateCharacter)) {
                    String[] dateParts = datePart.split(dateCharacter);
                    if (dateParts.length == 3) {
                        return LocalDate.of(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]));
                    }
                }
            }
        }
        return null;
    }

    /**
     * 获取日期的季度
     *
     * @param date date
     * @return result
     */
    public static int getQuarter(LocalDate date) {
        return (date.getMonthValue() - 1) / 3 + 1;
    }

    /**
     * 获取日期季度的最后一天
     *
     * @param date date
     * @return result
     */
    public static LocalDate getLastDayOfQuarter(LocalDate date) throws IllegalArgumentException {
        switch (getQuarter(date)) {
            case 1:
                return LocalDate.of(date.getYear(), 3, 31);
            case 2:
                return LocalDate.of(date.getYear(), 6, 30);
            case 3:
                return LocalDate.of(date.getYear(), 9, 30);
            case 4:
                return LocalDate.of(date.getYear(), 12, 31);
            default:
                throw new IllegalArgumentException("should not reach here Quarter");
        }
    }


    /**
     * compareTo
     *
     * @param date1 date1
     * @param date2 date2
     * @return result
     */
    public static int compareTo(String date1, String date2) {
        return Integer.valueOf(date1).compareTo(Integer.valueOf(date2));
    }


    /**
     * getDateFromStartToEnd
     *
     * @param startDate startDate
     * @param endDate   endDate
     * @return result
     */
    public static List<String> getDateFromStartToEnd(String startDate, String endDate) {
        List<String> resultDate = new ArrayList<>();
        LocalDate startLocalDate = DateUtil.parseLocalDate(startDate);
        LocalDate endLocalDate = DateUtil.parseLocalDate(endDate);
        if (startLocalDate == null || endLocalDate == null) {
            return new ArrayList<>();
        }
        LocalDate localDate = startLocalDate;
        while (localDate.compareTo(endLocalDate) <= 0) {
            resultDate.add(localDate.format(DATE_TIME_FORMATTER_MAP.get(YEAR_MONTH_DATE_PATTEN)));
            localDate = localDate.plusDays(1L);
        }
        return resultDate;
    }

    public static List<String> getDateMonthsFromStartToEnd(String startDate, String endDate) {
        List<String> resultDate = new ArrayList<>();
        LocalDate startLocalDate = DateUtil.parseLocalDate(startDate);
        LocalDate endLocalDate = DateUtil.parseLocalDate(endDate);
        if (startLocalDate == null || endLocalDate == null) {
            return new ArrayList<>();
        }
        LocalDate localDate = startLocalDate;
        while (localDate.compareTo(endLocalDate) <= 0) {
            resultDate.add(localDate.format(DATE_TIME_FORMATTER_MAP.get(YEAR_MONTH_PATTEN)));
            localDate = localDate.plusMonths(1L);
        }
        return resultDate;
    }

}
