package cn.revealing.howtose.participle;

import cn.revealing.howtose.dao.CommentWordMapper;
import cn.revealing.howtose.dao.QuestionWordMapper;
import cn.revealing.howtose.model.*;
import cn.revealing.howtose.services.CommentService;
import cn.revealing.howtose.services.KeywordService;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by GJW on 2018/4/13.
 */
@Service
public class ParticipleService {
    @Autowired
    KeywordService keywordService;

    @Autowired
    QuestionWordMapper questionWordMapper;

    @Autowired
    CommentWordMapper commentWordMapper;

    @Autowired
    CommentService commentService;

    public void addQuestionWord(Question question) {
        int qid = question.getId();
        List<Term> keywords = HanLP.segment(question.getContent());
        List<String> searchWords = new ArrayList<>();
        for (Term t : keywords) {
            searchWords.add(t.word);
        }
        System.out.println(keywords);
        Keyword aliveWord = keywordService.getExistWord(searchWords);

        if(aliveWord == null) {
            //TODO
            Comment comment = new Comment();
            comment.setContent("该问题暂时没有合适的答案");
            commentService.addComment(comment);
        }
        int wid = aliveWord.getId();
        keywordService.updatePriority(aliveWord);

        QuestionWord qw = new QuestionWord();
        qw.setAddDate(new Date());
        qw.setQuestionId(qid);
        qw.setWordId(wid);
        questionWordMapper.insertSelective(qw);
        configComment(question, wid);
    }

    private void configComment(Question question, int wid) {
        List<CommentWord> answers = commentWordMapper.selectByWordID(wid, 0, 10);
        List<Integer> cids = new ArrayList<>();
        for(CommentWord commentWord : answers) {
            cids.add(commentWord.getCommentId());
        }
        List<Comment> comments = commentService.getCommentByIds(cids);
        List<Comment> relatedComments = new ArrayList<>();
        for(Comment c : comments) {
            Comment comment = new Comment();
            comment.setContent(c.getContent());
            comment.setUserId(2);
            comment.setEntityType(EntityType.ENTITY_QUESTION);
            comment.setEntityId(question.getId());
            comment.setCreatedDate(new Date());
            relatedComments.add(comment);
        }
        commentService.batchAddComment(relatedComments);
    }


}
