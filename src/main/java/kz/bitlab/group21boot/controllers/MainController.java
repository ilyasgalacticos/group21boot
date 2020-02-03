package kz.bitlab.group21boot.controllers;

import kz.bitlab.group21boot.ItemRepository;
import kz.bitlab.group21boot.entities.Items;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class MainController {

    @Autowired
    ItemRepository itemRepository;

    @GetMapping(path = "/")
    public String index(Model model, @RequestParam(name = "key", defaultValue = "", required = false) String key){
        return "index";
    }

    @GetMapping(path = "/about")
    public String about(Model model){

        List<Items> items = itemRepository.findAllByDeletedAtNullOrderByIdAsc();
        model.addAttribute("items", items);

        return "about";
    }

    @PostMapping(path = "/add")
    public String addItem(Model model,
                          @RequestParam(name = "name") String name,
                          @RequestParam(name = "price") int price,
                          @RequestParam(name = "magazin") String magazin){

        itemRepository.save(new Items(null, name, price, magazin, null));
        return "redirect:/";
    }

    @GetMapping(path = "/edit/{itemId}")
    public String editItem(Model model, @PathVariable(name = "itemId") Long itemId){

        Optional items = itemRepository.findById(itemId);
        model.addAttribute("tovar", itemRepository.findByIdAndDeletedAtNull(itemId));

        return "edit";

    }

    @PostMapping(path = "/save")
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
    public String deleteItem(@RequestParam(name = "id") Long id){
        Items item = itemRepository.findByIdAndDeletedAtNull(id);
        if(item!=null){
            item.setDeletedAt(new Date());
            itemRepository.save(item);
        }
        return "redirect:/";
    }

}
