package cn.revealing.howtose.async;

import cn.revealing.howtose.util.JedisAdapter;
import cn.revealing.howtose.util.RedisKeyUtil;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by GJW on 2017/7/8.
 */

@Service
public class EventProducer {

    public static final Logger LOGGER = LoggerFactory.getLogger(EventProducer.class);

    @Autowired
    JedisAdapter jedisAdapter;

    public boolean fireEvent(EventModel eventModel) {
        try {
            String json = JSONObject.toJSONString(eventModel);
            String key = RedisKeyUtil.getEventQueueKey();
            jedisAdapter.lpush(key, json);
            return true;
        } catch (Exception e) {
            LOGGER.error("添加事件队列失败：" + e.getMessage());
            return false;
        }
    }
}
