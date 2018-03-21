package cn.revealing.howtose.async.handler;

import cn.revealing.howtose.async.EventHandler;
import cn.revealing.howtose.async.EventModel;
import cn.revealing.howtose.async.EventType;
import cn.revealing.howtose.model.EntityType;
import cn.revealing.howtose.model.Message;
import cn.revealing.howtose.services.FollowService;
import cn.revealing.howtose.services.MessageService;
import cn.revealing.howtose.services.UserService;
import cn.revealing.howtose.util.HowtoseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by GJW on 2017/12/8.
 */

@Component
public class FollowHandler implements EventHandler{
    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Autowired
    FollowService followService;
    @Override
    public void doHandle(EventModel model) {
        if(model.getEventType() == EventType.FOLLOW) {
            Message message = new Message();
            message.setFromId(HowtoseUtil.SYSTEM_USERID);
            message.setToId(model.getEntityOwnerId());
            message.setCreatedDate(new Date());
            if (model.getEntityType() == EntityType.ENTITY_USER) {
                message.setContent("用户" + userService.getUser(model.getActorId()).getName() + "关注了你，"
                        + "http://127.0.0.1:8080/user/" + model.getActorId());
            } else if (model.getEntityType() == EntityType.ENTITY_QUESTION) {
                message.setContent("用户" + userService.getUser(model.getActorId()).getName() + "关注了你的问题 "
                        + "http://127.0.0.1:8080/question/" + model.getEntityId());
            }

            messageService.addMessage(message);
        }
        else {
            followService.unfollow(model.getActorId(), model.getEntityType(), model.getEntityId());
        }

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.FOLLOW, EventType.UNFOLLOW);
    }
}
