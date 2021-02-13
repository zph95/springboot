package com.zph.programmer.springboot.utils;


import lombok.extern.slf4j.Slf4j;

import java.util.Map;


@Slf4j
public class DaoProviderUtil {

    public String findSelectColumnByTable(Map<String, Object> map) {
        if (map != null && map.containsKey(DaoParamNameEnum.COL_NAME.getName()) && map.containsKey(DaoParamNameEnum.TABLE_NAME.getName())) {
            StringBuilder sql = new StringBuilder();
            sql.append("select ");
            sql.append(map.get(DaoParamNameEnum.COL_NAME.getName()));
            sql.append(" from ");
            sql.append(map.get(DaoParamNameEnum.TABLE_NAME.getName()));
            return sql.toString();
        } else {
            return null;
        }
    }

    /**
     * 拼接sql 在dao层，mapper使用
     * import org.apache.ibatis.annotations.SelectProvider
     *
     * @param map
     * @return
     * @SelectProvider(type = DaoProviderUtil.class, method = "selectColumnByTableWhere")
     * List<Map<String, Object>> getSelectValueByMapWhere(Map<String, Object> map);
     */
    public String selectColumnByTableWhere(Map<String, Object> map) {
        if (map != null && map.containsKey(DaoParamNameEnum.COL_NAME.getName())
                && map.containsKey(DaoParamNameEnum.TABLE_NAME.getName())) {
            StringBuilder sql = new StringBuilder();
            sql.append("select ");
            sql.append(map.get(DaoParamNameEnum.COL_NAME.getName()));
            sql.append(" from ");
            sql.append(map.get(DaoParamNameEnum.TABLE_NAME.getName()));
            if (map.containsKey(DaoParamNameEnum.WHERE_CONDITION.getName())) {
                sql.append(" where ");
                sql.append(map.get(DaoParamNameEnum.WHERE_CONDITION.getName()));
            }
            log.info("finance Dao:{}", sql.toString());
            return sql.toString();
        } else {
            return null;
        }
    }

    public enum DaoParamNameEnum {

        /**
         * 枚举
         */
        COL_NAME("col_name"),
        TABLE_NAME("table_name"),
        WHERE_CONDITION("where_condition"),
        ;

        /**
         * 内容
         */
        private final String name;

        /**
         * init
         *
         * @param name 内容
         */
        DaoParamNameEnum(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }


    }


}
