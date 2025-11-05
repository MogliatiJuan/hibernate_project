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
    public Optional<City> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return Optional.empty();
        }
        return executeInSession(session -> {
            List<City> list = createQuery(session, "from City c where c.name = :n", "n", name.trim(), City.class);
            if (list != null && !list.isEmpty()) {
                return Optional.of(list.get(0));
            }
            return Optional.empty();
        });
    }

    @Override
    public City create(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }
        
        return executeInTransaction(session -> {
            City c = new City(name.trim());
            session.persist(c);
            return c;
        });
    }

    @Override
    public void update(City city) {
        if (city == null || city.getIdCity() == null) {
            throw new IllegalArgumentException("City cannot be null and must have idCity");
        }
        
        executeInTransaction(session -> {
            City c = (City) session.get(City.class, city.getIdCity());
            if (c == null) {
                throw new IllegalArgumentException("City not found with id: " + city.getIdCity());
            }
            
            c.setName(city.getName());
            session.update(c);
            return null;
        });
    }

    @Override
    public void delete(City city) {
        if (city == null) {
            throw new IllegalArgumentException("City cannot be null");
        }
        
        executeInTransaction(session -> {
            City c = (City) session.get(City.class, city.getIdCity());
            if (c != null) {
                session.delete(c);
            }
            return null;
        });
    }
}
