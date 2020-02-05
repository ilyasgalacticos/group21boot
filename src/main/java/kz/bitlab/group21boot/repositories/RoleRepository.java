package kz.bitlab.group21boot.repositories;

import kz.bitlab.group21boot.entities.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Roles, Long> {



}