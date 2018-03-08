package cn.revealing.howtose.services;

import cn.revealing.howtose.dao.QuestionDAO;
import cn.revealing.howtose.dao.UserDAO;
import cn.revealing.howtose.model.Question;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by GJW on 2017/5/17.
 */
@Service
public class QuestionService {

    public static final Logger LOGGER = LoggerFactory.getLogger(QuestionService.class);
    @Autowired
    QuestionDAO questionDAO;

    @Autowired
    SensitiveService sensitiveService;
    public List<Question> getLatestQuestions(int userId, int offset, int limit){
        return questionDAO.selectLatestQuestions(userId, offset, limit);
    }

    public int addQuestion(Question question){
        //敏感词过滤
        question.setContent(HtmlUtils.htmlEscape(question.getContent()));
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
        question.setTitle(sensitiveService.filter(question.getTitle()));
        question.setContent(sensitiveService.filter(question.getContent()));
        return questionDAO.addQuestion(question) > 0 ? question.getId() : 0;
    }

    public int updateCommentCount(int id, int count) {
        return questionDAO.updateCommentCount(id, count);
    }

    public  Question getQuestion(int qid){
        Question question = questionDAO.getQuestionById(qid);
        return question;
    }

}
