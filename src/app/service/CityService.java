package app.service;

import model.City;
import java.util.List;
import java.util.Optional;

public interface CityService {
    List<City> list();
    
    Optional<City> findById(Integer id);
    
    Optional<City> findByName(String name);
    
    City create(String name);
    
    void update(City city);
    
    void delete(City city);
}
