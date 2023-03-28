package com.antiplagiat.controller;

import com.antiplagiat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ModelAndView controlPanel(ModelAndView model) {
        model.addObject("allUsers", userService.allUsers());
        model.setViewName("admin");
        return model;
    }

    @GetMapping("/users")
    public String userList(Model model) {
        model.addAttribute("allUsers", userService.allUsers());
        return "admin_users";
    }

    @PostMapping("/users")
    public String  deleteUser(@RequestParam(required = true, defaultValue = "" ) Long userId,
                              @RequestParam(required = true, defaultValue = "" ) String action,
                              Model model) {
        if (action.equals("delete")){
            userService.deleteUser(userId);
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/gt/{userId}")
    public String  gtUser(@PathVariable("userId") Long userId, Model model) {
        model.addAttribute("allUsers", userService.usergtList(userId));
        return "admin_users";
    }
}
