package cn.revealing.howtose.controller;

import cn.revealing.howtose.async.EventModel;
import cn.revealing.howtose.async.EventProducer;
import cn.revealing.howtose.async.EventType;
import cn.revealing.howtose.model.Comment;
import cn.revealing.howtose.model.EntityType;
import cn.revealing.howtose.model.HostHolder;
import cn.revealing.howtose.model.LikeType;
import cn.revealing.howtose.services.CommentService;
import cn.revealing.howtose.services.LikeService;
import cn.revealing.howtose.util.HowtoseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by GJW on 2017/12/7.
 */

@Controller
public class LikeController {
    public static final Logger LOGGER = LoggerFactory.getLogger(LikeController.class);

    @Autowired
    HostHolder hostHolder;

    @Autowired
    LikeService likeService;

    @Autowired
    CommentService commentService;

    @Autowired
    EventProducer eventProducer;

    @RequestMapping( value = "/like", method = RequestMethod.POST)
    @ResponseBody
    public String like(@RequestParam("commentId") int commentId){
        if(hostHolder.getUser() == null) {
            return HowtoseUtil.getJsonString(999);
        }
        long likeCount = likeService.like(hostHolder.getUser().getId(), LikeType.ANSWER_LIKE, commentId);
        Comment comment = commentService.getCommentById(commentId);
        eventProducer.fireEvent(new EventModel(EventType.LIKE)
                .setActorId(hostHolder.getUser().getId())
                .setEntityType(EntityType.ENTITY_COMMENT)
                .setEntityId(commentId)
                .setEntityOwnerId(comment.getUserId())
                .setExt("questionId", String.valueOf(comment.getEntityId()))
        );
        return HowtoseUtil.getJsonString(0, String.valueOf(likeCount));
    }

    @RequestMapping( value = "/dislike", method = RequestMethod.POST)
    @ResponseBody
    public String dislike(@RequestParam("commentId") int commentId){
        if(hostHolder.getUser() == null) {
            return HowtoseUtil.getJsonString(999);
        }

        long likeCount = likeService.dislike(hostHolder.getUser().getId(), LikeType.ANSWER_LIKE, commentId);
        return HowtoseUtil.getJsonString(0, String.valueOf(likeCount));
    }
}
