package cn.revealing.howtose.dao;

import cn.revealing.howtose.model.Keyword;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
public interface KeywordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Keyword record);

    int insertSelective(Keyword record);

    Keyword selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Keyword record);

    int updateByPrimaryKey(Keyword record);

    List<Keyword> selectKeywords(@Param("start") int start,@Param("limit") int limit);

    Keyword selectByType(@Param("word") String word, @Param("type") int type);

    Keyword selectExistWord(@Param("words") List<String> words);
}