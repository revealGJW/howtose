package cn.revealing.howtose.dao;

import cn.revealing.howtose.model.ScoreDetail;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ScoreDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ScoreDetail record);

    int insertSelective(ScoreDetail record);

    ScoreDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ScoreDetail record);

    int updateByPrimaryKey(ScoreDetail record);
}