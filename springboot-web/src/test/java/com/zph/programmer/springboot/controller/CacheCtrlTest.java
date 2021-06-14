package com.zph.programmer.springboot.controller;

import com.zph.programmer.springboot.SpringBootApplicationTest;
import com.zph.programmer.springboot.cache.CacheService;
import com.zph.programmer.springboot.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.web.context.WebApplicationContext;

@Slf4j
public class CacheCtrlTest extends SpringBootApplicationTest {
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
            Assertions.assertEquals("{\"code\":200,\"status\":\"success\",\"message\":null,\"moreInfo\":null,\"data\":\"OK\",\"success\":true}",
                    content);

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
            Assertions.assertEquals("{\"code\":200,\"status\":\"success\",\"message\":null,\"moreInfo\":null,\"data\":\"测试缓存\",\"success\":true}",
                    content);

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
            Assertions.assertEquals("{\"code\":200,\"status\":\"success\",\"message\":null,\"moreInfo\":null,\"data\":\"OK\",\"success\":true}",
                    content);
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
            Assertions.assertEquals("{\"code\":200,\"status\":\"success\",\"message\":null,\"moreInfo\":null,\"data\":\"OK\",\"success\":true}",
                    content);

        } catch (Exception e) {
            log.error("缓存测试失败 e", e);
            Assertions.fail("缓存测试失败", e);
        }
    }
}
