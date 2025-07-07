package net.codejava.repositories;

import net.codejava.entity.User_imc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User_imc, String> {
    User_imc findByUsername(String username);
}