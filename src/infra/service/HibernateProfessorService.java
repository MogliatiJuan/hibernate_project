package infra.service;

import app.service.ProfessorService;
import model.City;
import model.Professor;

import java.util.List;
import java.util.Optional;

public class HibernateProfessorService extends BaseHibernateService implements ProfessorService {

    @Override
    public List<Professor> list() {
        return executeInSession(session -> {
            return createQuery(session, "from Professor p order by p.antiguedad desc", Professor.class);
        });
    }

    @Override
    public Optional<Professor> findById(Integer dni) {
        if (dni == null) {
            return Optional.empty();
        }
        return executeInSession(session -> {
            Professor p = (Professor) session.get(Professor.class, dni);
            return Optional.ofNullable(p);
        });
    }

    @Override
    public Optional<Professor> findByLastNameAndFirstName(String apellido, String nombre) {
        if (apellido == null || nombre == null || apellido.trim().isEmpty() || nombre.trim().isEmpty()) {
            return Optional.empty();
        }
        return executeInSession(session -> {
            List<Professor> list = createQueryWithParams(session, 
                "from Professor p where p.apellido = :a and p.nombre = :n", 
                Professor.class,
                "a", apellido.trim(),
                "n", nombre.trim());
            if (list != null && !list.isEmpty()) {
                return Optional.of(list.get(0));
            }
            return Optional.empty();
        });
    }

    @Override
    public Professor create(String apellido, String nombre, Integer dni, String fechaNac, Integer antiguedad, Integer idCity) {
        if (apellido == null || nombre == null || dni == null || antiguedad == null || idCity == null) {
            throw new IllegalArgumentException("Last name, first name, DNI, seniority and idCity are required");
        }
        
        return executeInTransaction(session -> {
            City c = (City) session.get(City.class, idCity);
            if (c == null) {
                throw new IllegalArgumentException("City does not exist with id: " + idCity);
            }
            
            Professor p = new Professor(antiguedad, apellido.trim(), nombre.trim(), dni, 
                    fechaNac != null ? fechaNac.trim() : null, c);
            session.persist(p);
            return p;
        });
    }

    @Override
    public void update(Professor profesor) {
        if (profesor == null || profesor.getDni() == null) {
            throw new IllegalArgumentException("Professor cannot be null and must have DNI");
        }
        
        executeInTransaction(session -> {
            Professor p = (Professor) session.get(Professor.class, profesor.getDni());
            if (p == null) {
                throw new IllegalArgumentException("Professor not found with DNI: " + profesor.getDni());
            }
            
            p.setAntiguedad(profesor.getAntiguedad());
            if (profesor.getCity() != null) {
                City c = (City) session.get(City.class, profesor.getCity().getIdCity());
                if (c != null) {
                    p.setCity(c);
                }
            }
            
            session.update(p);
            return null;
        });
    }

    @Override
    public void delete(Professor profesor) {
        if (profesor == null) {
            throw new IllegalArgumentException("Professor cannot be null");
        }
        
        executeInTransaction(session -> {
            Professor p = (Professor) session.get(Professor.class, profesor.getDni());
            if (p != null) {
                session.delete(p);
            }
            return null;
        });
    }
}
