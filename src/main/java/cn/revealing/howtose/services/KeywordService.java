package cn.revealing.howtose.services;

import cn.revealing.howtose.dao.KeywordMapper;
import cn.revealing.howtose.model.Keyword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author guojiawei
 * @date 2018/4/11
 */
@Service
public class KeywordService {

    @Autowired
    KeywordMapper keywordMapper;

    public List<Keyword> getKeywords(int start, int limit) {
        return keywordMapper.selectKeywords(start, limit);
    }

    public Keyword getWordByType(String word, int type) {
        return keywordMapper.selectByType(word, type);
    }

    public int addWord(String word, int type) {
        Keyword keyword = new Keyword();
        keyword.setAddDate(new Date());
        keyword.setWord(word);
        keyword.setType(type);
        keyword.setPriority(1);
        return keywordMapper.insert(keyword);
    }

    public int updatePriority(Keyword keyword) {
        keyword.setPriority(keyword.getPriority() + 1);
        return keywordMapper.updateByPrimaryKeySelective(keyword);
    }

    public Keyword getExistWord(List<String> words) {
        return keywordMapper.selectExistWord(words);
    }


}
