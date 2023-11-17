package com.zph.programmer.springdemo.utils;

import org.junit.internal.runners.InitializationError;
import org.junit.runner.notification.RunNotifier;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.unitils.UnitilsJUnit4TestClassRunner;


public class TestRunner extends UnitilsJUnit4TestClassRunner {

    private final SpringRunner springRunner;

    public TestRunner(Class<?> clazz) throws org.junit.runners.model.InitializationError, InitializationError {
        super(clazz);
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
