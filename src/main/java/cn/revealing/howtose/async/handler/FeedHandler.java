package cn.revealing.howtose.async.handler;

import cn.revealing.howtose.async.EventHandler;
import cn.revealing.howtose.async.EventModel;
import cn.revealing.howtose.async.EventType;
import cn.revealing.howtose.model.*;
import cn.revealing.howtose.services.*;
import cn.revealing.howtose.util.HowtoseUtil;
import cn.revealing.howtose.util.JedisAdapter;
import cn.revealing.howtose.util.RedisKeyUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by GJW on 2017/7/20.
 */
@Component
public class FeedHandler implements EventHandler {
    @Autowired
    QuestionService questionService;

    @Autowired
    UserService userService;

    @Autowired
    FeedService feedService;

    @Autowired
    FollowService followService;

    @Autowired
    JedisAdapter jedisAdapter;
    private String BuildFeedData(EventModel model) {
        Map<String, String> map = new HashMap<>();
        User user = userService.getUser(model.getActorId());
        map.put("userId", String.valueOf(user.getId()));
        map.put("userName", user.getName());
        map.put("userHead", user.getHeadUrl());

        if(model.getEventType() == EventType.COMMENT ||
                (model.getEventType() == EventType.FOLLOW) && model.getEntityType() == EntityType.ENTITY_QUESTION) {
            Question question = questionService.getQuestion(model.getEntityId());
            if(question == null) {
                return null;
            }
            map.put("questionId", String.valueOf(question.getId()));
            map.put("questionTitle", question.getTitle());
            map.put("questionCommentCount", String.valueOf(question.getCommentCount()));
            return JSONObject.toJSONString(map);
        }

        if(model.getEventType() == EventType.LIKE) {
            User commentUser = userService.getUser(model.getEntityOwnerId());
            map.put("commentUserName", commentUser.getName());
            Question question = questionService.getQuestion(Integer.parseInt(model.getExt("questionId")));
            if(question == null) {
                return null;
            }
            map.put("questionId", String.valueOf(question.getId()));
            map.put("questionTitle", question.getTitle());
            map.put("questionCommentCount", String.valueOf(question.getCommentCount()));
            return JSONObject.toJSONString(map);
        }
        return null;
    }
    @Override
    public void doHandle(EventModel model) {
        Feed feed = new Feed();
        feed.setUserId(model.getActorId());
        feed.setCreatedDate(new Date());
        feed.setType(model.getEventType().getValue());
        feed.setData(BuildFeedData(model));
        feedService.addFeed(feed);

        List<Integer> followers = followService.getFollower(EntityType.ENTITY_USER, model.getActorId(), Integer.MAX_VALUE);
        followers.add(0);
        for(Integer follower : followers) {
            String followerTimelineKey = RedisKeyUtil.getTimelineKey(follower);
            jedisAdapter.lpush(followerTimelineKey, String.valueOf(feed.getId()));
        }
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(new EventType[]{EventType.FOLLOW, EventType.COMMENT, EventType.LIKE});

    }
}
