package app.service;

import model.City;
import java.util.List;
import java.util.Optional;

public interface CityService {
    List<City> list();
    
    Optional<City> findById(Integer id);
    
    Optional<City> findByName(String nombre);
    
    City create(String nombre);
    
    void update(City ciudad);
    
    void delete(City ciudad);
}
