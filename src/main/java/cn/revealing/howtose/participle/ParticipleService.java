package cn.revealing.howtose.participle;

import cn.revealing.howtose.dao.QuestionWordMapper;
import cn.revealing.howtose.model.Question;
import cn.revealing.howtose.services.KeywordService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by GJW on 2018/4/13.
 */
public class ParticipleService {
    @Autowired
    KeywordService keywordService;

    @Autowired
    QuestionWordMapper questionWordMapper;

    public void addQuestionWord(Question question) {
        int qid = question.getId();
        
    }
}
