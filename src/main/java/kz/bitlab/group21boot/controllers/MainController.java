package kz.bitlab.group21boot.controllers;

import kz.bitlab.group21boot.entities.Roles;
import kz.bitlab.group21boot.entities.Users;
import kz.bitlab.group21boot.repositories.ItemRepository;
import kz.bitlab.group21boot.entities.Items;
import kz.bitlab.group21boot.repositories.RoleRepository;
import kz.bitlab.group21boot.repositories.UserRepository;
import kz.bitlab.group21boot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class MainController {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserService userService;

    @GetMapping(path = "/")
    public String index(Model model, @RequestParam(name = "key", defaultValue = "", required = false) String key){
        List<Items> items = itemRepository.findAllByDeletedAtNullOrderByIdAsc();
        model.addAttribute("items", items);
        return "index";
    }

    @GetMapping(path = "/add")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String addItemPage(Model model){
        model.addAttribute("user", getUserData());
        return "additempage";
    }

    @PostMapping(path = "/add")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String addItem(Model model,
                          @RequestParam(name = "name") String name,
                          @RequestParam(name = "price") int price,
                          @RequestParam(name = "magazin") String magazin){

        itemRepository.save(new Items(null, name, price, magazin, null));
        return "redirect:/";
    }

    @GetMapping(path = "/edit/{itemId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String editItem(Model model, @PathVariable(name = "itemId") Long itemId){

        Optional items = itemRepository.findById(itemId);
        model.addAttribute("tovar", itemRepository.findByIdAndDeletedAtNull(itemId));

        return "edit";

    }

    @PostMapping(path = "/save")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String saveItem(@RequestParam(name = "name") String name,
                           @RequestParam(name = "price") int price,
                           @RequestParam(name = "magazin") String magazin,
                           @RequestParam(name = "id") Long id){

        Items item = itemRepository.findByIdAndDeletedAtNull(id);

        if(item!=null){
            item.setName(name);
            item.setMagazinnynAty(magazin);
            item.setPrice(price);
            itemRepository.save(item);
        }
        return "redirect:/edit/"+id;
    }

    @PostMapping(path = "/delete")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String deleteItem(@RequestParam(name = "id") Long id){
        Items item = itemRepository.findByIdAndDeletedAtNull(id);
        if(item!=null){
            item.setDeletedAt(new Date());
            itemRepository.save(item);
        }
        return "redirect:/";
    }

    @GetMapping(path = "/enter")
    public String enter(Model model){

        return "enter";

    }

    @GetMapping(path = "/register")
    public String register(Model model){
        return "register";
    }

    @PostMapping(path = "/adduser")
    public String addUser(@RequestParam(name = "email") String email,
                          @RequestParam(name = "password") String password,
                          @RequestParam(name = "re_password") String rePassword,
                          @RequestParam(name = "full_name", required = false, defaultValue = "") String fullName){

        String redirect = "redirect:/register?error";

        Users user = userRepository.findByEmail(email);

        if(user==null){

            if(password.equals(rePassword)){

                Set<Roles> roles = new HashSet<>();
                Roles userRole = roleRepository.getOne(1L);
                roles.add(userRole);

                user = new Users(null, email, password, fullName, roles);
                userService.registerUser(user);
                redirect = "redirect:/register?success";

            }

        }

        return redirect;

    }

    @GetMapping(path = "/profile")
    @PreAuthorize("isAuthenticated()")
    public String profile(Model model){

        model.addAttribute("user", getUserData());

        return "profile";
    }

    public Users getUserData(){
        Users userData = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            User secUser = (User)authentication.getPrincipal();
            userData = userRepository.findByEmail(secUser.getUsername());
        }
        return userData;
    }


}
