package net.codejava.controller;
import java.util.List;
import jakarta.servlet.http.HttpSession;

import net.codejava.entity.User_imc;
import net.codejava.entity.Formulario;
import net.codejava.entity.ImcHistory;
import net.codejava.services.UserService;
import net.codejava.repositories.ImcHistoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import jakarta.validation.Valid;

@Controller
public class AppController {

    @Autowired
    private UserService service;

    @Autowired
    private ImcHistoryRepository historyRepo;

    private User_imc convertirFormularioAUserImc(Formulario form) {
        User_imc user = new User_imc();
        user.setId(form.getUsername());
        user.setUsername(form.getUsername());
        user.setName(form.getName());
        user.setSurname(form.getSurname());
        user.setAge(form.getAge());
        user.setGender(form.getGender());
        user.setHeight(form.getHeight());
        user.setWeight(form.getWeight());
        user.setPassword(form.getPassword());
        return user;
    }

    @RequestMapping("/")
    public String viewHomePage(HttpSession session, Model model) {
        if (session.getAttribute("mySessionAttribute") != null) {
            List<ImcHistory> history = historyRepo.findAllByOrderByDateDesc();
            model.addAttribute("history", history);
            return "index";
        } else {
            model.addAttribute("formulario", new Formulario());
            return "login";
        }
    }

    @GetMapping("/login")
    public String login(Model model, HttpSession session) {
        session.setAttribute("mySessionAttribute", "sasas");
        model.addAttribute("formulario", new Formulario());
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@ModelAttribute("formulario") Formulario form, HttpSession session, Model model) {
        User_imc user = service.findByUsername(form.getUsername());
        if (user != null && user.getPassword().equals(form.getPassword())) {
            session.setAttribute("mySessionAttribute", user.getUsername());
            return "redirect:/";
        } else {
            model.addAttribute("formulario", new Formulario());
            model.addAttribute("loginError", "Wrong username or password");
            return "login";
        }
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new Formulario());
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(@Valid @ModelAttribute("user") Formulario userForm, BindingResult result, Model model) {
    User_imc existingUser = service.findByUsername(userForm.getUsername());
    if (existingUser != null) {
        result.rejectValue("username", "error.user", "This username is already taken!");
    }
    if (result.hasErrors()) {
        return "register";
    }

    User_imc user = convertirFormularioAUserImc(userForm);
    service.save(user);

    float altura = user.getHeight();
    float peso = user.getWeight();
    float imc = peso / (altura * altura);

    ImcHistory history = new ImcHistory(
        user.getUsername(),
        user.getName(),
        user.getAge(),
        user.getGender(),
        altura,
        peso,
        imc,
        java.time.LocalDateTime.now()
    );
    historyRepo.save(history);

    model.addAttribute("peso", peso);
    model.addAttribute("altura", altura);
    model.addAttribute("imc", imc);

    return "imc";
}

    @GetMapping("/update-imc")
    public String showUpdateImcForm(Model model, HttpSession session) {
        String username = (String) session.getAttribute("mySessionAttribute");
        User_imc user = service.findByUsername(username);
        model.addAttribute("user", user);
        return "update_imc";
    }

    @PostMapping("/update-imc")
    public String processUpdateImc(@ModelAttribute("user") User_imc user, Model model, HttpSession session) {
        String username = (String) session.getAttribute("mySessionAttribute");
        User_imc existingUser = service.findByUsername(username);
        if (existingUser == null) {
            return "redirect:/";
        }

        existingUser.setWeight(user.getWeight());
        existingUser.setHeight(user.getHeight());
        service.save(existingUser);

        float altura = existingUser.getHeight();
        float peso = existingUser.getWeight();
        float imc = peso / (altura * altura);

        ImcHistory history = new ImcHistory(
            existingUser.getUsername(),
            existingUser.getName(),
            existingUser.getAge(),
            existingUser.getGender(),
            altura,
            peso,
            imc,
            java.time.LocalDateTime.now()
        );
        historyRepo.save(history);

        model.addAttribute("peso", peso);
        model.addAttribute("altura", altura);
        model.addAttribute("imc", imc);

        return "imc";
    }

    @RequestMapping("/new")
    public String showNewUserPage(Model model) {
        User_imc user = new User_imc();
        model.addAttribute("user", user);
        return "new_imc";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String saveUser(@ModelAttribute("user") User_imc user, Model model) {
        User_imc existingUser = service.findByUsername(user.getUsername());

        if (existingUser != null) {
            boolean isChanged = false;

            if (existingUser.getAge() != user.getAge()) {
                existingUser.setAge(user.getAge());
                isChanged = true;
            }
            if (existingUser.getHeight() != user.getHeight()) {
                existingUser.setHeight(user.getHeight());
                isChanged = true;
            }
            if (existingUser.getWeight() != user.getWeight()) {
                existingUser.setWeight(user.getWeight());
                isChanged = true;
            }

            if (isChanged) {
                service.save(existingUser); 

                float altura = existingUser.getHeight();
                float peso = existingUser.getWeight();
                float imc = peso / (altura * altura);

                ImcHistory history = new ImcHistory(
                    existingUser.getUsername(),
                    existingUser.getName(),
                    existingUser.getAge(),
                    existingUser.getGender(),
                    altura,
                    peso,
                    imc,
                    java.time.LocalDateTime.now()
                );
                historyRepo.save(history); 
            }
        }

        model.addAttribute("user", user);
        model.addAttribute("success", "Changes saved successfully.");
        return "edit_user";
    }

    @RequestMapping("/edit/{username}")
    public ModelAndView showEditUserPage(@PathVariable String username) {
        ModelAndView mav = new ModelAndView("edit_user");
        User_imc user = service.get(username);
        mav.addObject("user", user);
        return mav;
    }

    @RequestMapping("/delete/{username}")
    public String deleteUser(@PathVariable String username) {
    List<ImcHistory> histories = historyRepo.findByUsername(username);
    for (ImcHistory history : histories) {
    historyRepo.delete(history);  
    }

    service.delete(username);
    return "redirect:/";
}
}