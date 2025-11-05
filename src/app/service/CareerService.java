package app.service;

import model.Carrera;
import java.util.List;
import java.util.Optional;

public interface CareerService {
    List<Carrera> list();
    
    Optional<Carrera> findById(Integer id);
    
    Optional<Carrera> findByName(String nombre);
    
    Carrera create(String nombre, Integer idFacultad);
    
    void update(Carrera carrera);
    
    void delete(Carrera carrera);
}
