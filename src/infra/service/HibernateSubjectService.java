package infra.service;

import app.service.SubjectService;
import model.Carrera;
import model.Materia;
import model.Profesor;

import java.util.List;
import java.util.Optional;

public class HibernateSubjectService extends BaseHibernateService implements SubjectService {

    @Override
    public List<Materia> list() {
        return executeInSession(session -> {
            return createQuery(session, "from Materia m order by m.idMateria", Materia.class);
        });
    }

    @Override
    public List<Materia> listByLevel(int nivel) {
        return executeInSession(session -> {
            org.hibernate.Query q = session.createQuery(
                    "from Materia m " +
                    "where m.nivel = :n " +
                    "order by m.orden asc, m.idMateria asc"
            );
            q.setInteger("n", nivel);
            @SuppressWarnings("unchecked")
            List<Materia> list = q.list();
            return list;
        });
    }

    @Override
    public Optional<Materia> findById(Integer id) {
        if (id == null) {
            return Optional.empty();
        }
        return executeInSession(session -> {
            Materia m = (Materia) session.get(Materia.class, id);
            return Optional.ofNullable(m);
        });
    }

    @Override
    public Optional<Materia> findByName(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return Optional.empty();
        }
        return executeInSession(session -> {
            List<Materia> list = createQuery(session, "from Materia m where m.nombre = :n", "n", nombre.trim(), Materia.class);
            if (list != null && !list.isEmpty()) {
                return Optional.of(list.get(0));
            }
            return Optional.empty();
        });
    }

    @Override
    public Materia create(String nombre, Integer nivel, Integer orden, Integer dniProfesor, Integer idCarrera) {
        if (nombre == null || nivel == null || orden == null || idCarrera == null) {
            throw new IllegalArgumentException("Name, level, order and idCarrera are required");
        }
        
        return executeInTransaction(session -> {
            Profesor p = null;
            if (dniProfesor != null) {
                p = (Profesor) session.get(Profesor.class, dniProfesor);
                // If it doesn't exist, create without professor (no error thrown)
            }

            Carrera c = (Carrera) session.get(Carrera.class, idCarrera);
            if (c == null) {
                throw new IllegalArgumentException("Career does not exist with id: " + idCarrera);
            }

            Materia m = new Materia(
                    nombre.trim(),
                    nivel,
                    orden,
                    p,
                    c,
                    null
            );

            session.persist(m);
            return m;
        });
    }

    @Override
    public void update(Materia materia) {
        if (materia == null || materia.getIdMateria() == null) {
            throw new IllegalArgumentException("Subject cannot be null and must have idMateria");
        }
        
        executeInTransaction(session -> {
            // Load the entity fresh to ensure it's attached
            Materia m = (Materia) session.get(Materia.class, materia.getIdMateria());
            if (m == null) {
                throw new IllegalArgumentException("Subject not found with id: " + materia.getIdMateria());
            }
            
            // Apply changes from the detached entity
            m.setNombre(materia.getNombre());
            m.setNivel(materia.getNivel());
            m.setOrden(materia.getOrden());
            
            // Handle Profesor: load fresh from database to attach to session
            if (materia.getProfesor() != null) {
                Profesor p = (Profesor) session.get(Profesor.class, materia.getProfesor().getDni());
                m.setProfesor(p); // p can be null if profesor was deleted, which is OK
            } else {
                m.setProfesor(null);
            }
            
            // Handle Carrera: load fresh from database to attach to session
            if (materia.getCarrera() != null) {
                Carrera c = (Carrera) session.get(Carrera.class, materia.getCarrera().getIdCarrera());
                if (c == null) {
                    throw new IllegalArgumentException("Career not found with id: " + materia.getCarrera().getIdCarrera());
                }
                m.setCarrera(c);
            } else {
                throw new IllegalArgumentException("Career is required for Subject");
            }
            
            // Save the attached entity
            session.update(m);
            return null;
        });
    }

    @Override
    public void delete(Materia materia) {
        if (materia == null) {
            throw new IllegalArgumentException("Subject cannot be null");
        }
        
        executeInTransaction(session -> {
            // Load the entity fresh to ensure it's attached
            Materia m = (Materia) session.get(Materia.class, materia.getIdMateria());
            if (m != null) {
                session.delete(m);
            }
            return null;
        });
    }
    
    // Helper methods for loading related entities (used by ABM)
    public Profesor findProfessorById(Integer dni) {
        if (dni == null) {
            return null;
        }
        return executeInSession(session -> {
            return (Profesor) session.get(Profesor.class, dni);
        });
    }
    
    public Carrera findCareerById(Integer idCarrera) {
        if (idCarrera == null) {
            return null;
        }
        return executeInSession(session -> {
            return (Carrera) session.get(Carrera.class, idCarrera);
        });
    }
}
