package app.service;

import model.Faculty;
import java.util.List;
import java.util.Optional;

public interface FacultyService {
    List<Faculty> list();
    
    Optional<Faculty> findById(Integer id);
    
    Optional<Faculty> findByName(String name);
    
    Faculty create(String name, Integer idCity);
    
    void update(Faculty faculty);
    
    void delete(Faculty faculty);
}
