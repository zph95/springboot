package com.zph.programmer.springboot;

import com.zph.programmer.springboot.testconf.TestApplication;
import com.zph.programmer.springboot.testconf.TestConf;
import com.zph.programmer.springboot.utils.ReflectTestUtils;
import com.zph.programmer.springboot.utils.TestRunner;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@RunWith(TestRunner.class)
@SpringBootTest(classes = {TestConf.class, TestApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("utfake")
@Slf4j
public class BaseUnitilsTest {
    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void after() {
        ReflectTestUtils.revert();
    }

    @Ignore
    @Test
    public void t() {

    }
}
