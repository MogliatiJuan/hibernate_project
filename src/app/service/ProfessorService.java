package app.service;

import model.Professor;
import java.util.List;
import java.util.Optional;

public interface ProfessorService {
    List<Professor> list();
    
    Optional<Professor> findById(Integer dni);
    
    Optional<Professor> findByLastNameAndFirstName(String lastName, String firstName);
    
    Professor create(String lastName, String firstName, Integer dni, String birthDateYmd, Integer seniority, Integer idCity);
    
    void update(Professor professor);
    
    void delete(Professor professor);
}
