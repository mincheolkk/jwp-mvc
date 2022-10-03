package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestParam;
import core.db.DataBase;
import next.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
    @RequestMapping(value = "/users/login")
    public String execute(@RequestParam String userId, @RequestParam String password, HttpServletRequest req) throws Exception {
        User user = DataBase.findUserById(userId);
        if (user == null) {
            req.setAttribute("loginFailed", true);
            return "/user/login.jsp";
        }
        if (user.matchPassword(password)) {
            HttpSession session = req.getSession();
            session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);
            return "redirect:/";
        } else {
            req.setAttribute("loginFailed", true);
            return "/user/login.jsp";
        }
    }

    @RequestMapping(value = "/users/loginForm")
    public String loginForm(HttpServletRequest req, HttpServletResponse resp) {
        return "/user/login.jsp";
    }

    @RequestMapping(value = "/users/form")
    public String getform(HttpServletRequest req, HttpServletResponse resp) {
        return "/user/form.jsp";
    }
}
