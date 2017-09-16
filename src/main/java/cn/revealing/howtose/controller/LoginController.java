package cn.revealing.howtose.controller;

import cn.revealing.howtose.async.EventModel;
import cn.revealing.howtose.async.EventProducer;
import cn.revealing.howtose.async.EventType;
import cn.revealing.howtose.services.UserService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by GJW on 2017/5/17.
 */
@Controller
public class LoginController {
    public static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    UserService userService;


    @Autowired
    EventProducer eventProducer;

    @RequestMapping(value = "/reg/", method = RequestMethod.POST)
    public String reg(Model model,
                        @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value = "next", required = false, defaultValue = "") String next,
                      HttpServletResponse response){
        try {
            Map<String, String> map = userService.register(username, password);
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket"));
                cookie.setPath("/");
                response.addCookie(cookie);
                if(!StringUtils.isBlank(next)){
                    return "redirect:" + next;
                } else {
                    return "redirect:/";
                }
            } else {
                model.addAttribute("msg", map.get("msg"));
                return "login";
            }
        }catch (Exception e){
            LOGGER.error("注册异常： " +e.getMessage());

            return "login";
        }
    }

    @RequestMapping(value = "/login/", method = RequestMethod.POST)
    public String login(Model model,
                        @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value = "next", required = false, defaultValue = "") String next,
                        @RequestParam(value = "rememberme", defaultValue = "false" ) boolean rememberme,
                        HttpServletResponse response){
        try {
            Map<String, String> map = userService.login(username, password);
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket"));
                cookie.setPath("/");
                response.addCookie(cookie);
                EventModel eventModel = new EventModel(EventType.LOGIN);
                eventModel.setActorId(Integer.parseInt(map.get("userId")))
                        .setExt("username", username)
                        .setExt("email", "490861454@qq.com");
                eventProducer.fireEvent(eventModel);

                if(!StringUtils.isBlank(next)){
                    return "redirect:" + next;
                } else {
                    return "redirect:/";
                }
            } else {
                model.addAttribute("msg", map.get("msg"));
                return "login";
            }


        }catch (Exception e){
            LOGGER.error("登录异常： " +e.getMessage());

            return "login";
        }
    }

    @RequestMapping(value = "/reglogin", method = RequestMethod.GET)
    public String getLogin(Model model,
                           @RequestParam(value = "next", required = false, defaultValue = "") String next) {
        model.addAttribute("next", next);
        return "login";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/";
    }
}
