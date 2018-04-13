package cn.revealing.howtose.controller;

import cn.revealing.howtose.model.Comment;
import cn.revealing.howtose.model.Keyword;
import cn.revealing.howtose.services.CommentService;
import cn.revealing.howtose.services.KeywordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author guojiawei
 * @date 2018/4/11
 */
@Controller
@RequestMapping(value = "/admin")
public class BackController {
    @Autowired
    KeywordService keywordService;

    @Autowired
    CommentService commentService;

    @RequestMapping(value = "/word", method = RequestMethod.GET)
    public String wordConfig(Model model) {
        List<Keyword> keywords = keywordService.getKeywords(0, 20);
        model.addAttribute("words", keywords);
        return "admin_word";
    }

    @RequestMapping(value = "/review", method = RequestMethod.GET)
    public String review(Model model) {
        List<Comment> comments = commentService.getLatestComment(0, 10);
        model.addAttribute("words", comments);
        return "admin_comment";
    }

    @RequestMapping(value = "/review/status", method = RequestMethod.POST)
    public String review(Model model, @RequestParam("comment_id") int commentId,
                         @RequestParam("status")int status) {
        commentService.changeCommentStatus(commentId, status);
        return "修改成功";
    }

    @RequestMapping(value = "/word", method = RequestMethod.POST)
    public String addWord(Model model, @RequestParam("word") String word,
                          @RequestParam("type") String stype
                          ) {
        int type = Integer.parseInt(stype);
        Keyword keyword = keywordService.getWordByType(word, type);
        if(keyword == null) {
            keywordService.addWord(word, type);
        } else {
            keywordService.updatePriority(keyword);
        }
        return "admin_word";
    }
}
