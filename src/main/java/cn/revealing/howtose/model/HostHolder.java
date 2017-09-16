package cn.revealing.howtose.model;

import org.springframework.stereotype.Component;

/**
 * Created by GJW on 2017/6/19.
 */
@Component
public class HostHolder {
    private static ThreadLocal<User> users = new ThreadLocal<>();

    public User getUser() {
        return users.get();
    }

    public void setUser(User user) {
        users.set(user);
    }

    public void clear() {
        users.remove();
    }
}
