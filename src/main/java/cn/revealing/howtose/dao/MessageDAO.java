package cn.revealing.howtose.dao;

import cn.revealing.howtose.model.Comment;
import cn.revealing.howtose.model.Message;
import jdk.nashorn.internal.objects.annotations.Where;
import org.apache.ibatis.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by GJW on 2017/7/6.
 */
@Mapper
public interface MessageDAO {
    public static final Logger LOGGER = LoggerFactory.getLogger(MessageDAO.class);
    String TABLE_NAME = " message ";
    String INSERT_FIELDS = " from_id , to_id, content, has_read, created_date, conversation_id ";
    //String INSERT_FILEDS = " user_id, entity_type, entity_id, created_date, content, status ";
    String SELECT_FILEDS = "id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{fromId},#{toId},#{content},#{hasRead},#{createdDate},#{conversationId})"})
    int addMessage(Message message);


    List<Message> getConversitionDetail(@Param("conversationId") String  conversationId,
                                        @Param("offset") int offset, @Param("limit") int limit);

    @Select( { " select ", INSERT_FIELDS, " , count(id) as id from " +
            "(select * from ", TABLE_NAME, " where from_id=#{userId} or to_id=#{userId} order by created_date desc) tt " +
            "group by conversation_id order by created_date desc limit #{offset}, #{limit} "})
    List<Message> getConversitionList(@Param("userId") int  userId,
                                        @Param("offset") int offset, @Param("limit") int limit);

    @Select({" select count(id) from " + TABLE_NAME + " where entity_type = #{entityType} and entity_id = #{entityId} " } )
    int getCommentCount(@Param("entityId") int entityId, @Param("entityType") int entityType);

    @Select({ " select count(id) from ", TABLE_NAME, " where has_read=0 and to_id=#{userId} and conversation_id=#{conversationId}" })
    int getUnreadCount(@Param("userId") int userId, @Param("conversationId") String conversationId);

    @Update({"update ", TABLE_NAME, " set status = #{status} where id = #{id}"})
    int updateStatus(@Param("id") int id, @Param("status") int status);

}
