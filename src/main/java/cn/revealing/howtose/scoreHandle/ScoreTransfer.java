package cn.revealing.howtose.scoreHandle;

import cn.revealing.howtose.dao.ScoreDetailMapper;
import cn.revealing.howtose.model.Comment;
import cn.revealing.howtose.scoreHandle.ScoreHandler.ScoreHandler;
import cn.revealing.howtose.services.CommentService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author guojiawei
 * @date 2018/4/15
 */
@Service
public class ScoreTransfer implements InitializingBean, ApplicationContextAware{

    private ApplicationContext applicationContext;

    private List<ScoreHandler> handlers = new ArrayList<>();

    @Autowired
    CommentService commentService;

    @Autowired
    ScoreDetailMapper scoreDetailMapper;

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, ScoreHandler> beans = applicationContext.getBeansOfType(ScoreHandler.class);
        if (beans != null) {
            for (Map.Entry<String, ScoreHandler> entry : beans.entrySet()) {
                handlers.add(entry.getValue());
            }
        }
        try {
            ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
            service.scheduleAtFixedRate(() -> {
                List<Comment> comments = commentService.getNoScoreComment(100);
                for (Comment c : comments) {
                    transfer(c);
                }
            }, 0, 5, TimeUnit.MINUTES);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }




    public void transfer(Comment comment) {
        int score = 0;
        String html = comment.getContent();
        Document doc = Jsoup.parse(html);
        for(ScoreHandler handler : handlers) {
            score += handler.calculate(doc, comment.getId());
        }
        commentService.updateScore(comment.getId(), score);
    }
}
