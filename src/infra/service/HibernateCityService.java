package infra.service;

import app.service.CityService;
import model.Ciudad;

import java.util.List;
import java.util.Optional;

public class HibernateCityService extends BaseHibernateService implements CityService {

    @Override
    public List<Ciudad> list() {
        return executeInSession(session -> {
            return createQuery(session, "from Ciudad order by idCiudad", Ciudad.class);
        });
    }

    @Override
    public Optional<Ciudad> findById(Integer id) {
        if (id == null) {
            return Optional.empty();
        }
        return executeInSession(session -> {
            Ciudad c = (Ciudad) session.get(Ciudad.class, id);
            return Optional.ofNullable(c);
        });
    }

    @Override
    public Optional<Ciudad> findByName(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return Optional.empty();
        }
        return executeInSession(session -> {
            List<Ciudad> list = createQuery(session, "from Ciudad c where c.nombre = :n", "n", nombre.trim(), Ciudad.class);
            if (list != null && !list.isEmpty()) {
                return Optional.of(list.get(0));
            }
            return Optional.empty();
        });
    }

    @Override
    public Ciudad create(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }
        
        return executeInTransaction(session -> {
            Ciudad c = new Ciudad(nombre.trim());
            session.persist(c);
            return c;
        });
    }

    @Override
    public void update(Ciudad ciudad) {
        if (ciudad == null || ciudad.getIdCiudad() == null) {
            throw new IllegalArgumentException("City cannot be null and must have idCiudad");
        }
        
        executeInTransaction(session -> {
            Ciudad c = (Ciudad) session.get(Ciudad.class, ciudad.getIdCiudad());
            if (c == null) {
                throw new IllegalArgumentException("City not found with id: " + ciudad.getIdCiudad());
            }
            
            c.setNombre(ciudad.getNombre());
            session.update(c);
            return null;
        });
    }

    @Override
    public void delete(Ciudad ciudad) {
        if (ciudad == null) {
            throw new IllegalArgumentException("City cannot be null");
        }
        
        executeInTransaction(session -> {
            Ciudad c = (Ciudad) session.get(Ciudad.class, ciudad.getIdCiudad());
            if (c != null) {
                session.delete(c);
            }
            return null;
        });
    }
}
