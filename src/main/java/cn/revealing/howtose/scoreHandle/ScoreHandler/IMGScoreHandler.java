package cn.revealing.howtose.scoreHandle.ScoreHandler;

import cn.revealing.howtose.dao.ScoreDetailMapper;
import cn.revealing.howtose.model.ScoreDetail;
import cn.revealing.howtose.scoreHandle.ScoreItem;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author guojiawei
 * @date 2018/4/16
 */
@Component
public class IMGScoreHandler implements ScoreHandler {
    private static final int TYPE = ScoreItem.IMG.getType();
    private static final int BASE_SCORE = ScoreItem.IMG.getBaseScore();

    @Autowired
    ScoreDetailMapper scoreDetailMapper;

    @Override
    public int calculate(Document document, int id) {
        int num = document.select("img").size();
        int score = BASE_SCORE * num;
        ScoreDetail scoreDetail = new ScoreDetail();
        scoreDetail.setCommentId(id);
        scoreDetail.setScoreType(TYPE);
        scoreDetail.setSocre(score);
        scoreDetailMapper.insertSelective(scoreDetail);
        return score;
    }
}
