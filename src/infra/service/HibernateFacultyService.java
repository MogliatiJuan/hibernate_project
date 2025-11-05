package infra.service;

import app.service.FacultyService;
import model.Ciudad;
import model.Facultad;

import java.util.List;
import java.util.Optional;

public class HibernateFacultyService extends BaseHibernateService implements FacultyService {

    @Override
    public List<Facultad> list() {
        return executeInSession(session -> {
            return createQuery(session, "from Facultad f order by f.idFacultad", Facultad.class);
        });
    }

    @Override
    public Optional<Facultad> findById(Integer id) {
        if (id == null) {
            return Optional.empty();
        }
        return executeInSession(session -> {
            Facultad f = (Facultad) session.get(Facultad.class, id);
            return Optional.ofNullable(f);
        });
    }

    @Override
    public Optional<Facultad> findByName(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return Optional.empty();
        }
        return executeInSession(session -> {
            List<Facultad> list = createQuery(session, "from Facultad f where f.nombre = :n", "n", nombre.trim(), Facultad.class);
            if (list != null && !list.isEmpty()) {
                return Optional.of(list.get(0));
            }
            return Optional.empty();
        });
    }

    @Override
    public Facultad create(String nombre, Integer idCiudad) {
        if (nombre == null || idCiudad == null) {
            throw new IllegalArgumentException("Name and idCiudad are required");
        }
        
        return executeInTransaction(session -> {
            Ciudad c = (Ciudad) session.get(Ciudad.class, idCiudad);
            if (c == null) {
                throw new IllegalArgumentException("City does not exist with id: " + idCiudad);
            }
            
            Facultad f = new Facultad(nombre.trim(), c);
            session.persist(f);
            return f;
        });
    }

    @Override
    public void update(Facultad facultad) {
        if (facultad == null || facultad.getIdFacultad() == null) {
            throw new IllegalArgumentException("Faculty cannot be null and must have idFacultad");
        }
        
        executeInTransaction(session -> {
            Facultad f = (Facultad) session.get(Facultad.class, facultad.getIdFacultad());
            if (f == null) {
                throw new IllegalArgumentException("Faculty not found with id: " + facultad.getIdFacultad());
            }
            
            f.setNombre(facultad.getNombre());
            if (facultad.getCiudad() != null) {
                Ciudad c = (Ciudad) session.get(Ciudad.class, facultad.getCiudad().getIdCiudad());
                if (c != null) {
                    f.setCiudad(c);
                }
            }
            
            session.update(f);
            return null;
        });
    }

    @Override
    public void delete(Facultad facultad) {
        if (facultad == null) {
            throw new IllegalArgumentException("Faculty cannot be null");
        }
        
        executeInTransaction(session -> {
            Facultad f = (Facultad) session.get(Facultad.class, facultad.getIdFacultad());
            if (f != null) {
                session.delete(f);
            }
            return null;
        });
    }
}
