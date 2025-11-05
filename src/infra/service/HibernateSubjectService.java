package infra.service;

import app.service.SubjectService;
import model.Career;
import model.Subject;
import model.Professor;

import java.util.List;
import java.util.Optional;

public class HibernateSubjectService extends BaseHibernateService implements SubjectService {

    @Override
    public List<Subject> list() {
        return executeInSession(session -> {
            return createQuery(session, "from Subject m order by m.idSubject", Subject.class);
        });
    }

    @Override
    public List<Subject> listByLevel(int nivel) {
        return executeInSession(session -> {
            org.hibernate.Query q = session.createQuery(
                    "from Subject m " +
                    "where m.nivel = :n " +
                    "order by m.orden asc, m.idSubject asc"
            );
            q.setInteger("n", nivel);
            @SuppressWarnings("unchecked")
            List<Subject> list = q.list();
            return list;
        });
    }

    @Override
    public Optional<Subject> findById(Integer id) {
        if (id == null) {
            return Optional.empty();
        }
        return executeInSession(session -> {
            Subject m = (Subject) session.get(Subject.class, id);
            return Optional.ofNullable(m);
        });
    }

    @Override
    public Optional<Subject> findByName(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return Optional.empty();
        }
        return executeInSession(session -> {
            List<Subject> list = createQuery(session, "from Subject m where m.nombre = :n", "n", nombre.trim(), Subject.class);
            if (list != null && !list.isEmpty()) {
                return Optional.of(list.get(0));
            }
            return Optional.empty();
        });
    }

    @Override
    public Subject create(String nombre, Integer nivel, Integer orden, Integer dniProfessor, Integer idCareer) {
        if (nombre == null || nivel == null || orden == null || idCareer == null) {
            throw new IllegalArgumentException("Name, level, order and idCareer are required");
        }
        
        return executeInTransaction(session -> {
            Professor p = null;
            if (dniProfessor != null) {
                p = (Professor) session.get(Professor.class, dniProfessor);
                // If it doesn't exist, create without professor (no error thrown)
            }

            Career c = (Career) session.get(Career.class, idCareer);
            if (c == null) {
                throw new IllegalArgumentException("Career does not exist with id: " + idCareer);
            }

            Subject m = new Subject(
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
    public void update(Subject materia) {
        if (materia == null || materia.getIdSubject() == null) {
            throw new IllegalArgumentException("Subject cannot be null and must have idSubject");
        }
        
        executeInTransaction(session -> {
            // Load the entity fresh to ensure it's attached
            Subject m = (Subject) session.get(Subject.class, materia.getIdSubject());
            if (m == null) {
                throw new IllegalArgumentException("Subject not found with id: " + materia.getIdSubject());
            }
            
            // Apply changes from the detached entity
            m.setNombre(materia.getNombre());
            m.setNivel(materia.getNivel());
            m.setOrden(materia.getOrden());
            
            // Handle Professor: load fresh from database to attach to session
            if (materia.getProfessor() != null) {
                Professor p = (Professor) session.get(Professor.class, materia.getProfessor().getDni());
                m.setProfessor(p); // p can be null if profesor was deleted, which is OK
            } else {
                m.setProfessor(null);
            }
            
            // Handle Career: load fresh from database to attach to session
            if (materia.getCareer() != null) {
                Career c = (Career) session.get(Career.class, materia.getCareer().getIdCareer());
                if (c == null) {
                    throw new IllegalArgumentException("Career not found with id: " + materia.getCareer().getIdCareer());
                }
                m.setCareer(c);
            } else {
                throw new IllegalArgumentException("Career is required for Subject");
            }
            
            // Save the attached entity
            session.update(m);
            return null;
        });
    }

    @Override
    public void delete(Subject materia) {
        if (materia == null) {
            throw new IllegalArgumentException("Subject cannot be null");
        }
        
        executeInTransaction(session -> {
            // Load the entity fresh to ensure it's attached
            Subject m = (Subject) session.get(Subject.class, materia.getIdSubject());
            if (m != null) {
                session.delete(m);
            }
            return null;
        });
    }
    
    // Helper methods for loading related entities (used by ABM)
    public Professor findProfessorById(Integer dni) {
        if (dni == null) {
            return null;
        }
        return executeInSession(session -> {
            return (Professor) session.get(Professor.class, dni);
        });
    }
    
    public Career findCareerById(Integer idCareer) {
        if (idCareer == null) {
            return null;
        }
        return executeInSession(session -> {
            return (Career) session.get(Career.class, idCareer);
        });
    }
}
