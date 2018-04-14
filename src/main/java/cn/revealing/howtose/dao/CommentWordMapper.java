package cn.revealing.howtose.dao;

import cn.revealing.howtose.model.CommentWord;

public interface CommentWordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CommentWord record);

    int insertSelective(CommentWord record);

    CommentWord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CommentWord record);

    int updateByPrimaryKey(CommentWord record);
}