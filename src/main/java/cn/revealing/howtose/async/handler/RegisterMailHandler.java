package cn.revealing.howtose.async.handler;

import cn.revealing.howtose.async.EventHandler;
import cn.revealing.howtose.async.EventModel;
import cn.revealing.howtose.async.EventType;
import cn.revealing.howtose.util.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by GJW on 2018/4/2.
 */
@Component
public class RegisterMailHandler implements EventHandler {
    @Autowired
    MailSender mailSender;

    @Override
    public void doHandle(EventModel model) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("username", model.getExt("username"));
        map.put("code", model.getExt("code"));
        mailSender.sendWithHTMLTemplate(model.getExt("email"), "注册邮箱验证", "mails/register.html", map);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.REGISTER);
    }
}
