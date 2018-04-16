package cn.revealing.howtose.dao;

import cn.revealing.howtose.model.CommentWord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface CommentWordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CommentWord record);

    int insertSelective(CommentWord record);

    CommentWord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CommentWord record);

    int updateByPrimaryKey(CommentWord record);

    List<CommentWord> selectByWordID(@Param("wid") int wid, @Param("start") int start, @Param("limit") int limit);
}