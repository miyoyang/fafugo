package com.lxh.fafugo.mapper;

import com.lxh.fafugo.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    @Insert("insert into user(NAME,ACCOUNT_ID,TOKEN,GMT_CREATE,GMT_MODIFIED) value(#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified})")
    void insert(User user);

}
