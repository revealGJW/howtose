package cn.revealing.howtose.services;

import cn.revealing.howtose.dao.KeywordDAO;
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
    KeywordDAO keywordDAO;

    public List<Keyword> getKeywords(int start, int limit) {
        return keywordDAO.selectKeywords(start, limit);
    }

    public Keyword getWordByType(String word, int type) {
        return keywordDAO.selectByType(word, type);
    }

    public int addWord(String word, int type) {
        Keyword keyword = new Keyword();
        keyword.setAddDate(new Date());
        keyword.setWord(word);
        keyword.setType(type);
        keyword.setPriority(1);
        return keywordDAO.insert(keyword);
    }

    public int updatePriority(Keyword keyword) {
        keyword.setPriority(keyword.getPriority() + 1);
        return keywordDAO.updateByPrimaryKeySelective(keyword);
    }
}
