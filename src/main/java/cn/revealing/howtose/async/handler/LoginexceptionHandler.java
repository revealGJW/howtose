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
 * Created by GJW on 2017/12/9.
 */
@Component
public class LoginexceptionHandler implements EventHandler {
    @Autowired
    MailSender mailSender;

    @Override
    public void doHandle(EventModel model) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("username", model.getExt("username"));
        //mailSender.sendWithHTMLTemplate(model.getExt("email"), "登录异常", "mails/login_exception.html", map);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}
