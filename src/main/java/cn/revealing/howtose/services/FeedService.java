package cn.revealing.howtose.services;

import cn.revealing.howtose.dao.FeedDAO;
import cn.revealing.howtose.model.Feed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by GJW on 2017/7/20.
 */
@Service
public class FeedService {
    @Autowired
    FeedDAO feedDAO;
    public List<Feed> getUserFeeds(int maxId, List<Integer> userIds, int count) {
         return feedDAO.selectUserFeeds(maxId, userIds, count);
    }

    public boolean addFeed(Feed feed) {
        feedDAO.addFeed(feed);
        return feed.getId() > 0;
    }

    public Feed getFeedById(int id) {
        return feedDAO.getFeedById(id);
    }
}
