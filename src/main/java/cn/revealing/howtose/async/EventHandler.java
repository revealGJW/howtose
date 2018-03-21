package cn.revealing.howtose.async;

import java.util.List;

/**
 * Created by GJW on 2017/12/8.
 */
public interface EventHandler {
    void doHandle(EventModel model);
    List<EventType> getSupportEventTypes();
}
