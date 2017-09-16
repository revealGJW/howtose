package cn.revealing.howtose.controller;

import cn.revealing.howtose.model.EntityType;
import cn.revealing.howtose.model.Feed;
import cn.revealing.howtose.model.HostHolder;
import cn.revealing.howtose.services.FeedService;
import cn.revealing.howtose.services.FollowService;
import cn.revealing.howtose.util.JedisAdapter;
import cn.revealing.howtose.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by GJW on 2017/7/20.
 */
@Controller
public class FeedController {
    @Autowired
    FeedService feedService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    FollowService followService;

    @Autowired
    JedisAdapter jedisAdapter;
    @RequestMapping(path = "pullfeeds", method = RequestMethod.GET)
    private String getPullFeeds(Model model){
        int localHostUser = hostHolder.getUser() == null ? 0 : hostHolder.getUser().getId();
        List<Integer> followees = new ArrayList<>();
        if(localHostUser != 0) {
            followees = followService.getFollowee(EntityType.ENTITY_USER, localHostUser, Integer.MAX_VALUE);
        }
        List<Feed> feeds = feedService.getUserFeeds(Integer.MAX_VALUE, followees, 10);
        model.addAttribute("feeds", feeds);
        return "feeds";
    }


    @RequestMapping(path = "pushfeeds", method = RequestMethod.GET)
    private String getPushFeeds(Model model){
        int localHostUser = hostHolder.getUser() == null ? 0 : hostHolder.getUser().getId();
        List<String> feedIds = jedisAdapter.lrange(RedisKeyUtil.getTimelineKey(localHostUser), 0, 10);
        List<Feed> feeds = new ArrayList<>();
        for(String feedId : feedIds) {
            Feed feed = feedService.getFeedById(Integer.parseInt(feedId));
            if(feed == null)
                continue;
            feeds.add(feed);
        }
        model.addAttribute("feeds", feeds);
        return "feeds";
    }
}
