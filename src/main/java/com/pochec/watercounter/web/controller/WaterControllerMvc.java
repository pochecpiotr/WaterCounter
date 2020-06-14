package com.pochec.watercounter.web.controller;

import com.pochec.watercounter.model.User;
import com.pochec.watercounter.model.Water;
import com.pochec.watercounter.repository.UserRepository;
import com.pochec.watercounter.repository.WaterRepository;
import com.pochec.watercounter.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/waterstats")
public class WaterControllerMvc {

    private WaterRepository waterRepository;
    private UserRepository userRepository;


    @Autowired
    public WaterControllerMvc(WaterRepository waterRepository, UserRepository userRepository) {
        this.waterRepository = waterRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/user")
    public String listWater(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        Long userid = userRepository.findByEmail(username).getId();
        List<Water> allWaterList = waterRepository.findAll();
        List<Water> waterList = new ArrayList<>();
        for (Water water: allWaterList) {
            if (water.getUserId() == userid) {
                waterList.add(water);
            }
        }
        model.addAttribute("waterList", waterList);
        return "list";
    }

    @GetMapping("/all")
    public String listWaterAll(Model model) {
        List<Water> waterList = waterRepository.findAll();
        model.addAttribute("waterList", waterList);
        return "listall";
    }

    @RequestMapping(method=RequestMethod.POST)
    public String addWater(@ModelAttribute Water waterModel, RedirectAttributes redirectAttr) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        User user = userRepository.findByEmail(username);
        waterModel.setUserId(user.getId());
        waterModel.setDate(LocalDate.now());
        waterRepository.save(waterModel);
        redirectAttr.addFlashAttribute("message", "Water added successfuly");
        return "redirect:/";
    }
}
