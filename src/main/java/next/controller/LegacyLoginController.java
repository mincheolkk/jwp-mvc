package next.controller;

import core.db.DataBase;
import core.mvc.asis.Controller;
import core.mvc.view.ModelAndView;
import core.mvc.view.ResourceView;
import next.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LegacyLoginController implements Controller {
    @Override
    public ModelAndView execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");
        User user = DataBase.findUserById(userId);
        if (user == null) {
            req.setAttribute("loginFailed", true);
            return new ModelAndView(new ResourceView("/user/login.jsp"));
        }
        if (user.matchPassword(password)) {
            HttpSession session = req.getSession();
            session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);
            return new ModelAndView(new ResourceView("redirect:/"));
        } else {
            req.setAttribute("loginFailed", true);
            return new ModelAndView(new ResourceView("/user/login.jsp"));
        }
    }
}