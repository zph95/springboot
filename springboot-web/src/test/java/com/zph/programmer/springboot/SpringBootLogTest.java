package com.zph.programmer.springboot;

import com.zph.programmer.springboot.cache.CacheService;
import com.zph.programmer.springboot.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Slf4j
public class SpringBootLogTest extends SpringBootApplicationTest {
    @Autowired
    private TestService testService;
    @Qualifier("ehcache")
    @Autowired
    private CacheService cacheService;

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
    public void pointLogTest(){
        try {
            testService.testPointLog("测试参数");
            testService.testPointLog(null);
        } catch (Exception e) {
            log.info("测试日志注解");
        }
    }

   /* @Test
    public void cacheTest(){
        try {
            TestCtrl testCtrl = new TestCtrl(cacheService);
            testCtrl.testCachePost(new CacheService.KeyValue("test-cache", "测试缓存"));
            Assert.isTrue("测试缓存".equals(testCtrl.testCacheGet("test-cache").getData()), "缓存取值错误");
            testCtrl.testCachePut(new CacheService.KeyValue("test-cache", "更新缓存"));
            Assert.isTrue("更新缓存".equals(testCtrl.testCacheGet("test-cache").getData()), "缓存取值错误");
            testCtrl.testCacheDelete("test-cache");
        }catch (Exception e){
            log.error("缓存测试失败 e",e);
            Assert.isTrue(false,"缓存测试失败");
        }
    }*/
}
