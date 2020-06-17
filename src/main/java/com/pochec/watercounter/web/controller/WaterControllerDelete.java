package com.pochec.watercounter.web.controller;

import com.pochec.watercounter.model.Water;
import com.pochec.watercounter.repository.UserRepository;
import com.pochec.watercounter.repository.WaterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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

    @RequestMapping(method= RequestMethod.POST)
    public String deleteWater(@ModelAttribute Water waterModel, RedirectAttributes redirectAttr) {
        Water waterToDelete = waterModel;
        String username = getUsername();
        Long userid = userRepository.findByEmail(username).getId();
        List<Water> allWaterList = waterRepository.findByUserId(userid);
        for (Water water : allWaterList) {
            if (waterToDelete.getQuantity() == water.getQuantity()) {
                if (waterToDelete.getDate().toString().equals(water.getDate().toString()))
                    waterRepository.deleteById(water.getId());
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
