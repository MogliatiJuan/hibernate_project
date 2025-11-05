package infra.service;

import app.service.ProfessorService;
import model.Ciudad;
import model.Profesor;

import java.util.List;
import java.util.Optional;

public class HibernateProfessorService extends BaseHibernateService implements ProfessorService {

    @Override
    public List<Profesor> list() {
        return executeInSession(session -> {
            return createQuery(session, "from Profesor p order by p.antiguedad desc", Profesor.class);
        });
    }

    @Override
    public Optional<Profesor> findById(Integer dni) {
        if (dni == null) {
            return Optional.empty();
        }
        return executeInSession(session -> {
            Profesor p = (Profesor) session.get(Profesor.class, dni);
            return Optional.ofNullable(p);
        });
    }

    @Override
    public Optional<Profesor> findByLastNameAndFirstName(String apellido, String nombre) {
        if (apellido == null || nombre == null || apellido.trim().isEmpty() || nombre.trim().isEmpty()) {
            return Optional.empty();
        }
        return executeInSession(session -> {
            List<Profesor> list = createQueryWithParams(session, 
                "from Profesor p where p.apellido = :a and p.nombre = :n", 
                Profesor.class,
                "a", apellido.trim(),
                "n", nombre.trim());
            if (list != null && !list.isEmpty()) {
                return Optional.of(list.get(0));
            }
            return Optional.empty();
        });
    }

    @Override
    public Profesor create(String apellido, String nombre, Integer dni, String fechaNac, Integer antiguedad, Integer idCiudad) {
        if (apellido == null || nombre == null || dni == null || antiguedad == null || idCiudad == null) {
            throw new IllegalArgumentException("Last name, first name, DNI, seniority and idCiudad are required");
        }
        
        return executeInTransaction(session -> {
            Ciudad c = (Ciudad) session.get(Ciudad.class, idCiudad);
            if (c == null) {
                throw new IllegalArgumentException("City does not exist with id: " + idCiudad);
            }
            
            Profesor p = new Profesor(antiguedad, apellido.trim(), nombre.trim(), dni, 
                    fechaNac != null ? fechaNac.trim() : null, c);
            session.persist(p);
            return p;
        });
    }

    @Override
    public void update(Profesor profesor) {
        if (profesor == null || profesor.getDni() == null) {
            throw new IllegalArgumentException("Professor cannot be null and must have DNI");
        }
        
        executeInTransaction(session -> {
            Profesor p = (Profesor) session.get(Profesor.class, profesor.getDni());
            if (p == null) {
                throw new IllegalArgumentException("Professor not found with DNI: " + profesor.getDni());
            }
            
            p.setAntiguedad(profesor.getAntiguedad());
            if (profesor.getCiudad() != null) {
                Ciudad c = (Ciudad) session.get(Ciudad.class, profesor.getCiudad().getIdCiudad());
                if (c != null) {
                    p.setCiudad(c);
                }
            }
            
            session.update(p);
            return null;
        });
    }

    @Override
    public void delete(Profesor profesor) {
        if (profesor == null) {
            throw new IllegalArgumentException("Professor cannot be null");
        }
        
        executeInTransaction(session -> {
            Profesor p = (Profesor) session.get(Profesor.class, profesor.getDni());
            if (p != null) {
                session.delete(p);
            }
            return null;
        });
    }
}
