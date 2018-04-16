package cn.revealing.howtose.scoreHandle;

/**
 * @author guojiawei
 * @date 2018/4/15
 */

public enum ScoreItem {

    LENGTH(1, 10),

    IMG(2, 20);

    private int type;

    private int baseScore;

    ScoreItem(int type, int baseScore) {
        this.type = type;
        this.baseScore = baseScore;
    }

    public int getType() {
        return type;
    }

    public int getBaseScore() {
        return baseScore;
    }
}
