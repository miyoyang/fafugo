package com.lxh.fafugo.controller;

import com.lxh.fafugo.mapper.QuestionMapper;
import com.lxh.fafugo.mapper.UserMapper;
import com.lxh.fafugo.model.Question;
import com.lxh.fafugo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PulishController {

    @Autowired
    QuestionMapper questionMapper;
    @Autowired
    UserMapper userMapper;

    @GetMapping("/publish")
    public String publish() {
        return "publish";
    }

    @PostMapping("/publish")
    public ModelAndView doPublish(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("tag") String tag,
            HttpServletRequest request,
            ModelAndView model) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("title", title);
        mav.addObject("description", description);
        mav.addObject("tag", tag);

        User user = null;
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            mav.setViewName("index");
            return mav;
        } else {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    user = userMapper.findByToken(token);
                    if (user != null) {
                        request.getSession().setAttribute("user", user);
                    }
                    break;
                }
            }
            if (user == null) {
                model.addObject("error", "未登录");
            }
        }


        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(Long.valueOf(user.getId()));
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(question.getGmtCreate());
        questionMapper.create(question);
        mav.setViewName("index");
        return mav;
    }
}
