package com.zph.programmer.springboot.testconf;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;

@Slf4j
@ComponentScan(basePackages = "com.zph.programmer.springboot"
        , excludeFilters = {
        @ComponentScan.Filter(type = FilterType.CUSTOM, classes = TestApplication.TypeExFilter.class)
})
public class TestApplication {
    /**
     * 避免以下的Bean被实例化
     */
    public static class TypeExFilter implements TypeFilter {
        @Override
        public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
            String n = metadataReader.getClassMetadata().getClassName();
           /* if (n.equals(DataSourceConf.class.getName())) {
                return true;
            }
            */
            return false;

        }
    }
}
