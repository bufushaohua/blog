package com.jxlg.web.admin;

import com.jxlg.po.User;
import com.jxlg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class LoginController {

    @Autowired  //注入
    private UserService userService;

    //直接输入URL，跳转登录页面
    @GetMapping  //未指定路径，默认上方"/admin"
    public String LoginPage() {
        return "admin/login";
    }

    //由页面跳转到登录页面
    @PostMapping
    public String LoginPage1() {return "admin/login";}

    //登陆
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes attributes) {
        User user = userService.checkUser(username, password);
        if( user != null){
            user.setPassword(null); //不需要携带密码，很不安全
            session.setAttribute("user",user);
            return "admin/index";
        }else {   //用户名或密码不对，需要返回登陆页面，同时给前端一些提示
//            return "admin/login";  不能使用这种方法，因为再次访问就会出错
            attributes.addFlashAttribute("message","用户名和密码错误");
            return "redirect:/admin";
        }
    }

//    这里可以不用if、else判断
    @GetMapping("/index")
    public String BackIndex(HttpSession session){
        User user = (User)session.getAttribute("user");
        if(user != null){
            return "admin/index";
        }else{
            return "admin/BackLogin";
        }
    }

    //登出
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/admin";
    }
}
