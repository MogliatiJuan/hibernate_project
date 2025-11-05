package infra.service;

import app.service.CareerService;
import model.Career;
import model.Faculty;

import java.util.List;
import java.util.Optional;

public class HibernateCareerService extends BaseHibernateService implements CareerService {

    @Override
    public List<Career> list() {
        return executeInSession(session -> {
            return createQuery(session, "from Career c order by c.idCareer", Career.class);
        });
    }

    @Override
    public Optional<Career> findById(Integer id) {
        if (id == null) {
            return Optional.empty();
        }
        return executeInSession(session -> {
            Career c = (Career) session.get(Career.class, id);
            return Optional.ofNullable(c);
        });
    }

    @Override
    public Optional<Career> findByName(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return Optional.empty();
        }
        return executeInSession(session -> {
            List<Career> list = createQuery(session, "from Career c where c.nombre = :n", "n", nombre.trim(), Career.class);
            if (list != null && !list.isEmpty()) {
                return Optional.of(list.get(0));
            }
            return Optional.empty();
        });
    }

    @Override
    public Career create(String nombre, Integer idFaculty) {
        if (nombre == null || idFaculty == null) {
            throw new IllegalArgumentException("Name and idFaculty are required");
        }
        
        return executeInTransaction(session -> {
            Faculty f = (Faculty) session.get(Faculty.class, idFaculty);
            if (f == null) {
                throw new IllegalArgumentException("Faculty does not exist with id: " + idFaculty);
            }
            
            Career c = new Career(nombre.trim(), f);
            session.persist(c);
            return c;
        });
    }

    @Override
    public void update(Career carrera) {
        if (carrera == null || carrera.getIdCareer() == null) {
            throw new IllegalArgumentException("Career cannot be null and must have idCareer");
        }
        
        executeInTransaction(session -> {
            Career c = (Career) session.get(Career.class, carrera.getIdCareer());
            if (c == null) {
                throw new IllegalArgumentException("Career not found with id: " + carrera.getIdCareer());
            }
            
            c.setNombre(carrera.getNombre());
            if (carrera.getFaculty() != null) {
                Faculty f = (Faculty) session.get(Faculty.class, carrera.getFaculty().getIdFaculty());
                if (f != null) {
                    c.setFaculty(f);
                }
            }
            
            session.update(c);
            return null;
        });
    }

    @Override
    public void delete(Career carrera) {
        if (carrera == null) {
            throw new IllegalArgumentException("Career cannot be null");
        }
        
        executeInTransaction(session -> {
            Career c = (Career) session.get(Career.class, carrera.getIdCareer());
            if (c != null) {
                session.delete(c);
            }
            return null;
        });
    }
}
