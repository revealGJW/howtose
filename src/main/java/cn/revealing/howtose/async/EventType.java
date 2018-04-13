package cn.revealing.howtose.async;

/**
 * Created by GJW on 2017/7/8.
 */
public enum EventType {
    LIKE(0),
    COMMENT(1),
    LOGIN(2),
    MAIL(3),
    FOLLOW(4),
    UNFOLLOW(5),
    REGISTER(6);

    private int value;
    EventType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
