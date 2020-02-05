package kz.bitlab.group21boot.repositories;

import kz.bitlab.group21boot.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {

    Users findByEmail(String email);

}
