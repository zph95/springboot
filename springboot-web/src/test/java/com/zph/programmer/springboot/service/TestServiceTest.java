package com.zph.programmer.springboot.service;

import com.zph.programmer.springboot.BaseUnitilsTest;
import com.zph.programmer.springboot.po.RestCallLogRecord;
import com.zph.programmer.springboot.utils.jsoncomparer.JsonComparator;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.dbunit.annotation.ExpectedDataSet;

import javax.annotation.Resource;

@Slf4j
public class TestServiceTest extends BaseUnitilsTest {
    @Resource
    private TestService testService;


    @Test
    public void logTest() {
        //日志的级别；
        //由低到高   trace<debug<info<warn<error
        //可以调整输出的日志级别；日志就只会在这个级别及以后的高级别生效
        log.trace("这是trace日志...");
        log.debug("这是debug日志...");
        //SpringBoot默认给我们使用的是info级别的，没有指定级别的就用SpringBoot默认规定的级别；root level
        log.info("这是info日志...");
        log.warn("这是warn日志...");
        log.error("这是error日志...");
    }

    @Test
    public void pointLogTest() {
        try {
            testService.testPointLog("测试参数");
            testService.testPointLog(null);
        } catch (Exception e) {
            log.info("测试日志注解");
        }
    }

    /**
     * dbunit need junit4 test
     */
    @Test
    @DataSet(value = {"rest_call_log_record.init.xml"})
    @ExpectedDataSet(value = {"rest_call_log_record.init.xml"})
    public void findRestLogById() {

        RestCallLogRecord record = testService.findRestLogById(13);
        JsonComparator.newInstance("com/zph/programmer/springboot/service/findRestLogById.expected.json").compareAssert(record);

    }
}