package cn.revealing.howtose.dao;

import cn.revealing.howtose.model.Comment;
import cn.revealing.howtose.model.Feed;
import org.apache.ibatis.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by GJW on 2017/7/6.
 */
@Mapper
public interface FeedDAO {
    public static final Logger LOGGER = LoggerFactory.getLogger(FeedDAO.class);
    String TABLE_NAME = " feed ";
    String INSERT_FIELDS = " user_id, type, created_date, data ";
    //String INSERT_FILEDS = " user_id, entity_type, entity_id, created_date, content, status ";
    String SELECT_FILEDS = "id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{userId},#{type},#{createdDate},#{data})"})
    int addFeed(Feed feed);

    @Select({" select "+ SELECT_FILEDS + " from ", TABLE_NAME, " where id=#{id}"})
    public Feed getFeedById(@Param("id") int id);

    List<Feed> selectUserFeeds(@Param("maxId") int maxId,
                               @Param("userIds") List<Integer> userIds,
                               @Param("count") int count);
}
