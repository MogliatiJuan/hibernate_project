package app.service;

import model.Profesor;
import java.util.List;
import java.util.Optional;

public interface ProfessorService {
    List<Profesor> list();
    
    Optional<Profesor> findById(Integer dni);
    
    Optional<Profesor> findByLastNameAndFirstName(String apellido, String nombre);
    
    Profesor create(String apellido, String nombre, Integer dni, String fechaNac, Integer antiguedad, Integer idCiudad);
    
    void update(Profesor profesor);
    
    void delete(Profesor profesor);
}
