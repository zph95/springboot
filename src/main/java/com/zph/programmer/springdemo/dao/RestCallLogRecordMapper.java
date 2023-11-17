package com.zph.programmer.springdemo.dao;

import com.zph.programmer.springdemo.po.RestCallLogRecord;

public interface RestCallLogRecordMapper {
    int insert(RestCallLogRecord record);

    RestCallLogRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(RestCallLogRecord record);
}