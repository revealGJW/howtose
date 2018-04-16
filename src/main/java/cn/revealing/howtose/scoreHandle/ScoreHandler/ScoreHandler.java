package cn.revealing.howtose.scoreHandle.ScoreHandler;

import cn.revealing.howtose.dao.ScoreDetailMapper;
import cn.revealing.howtose.services.CommentService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author guojiawei
 * @date 2018/4/15
 */
public interface ScoreHandler {
    int calculate(Document document, int id);

}
