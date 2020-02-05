package kz.bitlab.group21boot.repositories;

import kz.bitlab.group21boot.entities.Items;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Items, Long> {
    List<Items> findAllByDeletedAtNullOrderByIdAsc();
    Items findByIdAndDeletedAtNull(Long id);
}