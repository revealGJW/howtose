package cn.revealing.howtose.controller;

import cn.revealing.howtose.model.*;
import cn.revealing.howtose.services.*;
import cn.revealing.howtose.util.HowtoseUtil;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by GJW on 2017/11/26.
 */
@Controller
public class QuestionController {
    public static final Logger LOGGER = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    QuestionService questionService;

    @Autowired
    CommentService commentService;

    @Autowired
    LikeService likeService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    FollowService followService;
    @RequestMapping(path = "/question", method = RequestMethod.POST)
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title, @RequestParam("content") String content){
        try{
            Question question = new Question();
            question.setTitle(title);
            question.setContent(content);
            if(hostHolder.getUser() == null) {
                question.setUserId(HowtoseUtil.ANONYMOUS_USERID);
            }
            else {
                question.setUserId(hostHolder.getUser().getId());
            }

            question.setCreatedDate(new Date());
            question.setCommentCount(0);
            int questionId;
            if (( questionId = questionService.addQuestion(question)) > 0) {
                int count = commentService.getCommentCount(questionId, EntityType.ENTITY_QUESTION);
                System.out.println(count);
                questionService.updateCommentCount(questionId, count);
                return HowtoseUtil.getJsonString(0);
            }
        }catch (Exception e){
            LOGGER.error(" 增加题目失败：", e);
        }
        return HowtoseUtil.getJsonString(1,"失败");
    }

    @RequestMapping(value = "/question/{qid}", method = RequestMethod.GET)
    public String  getQuestion(Model model, @PathVariable("qid") int qid) {
        Question question = questionService.getQuestion(qid);
        model.addAttribute("question", question);
        model.addAttribute("user", userService.getUser(question.getUserId()));
        List<Integer> usersId = followService.getFollower(EntityType.ENTITY_QUESTION, question.getId(), 999);
        List<User> followUsers = new ArrayList<>();
        for(Integer id : usersId) {
            followUsers.add(userService.getUser(id));
        }
        model.addAttribute("followUsers", followUsers);
        model.addAttribute("followed", followService.isFollower(hostHolder.getUser() == null ? 0 :hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION, qid));
        List<Comment> commentList = commentService.getCommentsByEntity(qid, EntityType.ENTITY_QUESTION, 0, 10);
        List<ViewObject> comments = new ArrayList<ViewObject>();
        for(Comment comment : commentList) {
            ViewObject vo = new ViewObject();
            vo.set("comment", comment);
            vo.set("user", userService.getUser(comment.getUserId()));
            if(hostHolder.getUser() == null) {
                vo.set("liked", 0);
            } else {
                vo.set("liked", likeService.getLikeStatus(hostHolder.getUser().getId(), LikeType.ANSWER_LIKE, comment.getId()));
            }
            vo.set("likeCount", likeService.getLikeCount(LikeType.ANSWER_LIKE, comment.getId()));
            comments.add(vo);
        }
        model.addAttribute("comments",comments);
        return "detail";
    }

}
