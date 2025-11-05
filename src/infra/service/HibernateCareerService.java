package infra.service;

import app.service.CareerService;
import model.Carrera;
import model.Facultad;

import java.util.List;
import java.util.Optional;

public class HibernateCareerService extends BaseHibernateService implements CareerService {

    @Override
    public List<Carrera> list() {
        return executeInSession(session -> {
            return createQuery(session, "from Carrera c order by c.idCarrera", Carrera.class);
        });
    }

    @Override
    public Optional<Carrera> findById(Integer id) {
        if (id == null) {
            return Optional.empty();
        }
        return executeInSession(session -> {
            Carrera c = (Carrera) session.get(Carrera.class, id);
            return Optional.ofNullable(c);
        });
    }

    @Override
    public Optional<Carrera> findByName(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return Optional.empty();
        }
        return executeInSession(session -> {
            List<Carrera> list = createQuery(session, "from Carrera c where c.nombre = :n", "n", nombre.trim(), Carrera.class);
            if (list != null && !list.isEmpty()) {
                return Optional.of(list.get(0));
            }
            return Optional.empty();
        });
    }

    @Override
    public Carrera create(String nombre, Integer idFacultad) {
        if (nombre == null || idFacultad == null) {
            throw new IllegalArgumentException("Name and idFacultad are required");
        }
        
        return executeInTransaction(session -> {
            Facultad f = (Facultad) session.get(Facultad.class, idFacultad);
            if (f == null) {
                throw new IllegalArgumentException("Faculty does not exist with id: " + idFacultad);
            }
            
            Carrera c = new Carrera(nombre.trim(), f);
            session.persist(c);
            return c;
        });
    }

    @Override
    public void update(Carrera carrera) {
        if (carrera == null || carrera.getIdCarrera() == null) {
            throw new IllegalArgumentException("Career cannot be null and must have idCarrera");
        }
        
        executeInTransaction(session -> {
            Carrera c = (Carrera) session.get(Carrera.class, carrera.getIdCarrera());
            if (c == null) {
                throw new IllegalArgumentException("Career not found with id: " + carrera.getIdCarrera());
            }
            
            c.setNombre(carrera.getNombre());
            if (carrera.getFacultad() != null) {
                Facultad f = (Facultad) session.get(Facultad.class, carrera.getFacultad().getIdFacultad());
                if (f != null) {
                    c.setFacultad(f);
                }
            }
            
            session.update(c);
            return null;
        });
    }

    @Override
    public void delete(Carrera carrera) {
        if (carrera == null) {
            throw new IllegalArgumentException("Career cannot be null");
        }
        
        executeInTransaction(session -> {
            Carrera c = (Carrera) session.get(Carrera.class, carrera.getIdCarrera());
            if (c != null) {
                session.delete(c);
            }
            return null;
        });
    }
}
