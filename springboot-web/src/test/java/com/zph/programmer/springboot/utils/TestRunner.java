package com.zph.programmer.springboot.utils;

import org.junit.internal.runners.InitializationError;
import org.junit.internal.runners.TestClass;
import org.junit.runner.notification.RunNotifier;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.unitils.UnitilsJUnit4TestClassRunner;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 兼容springboot@Bean @Autowired，unitils
 */
public class TestRunner extends UnitilsJUnit4TestClassRunner {

    private final List<Method> testMethods;
    private SpringRunner springRunner;
    private TestClass testClass;

    public TestRunner(Class<?> clazz) throws org.junit.runners.model.InitializationError, InitializationError {
        super(clazz);
        this.testClass = new TestClass(clazz);
        this.testMethods = testClass.getTestMethods();
        springRunner = new SpringRunner(clazz);
    }

    @Override
    public void run(RunNotifier notifier) {
        super.run(notifier);
    }

    @Override
    protected Object createTest() throws Exception {
        //使用SpringJUnit4ClassRunner.createTest(), 兼容@Bean @Autowired
        return springRunner.createTest();
    }

    public static class SpringRunner extends SpringJUnit4ClassRunner {
        public SpringRunner(Class<?> clazz) throws org.junit.runners.model.InitializationError {
            super(clazz);
        }

        @Override
        public Object createTest() throws Exception {
            return super.createTest();
        }
    }
}
