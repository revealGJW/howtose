package cn.revealing.howtose.controller;

import cn.revealing.howtose.async.EventModel;
import cn.revealing.howtose.async.EventProducer;
import cn.revealing.howtose.async.EventType;
import cn.revealing.howtose.model.User;
import cn.revealing.howtose.services.UserService;
import cn.revealing.howtose.util.JedisAdapter;
import cn.revealing.howtose.util.MailSender;
import cn.revealing.howtose.util.RedisKeyUtil;
import org.apache.commons.lang.RandomStringUtils;
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
import java.util.Random;
import java.util.UUID;

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

    @Autowired
    JedisAdapter jedisAdapter;

    @RequestMapping(value = "/reg/", method = RequestMethod.POST)
    public String reg(Model model,
                        @RequestParam("email") String email,
                        @RequestParam("password") String password,
                        @RequestParam(value = "next", required = false, defaultValue = "") String next,
                      HttpServletResponse response){
        try {
            String res =passCheckMail(email);
            if(!"OK".equals(res)) {
                model.addAttribute("msg", res);
                return "login";
            }
            EventModel eventModel = new EventModel();
            String code = RandomStringUtils.randomAlphabetic(4).toUpperCase();
            jedisAdapter.setex(email, 65, code);
            eventModel.setEventType(EventType.REGISTER)
                    .setExt("username", "用户")
                    .setExt("email", email)
                    .setExt("code", code);
            eventProducer.fireEvent(eventModel);
            return "check";
        }catch (Exception e){
            LOGGER.error("注册异常： " +e.getMessage());

            return "login";
        }
    }

    @RequestMapping(value = "/check/", method = RequestMethod.POST)
    public String check(Model model,
                        @RequestParam("email") String email,
                        @RequestParam("password") String password,
                        @RequestParam("code") String code,
                        @RequestParam(value = "next", required = false, defaultValue = "") String next,
                        HttpServletResponse response) {
        String curCode = jedisAdapter.get(email);
        if(curCode.equals("")) {
            return "验证码已过期或不存在";
        }
        if(!code.equals(curCode)) {
            model.addAttribute("msg", "验证码错误！");
            return "login";
        }
        Map<String, String> map = userService.register(email, password);
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
    }

    private String passCheckMail(String email) {
        User user = userService.selectByEmail(email);
        if(user != null) {
            return "邮箱已注册！";
        }
        boolean isMatch = true;
        if(!isMatch) return "邮箱格式错误！";
        boolean isExist = jedisAdapter.exists(email);
        if(isExist) return "验证码已发送过！";
        return "OK";
    }

    @RequestMapping(value = "/login/", method = RequestMethod.POST)
    public String login(Model model,
                        @RequestParam("email") String email,
                        @RequestParam("password") String password,
                        @RequestParam(value = "next", required = false, defaultValue = "") String next,
                        @RequestParam(value = "rememberme", defaultValue = "false" ) boolean rememberme,
                        HttpServletResponse response){
        try {
            Map<String, String> map = userService.login(email, password);
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
