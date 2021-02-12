package com.zph.programmer.springboot.dao;

import com.zph.programmer.springboot.po.RestCallLogRecord;

public interface RestCallLogRecordMapper {
    int insert(RestCallLogRecord record);

    RestCallLogRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(RestCallLogRecord record);
}