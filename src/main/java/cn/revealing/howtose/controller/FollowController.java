package cn.revealing.howtose.controller;

import cn.revealing.howtose.async.EventModel;
import cn.revealing.howtose.async.EventProducer;
import cn.revealing.howtose.async.EventType;
import cn.revealing.howtose.model.*;
import cn.revealing.howtose.services.CommentService;
import cn.revealing.howtose.services.FollowService;
import cn.revealing.howtose.services.QuestionService;
import cn.revealing.howtose.services.UserService;
import cn.revealing.howtose.util.HowtoseUtil;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.parser.Entity;
import java.util.*;

/**
 * Created by GJW on 2017/11/26.
 */
@Controller
public class FollowController {
    public static final Logger LOGGER = LoggerFactory.getLogger(FollowController.class);

    @Autowired
    FollowService followService;
    @Autowired
    UserService userService;

    @Autowired
    QuestionService questionService;

    @Autowired
    EventProducer eventProducer;

    @Autowired
    CommentService commentService;

    @Autowired
    HostHolder hostHolder;
    @RequestMapping(value = "/followUser", method = RequestMethod.POST)
    @ResponseBody
    public String follow(@RequestParam("userId") int userId){
        if(hostHolder.getUser() == null) {
            return HowtoseUtil.getJsonString(999);
        }

        boolean ret = followService.follow(hostHolder.getUser().getId(), EntityType.ENTITY_USER, userId);
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(hostHolder.getUser().getId())
                .setEntityOwnerId(userId)
                .setEntityType(EntityType.ENTITY_USER)
                .setEntityId(userId));
        return HowtoseUtil.getJsonString(ret ? 0 : 1, String.valueOf(followService.getFolloweeCount(EntityType.ENTITY_USER, hostHolder.getUser().getId())));
    }

    @RequestMapping(value = "/unfollowUser", method = RequestMethod.POST)
    @ResponseBody
    public String unfollow(@RequestParam("userId") int userId){
        if(hostHolder.getUser() == null) {
            return HowtoseUtil.getJsonString(999);
        }

        boolean ret = followService.unfollow(hostHolder.getUser().getId(), EntityType.ENTITY_USER, userId);
        eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
                .setActorId(hostHolder.getUser().getId())
                .setEntityOwnerId(userId)
                .setEntityType(EntityType.ENTITY_USER)
                .setEntityId(userId));
        return HowtoseUtil.getJsonString(ret ? 0 : 1, String.valueOf(followService.getFolloweeCount(EntityType.ENTITY_USER, hostHolder.getUser().getId())));
    }

    @RequestMapping(value = "/followQuestion", method = RequestMethod.POST)
    @ResponseBody
    public String followQuestion(@RequestParam("questionId") int questionId){
        if(hostHolder.getUser() == null) {
            return HowtoseUtil.getJsonString(999);
        }
        Question q = questionService.getQuestion(questionId);
        if(q == null){
            return HowtoseUtil.getJsonString(1, "问题不存在");
        }
        boolean ret = followService.follow(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION, questionId);

        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(hostHolder.getUser().getId())
                .setEntityOwnerId(q.getUserId())
                .setEntityType(EntityType.ENTITY_QUESTION)
                .setEntityId(questionId));

        Map<String, Object> info = new HashMap<>();
        info.put("headUrl", hostHolder.getUser().getHeadUrl());
        info.put("name", hostHolder.getUser().getName());
        info.put("userId", hostHolder.getUser().getId());
        info.put("followCount", followService.getFollowerCount(EntityType.ENTITY_QUESTION, questionId));
        return HowtoseUtil.getJsonString(ret ? 0 : 1, info);
    }

    @RequestMapping(value = "/unfollowQuestion", method = RequestMethod.POST)
    @ResponseBody
    public String unfollowQuestion(@RequestParam("questionId") int questionId){
        if(hostHolder.getUser() == null) {
            return HowtoseUtil.getJsonString(999);
        }
        Question q = questionService.getQuestion(questionId);
        if(q == null){
            return HowtoseUtil.getJsonString(1, "问题不存在");
        }
        boolean ret = followService.unfollow(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION, questionId);
        eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
                .setActorId(hostHolder.getUser().getId())
                .setEntityOwnerId(q.getUserId())
                .setEntityType(EntityType.ENTITY_QUESTION)
                .setEntityId(questionId));
        Map<String, Object> info = new HashMap<>();
        info.put("headUrl", hostHolder.getUser().getHeadUrl());
        info.put("name", hostHolder.getUser().getName());
        info.put("userId", hostHolder.getUser().getId());
        info.put("followCount", followService.getFollowerCount(EntityType.ENTITY_QUESTION, questionId));
        return HowtoseUtil.getJsonString(ret ? 0 : 1, info);
    }


    @RequestMapping(value = "/user/{uid}/followees", method = RequestMethod.GET)
    public String followees(Model model, @PathVariable("uid") int uid){

        List<Integer> followeeIds = followService.getFollowee(EntityType.ENTITY_USER, uid, 0, 10);
        model.addAttribute("curUser", userService.getUser(uid));
        model.addAttribute("followeeCount", followService.getFolloweeCount(EntityType.ENTITY_USER, uid));
        if(hostHolder.getUser() != null) {
            model.addAttribute("followees",getUsersInfo(hostHolder.getUser().getId(), followeeIds));
        } else {
            model.addAttribute("followees",getUsersInfo(0, followeeIds));
        }
        return "followees";
    }

    @RequestMapping(value = "/user/{uid}/followers", method = RequestMethod.GET)
    public String followers(Model model, @PathVariable("uid") int uid){

        List<Integer> followerIds = followService.getFollower(EntityType.ENTITY_USER, uid, 0, 10);
        model.addAttribute("curUser", userService.getUser(uid));
        model.addAttribute("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, uid));
        if(hostHolder.getUser() != null) {
            model.addAttribute("followers",getUsersInfo(hostHolder.getUser().getId(), followerIds));
        } else {
            model.addAttribute("followers",getUsersInfo(0, followerIds));
        }
        return "followers";
    }

    private List<ViewObject> getUsersInfo(int localHostUser, List<Integer> userIds) {
        List<ViewObject> userInfos = new ArrayList<>();
        for(int userId : userIds) {
            User user = userService.getUser(userId);
            if (user == null ) continue;
            ViewObject vo = new ViewObject();
            vo.set("user", user);
            //vo.set("commentCount", commentService.getCommentCount())
            vo.set("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, userId));
            vo.set("followeeCount", followService.getFolloweeCount(EntityType.ENTITY_USER, userId));
            if(localHostUser != 0){
                vo.set("followed", followService.isFollower(localHostUser, EntityType.ENTITY_USER, userId));
            } else {
                vo.set("followed", false);
            }
            userInfos.add(vo);
        }
        return userInfos;
    }


}
