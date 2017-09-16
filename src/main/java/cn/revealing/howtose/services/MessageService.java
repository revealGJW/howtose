package cn.revealing.howtose.services;

import cn.revealing.howtose.dao.CommentDAO;
import cn.revealing.howtose.dao.MessageDAO;
import cn.revealing.howtose.model.Comment;
import cn.revealing.howtose.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * Created by GJW on 2017/5/17.
 */
@Service
public class MessageService {

    public static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);
    @Autowired
    MessageDAO messageDAO;

    @Autowired
    SensitiveService sensitiveService;

    public List<Message> getConversitionDetail(String conversationId, int offset, int limit) {
        return messageDAO.getConversitionDetail(conversationId, offset, limit);
    }

    public List<Message> getConversitionlist(int userId, int offset, int limit) {
        return messageDAO.getConversitionList(userId, offset, limit);
    }

    public int addMessage(Message message) {
        //敏感词过滤
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        message.setContent(sensitiveService.filter(message.getContent()));
        return messageDAO.addMessage(message) > 0 ? message.getId() : 0;
    }

    /*public int getCommentCount(int entityId, int entityType) {
        return commentDAO.getCommentCount(entityId, entityType);
    }

    public boolean deleteComment(int id){
        return commentDAO.updateStatus( id, 1) > 0;
    }*/

    public int getUnreadCount(int userId, String conversationId){
        return messageDAO.getUnreadCount(userId, conversationId);
    }

}
