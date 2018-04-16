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
public class LengthScoreHandler implements ScoreHandler{
    private static final int TYPE = ScoreItem.LENGTH.getType();
    private static final int BASE_SCORE = ScoreItem.LENGTH.getBaseScore();
    private static final int BASE_LENGTH = 50;

    @Autowired
    ScoreDetailMapper scoreDetailMapper;

    @Override
    public int calculate(Document document, int id) {
        int length = document.text().length();
        int score = (length / BASE_LENGTH) * BASE_SCORE;
        ScoreDetail scoreDetail = new ScoreDetail();
        scoreDetail.setCommentId(id);
        scoreDetail.setScoreType(TYPE);
        scoreDetail.setSocre(score);
        scoreDetailMapper.insertSelective(scoreDetail);
        return score;
    }
}
