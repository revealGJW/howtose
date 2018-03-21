package cn.revealing.howtose.dao;

import cn.revealing.howtose.model.Question;
import org.apache.ibatis.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by GJW on 2017/11/16.
 */
@Mapper
public interface QuestionDAO {
    public static final Logger LOGGER = LoggerFactory.getLogger(QuestionDAO.class);
    String TABLE_NAME = " question ";
    String INSERT_FILEDS = " title, content, user_id, created_date, comment_count ";
    String SELECT_FILEDS = "id, " + INSERT_FILEDS;
    @Insert( {" insert into ", TABLE_NAME ,"(", INSERT_FILEDS,
            " ) values (#{title}, #{content}, #{userId}, #{createdDate}, #{commentCount})"})
    int addQuestion(Question question);


    List<Question> selectLatestQuestions(@Param("userId") int userId, @Param("offset") int offset,
                                         @Param("limit") int limit);

    List<Question> selectHotQuestions(@Param("offset") int offset,
                                         @Param("limit") int limit);

    @Update({"update ", TABLE_NAME, " set title = #{title} where id = #{id}"})
    void updateTitle(Question question);

    @Update({"update ", TABLE_NAME, " set comment_count=#{count} where id=#{id}"})
    int updateCommentCount(@Param("id") int id, @Param("count") int count);

    @Select({ " select " + SELECT_FILEDS + " from " + TABLE_NAME + " where id = #{pid} " })
    Question getQuestionById(int qid);
}
