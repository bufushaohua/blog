package com.jxlg.web.admin;

import com.jxlg.po.User;
import com.jxlg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/admin")
public class UserController {

    private static final String INPUT = "admin/user-input";
    private static final String Login_list = "redirect:/admin";
    private static final String Index_list = "redirect:/admin/index";

    @Autowired  //注入
    private UserService userService;

    //更新用户
    @GetMapping("/user/{id}/input")
    public String input(@PathVariable Long id, Model model){
        User user = userService.getUser(id);
        model.addAttribute("user",user);
        return INPUT;
    }

    //新增用户
    @GetMapping("/user/input")
    public String input(Model model) {
        model.addAttribute("user",new User());
        return INPUT;
    }

    //user-input页面的提交
    @PostMapping("/user")
    public String post(User user, RedirectAttributes attributes) {
        User u;
        // 对博客的新增操作和更新操作进行分类，以防止更新博客后，保存时将原来未更改的数据给重置为0（如view）
        if(user.getId() == null) {
            u = userService.saveUser(user);
            if(u == null) {
                attributes.addFlashAttribute("message", "新增用户失败！");
            }else{
                attributes.addFlashAttribute("message","新增用户成功！请登录");
            }
            return Login_list;
        }else {
            u = userService.updateUser(user.getId(), user);
            if(u == null) {
                attributes.addFlashAttribute("message", "更新用户失败！");
            }else{
                attributes.addFlashAttribute("message","更新用户成功！");
            }
            return Index_list;
        }
    }

}
