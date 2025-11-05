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
            return createQuery(session, "from Professor p order by p.seniority desc", Professor.class);
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
    public Optional<Professor> findByLastNameAndFirstName(String lastName, String firstName) {
        if (lastName == null || firstName == null || lastName.trim().isEmpty() || firstName.trim().isEmpty()) {
            return Optional.empty();
        }
        return executeInSession(session -> {
            List<Professor> list = createQueryWithParams(session, 
                "from Professor p where p.lastName = :a and p.firstName = :n", 
                Professor.class,
                "a", lastName.trim(),
                "n", firstName.trim());
            if (list != null && !list.isEmpty()) {
                return Optional.of(list.get(0));
            }
            return Optional.empty();
        });
    }

    @Override
    public Professor create(String lastName, String firstName, Integer dni, String birthDateYmd, Integer seniority, Integer idCity) {
        if (lastName == null || firstName == null || dni == null || seniority == null || idCity == null) {
            throw new IllegalArgumentException("Last name, first name, DNI, seniority and idCity are required");
        }
        
        return executeInTransaction(session -> {
            City c = (City) session.get(City.class, idCity);
            if (c == null) {
                throw new IllegalArgumentException("City does not exist with id: " + idCity);
            }
            
            Professor p = new Professor(seniority, lastName.trim(), firstName.trim(), dni, 
                    birthDateYmd != null ? birthDateYmd.trim() : null, c);
            session.persist(p);
            return p;
        });
    }

    @Override
    public void update(Professor professor) {
        if (professor == null || professor.getDni() == null) {
            throw new IllegalArgumentException("Professor cannot be null and must have DNI");
        }
        
        executeInTransaction(session -> {
            Professor p = (Professor) session.get(Professor.class, professor.getDni());
            if (p == null) {
                throw new IllegalArgumentException("Professor not found with DNI: " + professor.getDni());
            }
            
            p.setSeniority(professor.getSeniority());
            if (professor.getCity() != null) {
                City c = (City) session.get(City.class, professor.getCity().getIdCity());
                if (c != null) {
                    p.setCity(c);
                }
            }
            
            session.update(p);
            return null;
        });
    }

    @Override
    public void delete(Professor professor) {
        if (professor == null) {
            throw new IllegalArgumentException("Professor cannot be null");
        }
        
        executeInTransaction(session -> {
            Professor p = (Professor) session.get(Professor.class, professor.getDni());
            if (p != null) {
                session.delete(p);
            }
            return null;
        });
    }
}
