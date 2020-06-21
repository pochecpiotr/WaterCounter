package com.pochec.watercounter.web.controller;

import com.pochec.watercounter.model.Water;
import com.pochec.watercounter.repository.UserRepository;
import com.pochec.watercounter.repository.WaterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/water")
public class WaterControllerDelete {

    private WaterRepository waterRepository;
    private UserRepository userRepository;


    @Autowired
    public WaterControllerDelete(WaterRepository waterRepository, UserRepository userRepository) {
        this.waterRepository = waterRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/delete")
    public String listWaterToday(Model model) {
        String username = getUsername();
        Long userid = userRepository.findByEmail(username).getId();
        List<Water> allWaterList = waterRepository.findAll();
        List<Water> waterList = new ArrayList<>();
        for (Water water: allWaterList) {
            if (water.getUserId().equals(userid)) {
                waterList.add(water);
            }
        }
        model.addAttribute("waterList", waterList);
        return "deletewater";
    }

    @RequestMapping(method= RequestMethod.POST)
    public String deleteWater(@RequestParam("idChecked") List<String> idwaters, RedirectAttributes redirectAttr) {
        if (idwaters != null) {
            for (String idwaterStr : idwaters) {
                Long idwater = Long.parseLong(idwaterStr);
                waterRepository.deleteById(idwater);
                redirectAttr.addFlashAttribute("message", "Water deleted successfuly");
            }
        }
        return "redirect:/";
    }

    private String getUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        return username;
    }
}
