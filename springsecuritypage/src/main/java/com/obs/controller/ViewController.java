package com.obs.controller;

import com.obs.security.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Created by ongbo on 2/2/2017.
 */
@Controller
public class ViewController {
    /**
     * Apply to all request mapping in this controller class
     *
     * @param user
     * @return
     */
    @ModelAttribute("currentUser")
    public User username(@AuthenticationPrincipal User user) {
        return user;
    }

    @GetMapping(value = "/profile")
    public String profile() {
        return "profile";
    }
}
