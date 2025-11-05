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
    public Optional<Career> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return Optional.empty();
        }
        return executeInSession(session -> {
            List<Career> list = createQuery(session, "from Career c where c.name = :n", "n", name.trim(), Career.class);
            if (list != null && !list.isEmpty()) {
                return Optional.of(list.get(0));
            }
            return Optional.empty();
        });
    }

    @Override
    public Career create(String name, Integer idFaculty) {
        if (name == null || idFaculty == null) {
            throw new IllegalArgumentException("Name and idFaculty are required");
        }
        
        return executeInTransaction(session -> {
            Faculty f = (Faculty) session.get(Faculty.class, idFaculty);
            if (f == null) {
                throw new IllegalArgumentException("Faculty does not exist with id: " + idFaculty);
            }
            
            Career c = new Career(name.trim(), f);
            session.persist(c);
            return c;
        });
    }

    @Override
    public void update(Career career) {
        if (career == null || career.getIdCareer() == null) {
            throw new IllegalArgumentException("Career cannot be null and must have idCareer");
        }
        
        executeInTransaction(session -> {
            Career c = (Career) session.get(Career.class, career.getIdCareer());
            if (c == null) {
                throw new IllegalArgumentException("Career not found with id: " + career.getIdCareer());
            }
            
            c.setName(career.getName());
            if (career.getFaculty() != null) {
                Faculty f = (Faculty) session.get(Faculty.class, career.getFaculty().getIdFaculty());
                if (f != null) {
                    c.setFaculty(f);
                }
            }
            
            session.update(c);
            return null;
        });
    }

    @Override
    public void delete(Career career) {
        if (career == null) {
            throw new IllegalArgumentException("Career cannot be null");
        }
        
        executeInTransaction(session -> {
            Career c = (Career) session.get(Career.class, career.getIdCareer());
            if (c != null) {
                session.delete(c);
            }
            return null;
        });
    }
}
