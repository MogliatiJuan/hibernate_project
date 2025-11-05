package app.service;

import model.Professor;
import java.util.List;
import java.util.Optional;

public interface ProfessorService {
    List<Professor> list();
    
    Optional<Professor> findById(Integer dni);
    
    Optional<Professor> findByLastNameAndFirstName(String apellido, String nombre);
    
    Professor create(String apellido, String nombre, Integer dni, String fechaNac, Integer antiguedad, Integer idCiudad);
    
    void update(Professor profesor);
    
    void delete(Professor profesor);
}
