package net.codejava.services;

import net.codejava.repositories.UserRepository;
import net.codejava.entity.User_imc;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository repo;
    
    public List<User_imc> listAll() {
        return repo.findAll();
    }
    
    public void save(User_imc user) {
        repo.save(user);
    }
    
    public User_imc get(String id) { 
        return repo.findById(id).orElse(null); 
    }
    
    public void delete(String id) { 
        repo.deleteById(id);
    }

    public User_imc findByUsername(String username) {
        return repo.findByUsername(username);
    }
}