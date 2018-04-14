package cn.revealing.howtose.dao;

import cn.revealing.howtose.model.QuestionWord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface QuestionWordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(QuestionWord record);

    int insertSelective(QuestionWord record);

    QuestionWord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(QuestionWord record);

    int updateByPrimaryKey(QuestionWord record);
}