package infra.service;

import app.service.FacultyService;
import model.City;
import model.Faculty;

import java.util.List;
import java.util.Optional;

public class HibernateFacultyService extends BaseHibernateService implements FacultyService {

    @Override
    public List<Faculty> list() {
        return executeInSession(session -> {
            return createQuery(session, "from Faculty f order by f.idFaculty", Faculty.class);
        });
    }

    @Override
    public Optional<Faculty> findById(Integer id) {
        if (id == null) {
            return Optional.empty();
        }
        return executeInSession(session -> {
            Faculty f = (Faculty) session.get(Faculty.class, id);
            return Optional.ofNullable(f);
        });
    }

    @Override
    public Optional<Faculty> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return Optional.empty();
        }
        return executeInSession(session -> {
            List<Faculty> list = createQuery(session, "from Faculty f where f.name = :n", "n", name.trim(), Faculty.class);
            if (list != null && !list.isEmpty()) {
                return Optional.of(list.get(0));
            }
            return Optional.empty();
        });
    }

    @Override
    public Faculty create(String name, Integer idCity) {
        if (name == null || idCity == null) {
            throw new IllegalArgumentException("Name and idCity are required");
        }
        
        return executeInTransaction(session -> {
            City c = (City) session.get(City.class, idCity);
            if (c == null) {
                throw new IllegalArgumentException("City does not exist with id: " + idCity);
            }
            
            Faculty f = new Faculty(name.trim(), c);
            session.persist(f);
            return f;
        });
    }

    @Override
    public void update(Faculty faculty) {
        if (faculty == null || faculty.getIdFaculty() == null) {
            throw new IllegalArgumentException("Faculty cannot be null and must have idFaculty");
        }
        
        executeInTransaction(session -> {
            Faculty f = (Faculty) session.get(Faculty.class, faculty.getIdFaculty());
            if (f == null) {
                throw new IllegalArgumentException("Faculty not found with id: " + faculty.getIdFaculty());
            }
            
            f.setName(faculty.getName());
            if (faculty.getCity() != null) {
                City c = (City) session.get(City.class, faculty.getCity().getIdCity());
                if (c != null) {
                    f.setCity(c);
                }
            }
            
            session.update(f);
            return null;
        });
    }

    @Override
    public void delete(Faculty faculty) {
        if (faculty == null) {
            throw new IllegalArgumentException("Faculty cannot be null");
        }
        
        executeInTransaction(session -> {
            Faculty f = (Faculty) session.get(Faculty.class, faculty.getIdFaculty());
            if (f != null) {
                session.delete(f);
            }
            return null;
        });
    }
}
