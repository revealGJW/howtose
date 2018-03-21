package cn.revealing.howtose.dao;

import cn.revealing.howtose.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by GJW on 2017/11/16.
 */

@Mapper
public interface UserDAO {

    public static final Logger LOGGER = LoggerFactory.getLogger(UserDAO.class);
    String TABLE_NAME = " user ";
    String INSERT_FILEDS = " name, password, salt, head_url ";
    String SELECT_FILEDS = "id, " + INSERT_FILEDS;
    @Insert( {" insert into ", TABLE_NAME ,"(", INSERT_FILEDS,
            " ) values (#{name}, #{password}, #{salt}, #{headUrl})"})
    int addUser(User user);

    @Select({"select ",SELECT_FILEDS, "from ", TABLE_NAME, "where id = #{id}"})
    User selectById(int id);

    @Select({"select ",SELECT_FILEDS, "from ", TABLE_NAME, "where name = #{name}"})
    User selectByName(String name);

    @Update({"update ", TABLE_NAME, " set password = #{password} where id = #{id}"})
    void updatePassword(User user);

    @Update({"update ", TABLE_NAME, " set name = #{name} where id = #{id}"})
    void updateName(User user);

    @Update({"update ", TABLE_NAME, " set head_url = #{headUrl} where id = #{id}"})
    void updateHeadUrl(User user);
}
