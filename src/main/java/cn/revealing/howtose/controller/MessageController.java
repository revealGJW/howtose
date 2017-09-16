package cn.revealing.howtose.controller;

import cn.revealing.howtose.model.*;
import cn.revealing.howtose.services.CommentService;
import cn.revealing.howtose.services.MessageService;
import cn.revealing.howtose.services.UserService;
import cn.revealing.howtose.util.HowtoseUtil;
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
 * Created by GJW on 2017/6/26.
 */
@Controller
public class MessageController {
    public static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    @RequestMapping(value = "/msg/detail", method = RequestMethod.GET)
    public String getConversationDetail(Model model, @RequestParam("conversationId") String conversationId) {
        try {
            List<Message> messageList = messageService.getConversitionDetail(conversationId, 0, 10);
            List<ViewObject> messages = new ArrayList<ViewObject>();
            for (Message message : messageList) {
                ViewObject vo = new ViewObject();
                vo.set("message", message);
                vo.set("user", userService.getUser(message.getFromId()));
                messages.add(vo);
            }
            model.addAttribute("messages", messages);

        } catch (Exception e) {
            LOGGER.error("获取消息失败：", e.getMessage());
        }
        return "letterDetail";
    }

    @RequestMapping(value = "/msg/list", method = RequestMethod.GET)
    public String getConversationList(Model model) {
        if(hostHolder.getUser() == null){
            return "redirect:/reglogin";
        }
        int localUserId = hostHolder.getUser().getId();
        List<Message> messageList =  messageService.getConversitionlist(localUserId, 0, 10);
        List<ViewObject> conversations = new ArrayList<ViewObject>();
        for(Message message : messageList) {
            ViewObject vo = new ViewObject();
            vo.set("conversation", message);
            vo.set("unread", messageService.getUnreadCount(localUserId, message.getconversationId()));
            int targetId = message.getFromId() == localUserId ? message.getToId() : message.getFromId();
            vo.set("user", userService.getUser(targetId));
            conversations.add(vo);
        }
        model.addAttribute("conversations", conversations);
        return "letter";
    }


    @RequestMapping(value = "/msg", method = {RequestMethod.POST})
    @ResponseBody
    public String addMessage(@RequestParam("toName") String toName,
                             @RequestParam("content") String content) {
        try {

            if (hostHolder.getUser() == null) {
                return HowtoseUtil.getJsonString(999, "未登录");
            }
            User user = userService.selectByName(toName);
            if (user == null) {
                return HowtoseUtil.getJsonString(1, "用户不存在");
            }
            Message message = new Message();
            message.setContent(content);
            message.setFromId(hostHolder.getUser().getId());
            message.setToId(user.getId());
            message.setCreatedDate(new Date());
            message.setconversationId(message.getconversationId());
            messageService.addMessage(message);
            /*if (commentService.addComment(comment) > 0) {
                return HowtoseUtil.getJsonString(0);
            }*/
            return HowtoseUtil.getJsonString(0);

        } catch (Exception e) {
            LOGGER.error(" 发送消息失败：", e.getMessage());
            return HowtoseUtil.getJsonString(1, "发送消息失败");
        }
    }

}
