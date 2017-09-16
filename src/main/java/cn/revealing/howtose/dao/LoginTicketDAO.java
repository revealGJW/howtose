package cn.revealing.howtose.dao;

import cn.revealing.howtose.controller.LoginController;
import cn.revealing.howtose.model.LoginTicket;
import org.apache.ibatis.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by GJW on 2017/6/18.
 */
@Mapper
public interface LoginTicketDAO {
    public static final Logger LOGGER = LoggerFactory.getLogger(LoginTicketDAO.class);
    String TABLE_NAME = " login_ticket ";
    String INSERT_FILEDS = " user_id , ticket , expired , status ";
    String SELECT_FILEDS = "id, " + INSERT_FILEDS;
    @Insert( {" insert into ", TABLE_NAME ,"(", INSERT_FILEDS,
            " ) values (#{userId}, #{ticket}, #{expired}, #{status})"})
    int addLoginTicket(LoginTicket loginTicket);

    @Select({"select ",SELECT_FILEDS, "from ", TABLE_NAME, "where ticket = #{ticket}"})
    LoginTicket selectByTicket(String ticket);

    @Update({"update ", TABLE_NAME, " set status = #{status} where ticket = #{ticket}"})
    void updateStatus(@Param("status") int status, @Param("ticket") String ticket);

}
