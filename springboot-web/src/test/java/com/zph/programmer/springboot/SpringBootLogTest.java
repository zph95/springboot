package com.zph.programmer.springboot;

import com.zph.programmer.springboot.cache.CacheService;
import com.zph.programmer.springboot.service.TestService;
import com.zph.programmer.springboot.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;


@Slf4j
public class SpringBootLogTest extends SpringBootApplicationTest {
    @Autowired
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


    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();//建议使用这种
    }


    /**
     * 1、mockMvc.perform执行一个请求。
     * 2、MockMvcRequestBuilders.get("XXX")构造一个请求。
     * 3、ResultActions.param添加请求传值
     * 4、ResultActions.accept(MediaType.TEXT_HTML_VALUE))设置返回类型
     * 5、ResultActions.andExpect添加执行完成后的断言。
     * 6、ResultActions.andDo添加一个结果处理器，表示要对结果做点什么事情
     * 比如此处使用MockMvcResultHandlers.print()输出整个响应结果信息。
     * 5、ResultActions.andReturn表示执行完成后返回相应的结果。
     */
    @Test
    public void cacheTest() {
        try {
            CacheService.KeyValue keyValue = new CacheService.KeyValue("test_cache", "测试缓存");
            String requestJson = JsonUtils.toJSONString(keyValue);
            //对post测试

            ResultActions resultActions = mockMvc.perform(
                    MockMvcRequestBuilders.post("/test/testCache")
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .content(requestJson)
            ).andExpect(MockMvcResultMatchers.status().isOk());
            resultActions.andReturn().getResponse().setCharacterEncoding("UTF-8");
            resultActions.andDo(MockMvcResultHandlers.print());
            //断言，判断返回的值是否正确
            String content = resultActions.andReturn().getResponse().getContentAsString();
            Assert.isTrue(content.equals("{\"code\":200,\"status\":\"success\",\"message\":null,\"moreInfo\":null,\"data\":\"OK\",\"success\":true}"),
                    "测试缓存失败");

            //对get测试
            resultActions = mockMvc.perform(
                    MockMvcRequestBuilders.get("/test/testCache")
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .param("key", keyValue.getKey())
            ).andExpect(MockMvcResultMatchers.status().isOk());
            //设置编码
            resultActions.andReturn().getResponse().setCharacterEncoding("UTF-8");
            resultActions.andDo(MockMvcResultHandlers.print());
            content = resultActions.andReturn().getResponse().getContentAsString();
            Assert.isTrue(content.equals("{\"code\":200,\"status\":\"success\",\"message\":null,\"moreInfo\":null,\"data\":\"测试缓存\",\"success\":true}"),
                    "测试缓存失败");

            CacheService.KeyValue keyValue2 = new CacheService.KeyValue("test_cache", "更新缓存");
            String requestJson2 = JsonUtils.toJSONString(keyValue2);
            //对put测试
            MvcResult mvcResultPut = mockMvc.perform(
                    MockMvcRequestBuilders.put("/test/testCache")
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .content(requestJson2)
            ).andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();
            content = mvcResultPut.getResponse().getContentAsString();
            Assert.isTrue(content.equals("{\"code\":200,\"status\":\"success\",\"message\":null,\"moreInfo\":null,\"data\":\"OK\",\"success\":true}"),
                    "测试缓存失败");
            //对delete测试
            MvcResult mvcResultDelete = mockMvc.perform(
                    MockMvcRequestBuilders.delete("/test/testCache")
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .param("key", keyValue.getKey())
            ).andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();
            content = mvcResultDelete.getResponse().getContentAsString();
            Assert.isTrue(content.equals("{\"code\":200,\"status\":\"success\",\"message\":null,\"moreInfo\":null,\"data\":\"OK\",\"success\":true}"),
                    "测试缓存失败");

        }catch (Exception e){
            log.error("缓存测试失败 e",e);
            Assert.isTrue(false,"缓存测试失败");
        }
    }
}
