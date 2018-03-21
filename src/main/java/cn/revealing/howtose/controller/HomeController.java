package cn.revealing.howtose.controller;

import cn.revealing.howtose.model.*;
import cn.revealing.howtose.services.CommentService;
import cn.revealing.howtose.services.FollowService;
import cn.revealing.howtose.services.QuestionService;
import cn.revealing.howtose.services.UserService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.slf4j.Logger;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by GJW on 2017/11/17.
 */
@Controller
public class HomeController {
    public static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    QuestionService questionService;
    @Autowired
    UserService userService;
    @Autowired
    CommentService commentService;

    @Autowired
    FollowService followService;

    @Autowired
    HostHolder hostHolder;

    @RequestMapping({"/user/{userId}"})
    public String userIndex(Model model, @PathVariable("userId") int userId) {
        model.addAttribute("vos", getQuestions(userId, 0, 10));

        User user = userService.getUser(userId);
        ViewObject vo = new ViewObject();
        vo.set("user", user);
        vo.set("commentCount", commentService.getUserCommentCount(userId));
        vo.set("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, userId));
        vo.set("followeeCount", followService.getFolloweeCount(EntityType.ENTITY_USER, userId));
        if (hostHolder.getUser() != null) {
            vo.set("followed", followService.isFollower(hostHolder.getUser().getId(), EntityType.ENTITY_USER, userId));
        } else {
            vo.set("followed", false);
        }
        model.addAttribute("profileUser", vo);
        return "profile";
    }

    @RequestMapping(path = {"/", "/index"})
    public String index(Model model, HttpSession httpSession) {
        model.addAttribute("vos", getQuestions(0, 0, 10));
        return "index";
    }

    @RequestMapping({"/getquestionByPage"})
    @ResponseBody
    public String index(@RequestParam("page") String page, HttpSession httpSession) {
        Map<String, Object> map = new HashMap<>();
        int pageNum = Integer.parseInt(page);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rtnCode", 200);
        jsonObject.put("bizData", getQuestions(0, (pageNum > 0 ? pageNum - 1 : 0) * 10, 10));
        return jsonObject.toJSONString();
    }

    private List<ViewObject> getQuestions(int userId, int offset, int limit){
        List<ViewObject> vos = new ArrayList<ViewObject>();
        List<Question> questions = questionService.getLatestQuestions(userId, offset, limit);
        for (Question question : questions) {
            ViewObject vo = new ViewObject();
            vo.set("question", question);
            vo.set("user", userService.getUser(question.getUserId()));
            vo.set("followCount", followService.getFollowerCount(EntityType.ENTITY_QUESTION, question.getId()));
            vos.add(vo);
        }
        return vos;
    }

    private List<ViewObject> getHotQuestions(int offset, int limit){
        List<ViewObject> vos = new ArrayList<ViewObject>();
        List<Question> questions = questionService.getHotQuestions(offset, limit);
        for (Question question : questions) {
            ViewObject vo = new ViewObject();
            vo.set("question", question);
            vo.set("user", userService.getUser(question.getUserId()));
            vo.set("followCount", followService.getFollowerCount(EntityType.ENTITY_QUESTION, question.getId()));
            vos.add(vo);
        }
        return vos;
    }

    @RequestMapping({"/hot"})
    public String getHot(Model model) {
        model.addAttribute("vos", getHotQuestions(0, 10));
        return "hot";
    }
}