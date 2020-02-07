package kz.bitlab.group21boot.rest;

import kz.bitlab.group21boot.entities.Items;
import kz.bitlab.group21boot.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/rest")
public class MainRestController {

    @Autowired
    ItemRepository itemRepository;

    @ResponseBody
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping(path = "/allitems")
    public ResponseEntity<List<Items>> allItems(){
        List<Items> items = itemRepository.findAllByDeletedAtNullOrderByIdAsc();
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @ResponseBody
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping(path = "/item/{idbek}")
    public ResponseEntity<Items> getItem(@PathVariable(name = "idbek") Long id){

        Items item = itemRepository.findByIdAndDeletedAtNull(id);
        return new ResponseEntity<>(item, HttpStatus.OK);

    }

    @ResponseBody
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping(path = "/additem")
    public ResponseEntity<String> addItem(@RequestParam(name = "name") String name,
                                          @RequestParam(name = "price") int price,
                                          @RequestParam(name = "magazin") String magazin){

        itemRepository.save(new Items(null, name, price, magazin, null));
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }


    @ResponseBody
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping(path = "/saveitem")
    public ResponseEntity<String> saveItem(
            @RequestParam(name = "id") Long id,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "price") int price,
            @RequestParam(name = "magazin") String magazin){

        Items item = itemRepository.findByIdAndDeletedAtNull(id);

        item.setName(name);
        item.setPrice(price);
        item.setMagazinnynAty(magazin);

        itemRepository.save(item);

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }


}
