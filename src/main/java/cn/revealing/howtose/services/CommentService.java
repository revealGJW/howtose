package cn.revealing.howtose.services;

import cn.revealing.howtose.dao.CommentDAO;
import cn.revealing.howtose.model.Comment;
import cn.revealing.howtose.model.Question;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
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
public class CommentService {

    public static final Logger LOGGER = LoggerFactory.getLogger(CommentService.class);
    @Autowired
    CommentDAO commentDAO;

    @Autowired
    SensitiveService sensitiveService;

    public List<Comment> getCommentsByEntity(int entityId, int entityType, int offset, int limit) {
        return commentDAO.selectCommentByEntity(entityId, entityType, offset, limit);
    }

    public int addComment(Comment comment) {
        //敏感词过滤
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveService.filter(comment.getContent()));
        return commentDAO.addComment(comment) > 0 ? comment.getId() : 0;
    }

    public int getCommentCount(int entityId, int entityType) {
        return commentDAO.getCommentCount(entityId, entityType);
    }

    public Comment getCommentById(int id) {
        return commentDAO.getCommentById(id);
    }

    public boolean deleteComment(int id){
        return commentDAO.updateStatus( id, 1) > 0;
    }

    public int getUserCommentCount(int userId) {
        return commentDAO.getUserCommentCount(userId);
    }

}
