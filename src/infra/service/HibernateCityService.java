package infra.service;

import app.service.CityService;
import model.City;

import java.util.List;
import java.util.Optional;

public class HibernateCityService extends BaseHibernateService implements CityService {

    @Override
    public List<City> list() {
        return executeInSession(session -> {
            return createQuery(session, "from City order by idCity", City.class);
        });
    }

    @Override
    public Optional<City> findById(Integer id) {
        if (id == null) {
            return Optional.empty();
        }
        return executeInSession(session -> {
            City c = (City) session.get(City.class, id);
            return Optional.ofNullable(c);
        });
    }

    @Override
    public Optional<City> findByName(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return Optional.empty();
        }
        return executeInSession(session -> {
            List<City> list = createQuery(session, "from City c where c.nombre = :n", "n", nombre.trim(), City.class);
            if (list != null && !list.isEmpty()) {
                return Optional.of(list.get(0));
            }
            return Optional.empty();
        });
    }

    @Override
    public City create(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }
        
        return executeInTransaction(session -> {
            City c = new City(nombre.trim());
            session.persist(c);
            return c;
        });
    }

    @Override
    public void update(City ciudad) {
        if (ciudad == null || ciudad.getIdCity() == null) {
            throw new IllegalArgumentException("City cannot be null and must have idCity");
        }
        
        executeInTransaction(session -> {
            City c = (City) session.get(City.class, ciudad.getIdCity());
            if (c == null) {
                throw new IllegalArgumentException("City not found with id: " + ciudad.getIdCity());
            }
            
            c.setNombre(ciudad.getNombre());
            session.update(c);
            return null;
        });
    }

    @Override
    public void delete(City ciudad) {
        if (ciudad == null) {
            throw new IllegalArgumentException("City cannot be null");
        }
        
        executeInTransaction(session -> {
            City c = (City) session.get(City.class, ciudad.getIdCity());
            if (c != null) {
                session.delete(c);
            }
            return null;
        });
    }
}
