package cn.revealing.howtose.async;


import cn.revealing.howtose.util.JedisAdapter;
import cn.revealing.howtose.util.RedisKeyUtil;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by GJW on 2017/12/8.
 */
@Service
public class EventConsumer implements InitializingBean, ApplicationContextAware{
    public static final Logger LOGGER = LoggerFactory.getLogger(EventConsumer.class);
    @Autowired
    JedisAdapter jedisAdapter;

    Map<EventType, List<EventHandler>> config = new HashMap<EventType, List<EventHandler>>();
    private ApplicationContext applicationContext;

    public static ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        if(beans != null) {
            for(Map.Entry<String, EventHandler> entry : beans.entrySet()) {
                List<EventType> types = entry.getValue().getSupportEventTypes();
                for(EventType type : types) {
                    if(!config.containsKey(type)) {
                        config.put(type, new ArrayList<EventHandler>());
                    }
                    config.get(type).add(entry.getValue());
                }
            }
        }

        executorService.execute( new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    String key = RedisKeyUtil.getEventQueueKey();
                    List<String> events = jedisAdapter.brpop(0, key);
                    if(events == null) {
                        continue;
                    }
                    for (String event : events) {
                        if(event.equals(key)) continue;
                        EventModel model = JSON.parseObject(event, EventModel.class);
                        if(!config.containsKey(model.getEventType())){
                            LOGGER.error("不能识别的事件：" + model.getEventType());
                            continue;
                        }
                        List<EventHandler> handlers = config.get(model.getEventType());
                        for(EventHandler handler : handlers) {
                            handler.doHandle(model);
                        }
                    }
                }
            }
        }));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void config() {

    }
}
