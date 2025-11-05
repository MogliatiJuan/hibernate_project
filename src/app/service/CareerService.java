package app.service;

import model.Career;
import java.util.List;
import java.util.Optional;

public interface CareerService {
    List<Career> list();
    
    Optional<Career> findById(Integer id);
    
    Optional<Career> findByName(String name);
    
    Career create(String name, Integer idFaculty);
    
    void update(Career career);
    
    void delete(Career career);
}
