package cn.revealing.howtose.async.handler;

import cn.revealing.howtose.async.EventHandler;
import cn.revealing.howtose.async.EventModel;
import cn.revealing.howtose.async.EventType;
import cn.revealing.howtose.model.Message;
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
public class LikeHandler implements EventHandler{
    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;
    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        message.setFromId(HowtoseUtil.SYSTEM_USERID);
        message.setToId(model.getEntityOwnerId());
        message.setCreatedDate(new Date());
        message.setContent("用户" + userService.getUser(model.getActorId()).getName() + "赞了你的回答，"
        + "http://127.0.0.1:8080/question/" + model.getExt("questionId"));

        messageService.addMessage(message);

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
