package kz.bitlab.group21boot.controllers;

import kz.bitlab.group21boot.ItemRepository;
import kz.bitlab.group21boot.entities.Items;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class MainController {

    @Autowired
    ItemRepository itemRepository;

    @GetMapping(path = "/")
    public String index(Model model, @RequestParam(name = "key", defaultValue = "", required = false) String key){
        // Hello BITLAB
        List<Items> items;
        if(key!=null&&!key.equals("")){
            items = itemRepository.findAllByName(key);
        }else{
            items = itemRepository.findAll();
        }
        model.addAttribute("items", items);

        return "index";
    }

    @PostMapping(path = "/add")
    public String addItem(Model model,
                          @RequestParam(name = "name") String name,
                          @RequestParam(name = "price") int price,
                          @RequestParam(name = "magazin") String magazin){

        itemRepository.save(new Items(null, name, price, magazin));
        return "redirect:/";
    }

    @GetMapping(path = "/edit/{itemId}")
    public String editItem(Model model, @PathVariable(name = "itemId") Long itemId){

        Optional items = itemRepository.findById(itemId);
        model.addAttribute("tovar", items.orElse(new Items(0L, "No Item", 0, "No Magazin")));

        return "edit";

    }

    @PostMapping(path = "/save")
    public String saveItem(@RequestParam(name = "name") String name,
                           @RequestParam(name = "price") int price,
                           @RequestParam(name = "magazin") String magazin,
                           @RequestParam(name = "id") Long id){

        Items item = itemRepository.findById(id).orElse(null);

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
        itemRepository.deleteById(id);
        return "redirect:/";
    }

}
