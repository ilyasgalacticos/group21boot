package kz.bitlab.group21boot;

import kz.bitlab.group21boot.entities.Items;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Items, Long> {
    List<Items> findAllByMagazinnynAty(String magazin);
    List<Items> findAllByName(String name);
}