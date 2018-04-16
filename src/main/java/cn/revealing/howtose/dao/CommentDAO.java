package cn.revealing.howtose.dao;

import cn.revealing.howtose.model.Comment;
import cn.revealing.howtose.model.Question;
import org.apache.ibatis.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by GJW on 2017/12/6.
 */
@Mapper
public interface CommentDAO {
    public static final Logger LOGGER = LoggerFactory.getLogger(CommentDAO.class);
    String TABLE_NAME = " comment ";
    String INSERT_FIELDS = " user_id, content, created_date, entity_id, entity_type, status, score ";
    String SELECT_FILEDS = "id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{userId},#{content},#{createdDate},#{entityId},#{entityType},#{status}, #{score})"})
    int addComment(Comment comment);
    /*@Insert( {" insert into ", TABLE_NAME ,"(", INSERT_FILEDS,
            " ) values (#{userId}, #{entityType}, #{entityId}, #{createdDate}, #{content}, #{status})"})
    int addComment(Comment comment);*/


    List<Comment> selectCommentByEntity(@Param("entityId") int entityId, @Param("entityType") int entityType,
                                         @Param("offset") int offset, @Param("limit") int limit);

    @Select({" select count(id) from " + TABLE_NAME + " where entity_type = #{entityType} and entity_id = #{entityId} " } )
    int getCommentCount(@Param("entityId") int entityId, @Param("entityType") int entityType);

    @Update({"update ", TABLE_NAME, " set status = #{status} where id = #{id}"})
    int updateStatus(@Param("id") int id, @Param("status") int status);

    @Select({" select "+ SELECT_FILEDS + " from ", TABLE_NAME, " where id=#{id}"})
    public Comment getCommentById(@Param("id") int id);


    @Select({" select count(id) from " + TABLE_NAME + " where user_id=#{userId} " } )
    int getUserCommentCount(@Param("userId") int userId);


    List<Comment> getLatestComment(@Param("offset") int offset, @Param("limit") int limit);

    List<Comment> selectByIds(@Param("ids") List<Integer> ids);

    void addComments(@Param("comments") List<Comment> comments);

    @Update({"update ", TABLE_NAME, " set score = #{score} where id = #{id}"})
    int updateScore(@Param("id") int id, @Param("score") int score);

    List<Comment> getNoScoreComment(@Param("limit") int limit);
}
