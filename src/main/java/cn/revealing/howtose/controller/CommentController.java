package cn.revealing.howtose.controller;

import cn.revealing.howtose.async.EventModel;
import cn.revealing.howtose.async.EventProducer;
import cn.revealing.howtose.async.EventType;
import cn.revealing.howtose.model.Comment;
import cn.revealing.howtose.model.EntityType;
import cn.revealing.howtose.model.HostHolder;
import cn.revealing.howtose.model.Question;
import cn.revealing.howtose.services.CommentService;
import cn.revealing.howtose.services.QuestionService;
import cn.revealing.howtose.services.UserService;
import cn.revealing.howtose.util.HowtoseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * Created by GJW on 2017/6/26.
 */
@Controller
public class CommentController {
    public static final Logger LOGGER = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    CommentService commentService;

    @Autowired
    EventProducer eventProducer;
    @Autowired
    UserService userService;

    @Autowired
    QuestionService questionService;

    @Autowired
    HostHolder hostHolder;
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public String addQuestion(@RequestParam("content") String content, @RequestParam("questionId") int questionId){
        try{
            Comment comment = new Comment();
            comment.setContent(content);
            if(hostHolder.getUser() == null) {
                comment.setUserId(HowtoseUtil.ANONYMOUS_USERID);
            }
            else {
                comment.setUserId(hostHolder.getUser().getId());
            }
            comment.setEntityType(EntityType.ENTITY_QUESTION);
            comment.setEntityId(questionId);
            comment.setCreatedDate(new Date());

            commentService.addComment(comment);

            int count = commentService.getCommentCount(EntityType.ENTITY_QUESTION, questionId);
            questionService.updateCommentCount(questionId, count);

            eventProducer.fireEvent(new EventModel(EventType.COMMENT)
                    .setActorId(comment.getUserId())
                    .setEntityOwnerId(comment.getUserId())
                    .setEntityType(comment.getEntityType())
                    .setEntityId(comment.getEntityId()));
            /*if (commentService.addComment(comment) > 0) {
                return HowtoseUtil.getJsonString(0);
            }*/
        }catch (Exception e){
            LOGGER.error(" 增加评论失败：", e.getMessage());
        }
        return "redirect:/question/" + String.valueOf(questionId);
    }

}
