package sec.project.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.domain.Signup;
import sec.project.repository.SignupRepository;
import sec.project.config.CustomUserDetailsService;

@Controller
public class SignupController {

    @Autowired
    private SignupRepository signupRepository;
    
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @RequestMapping("*")
    public String defaultMapping() {
        return "redirect:/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String loadForm() {
        return "form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String submitForm(@RequestParam String name, @RequestParam String address, Model model) {
        Signup signup = new Signup(name, address);
        signupRepository.save(signup);
        List<String> addresses = new ArrayList();
        for (Signup a : signupRepository.findByName(name))  addresses.add(a.getAddress());
        model.addAttribute("address", addresses);
        model.addAttribute("name", name);
      
       
        return "done";
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String loadAdmin(Model model) {
        model.addAttribute("signups",signupRepository.findAll());
        return "admin";
    }
    
    @RequestMapping(value = "/admin_login", method = RequestMethod.GET)
    public String loadlogin() {
        return "admin_login";
    }
    
    @RequestMapping(value = "/admin_login", method = RequestMethod.POST)
    public String loginAdmin(@RequestParam String name, @RequestParam String password,Model model) {
        try{
            if (customUserDetailsService.loadUserByUsername(name)!= null) 
            return "redirect:admin";
            }
        catch (UsernameNotFoundException e){
        
            model.addAttribute("error",e.getMessage());
            return "error";
        }
        return "redirect:admin_login";
    }    

    
}
