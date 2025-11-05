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
    public List<Subject> listByLevel(int level) {
        return executeInSession(session -> {
            org.hibernate.Query q = session.createQuery(
                    "from Subject m " +
                    "where m.level = :n " +
                    "order by m.order asc, m.idSubject asc"
            );
            q.setInteger("n", level);
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
    public Optional<Subject> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return Optional.empty();
        }
        return executeInSession(session -> {
            List<Subject> list = createQuery(session, "from Subject m where m.name = :n", "n", name.trim(), Subject.class);
            if (list != null && !list.isEmpty()) {
                return Optional.of(list.get(0));
            }
            return Optional.empty();
        });
    }

    @Override
    public Subject create(String name, Integer level, Integer order, Integer dniProfessor, Integer idCareer) {
        if (name == null || level == null || order == null || idCareer == null) {
            throw new IllegalArgumentException("Name, level, order and idCareer are required");
        }
        
        return executeInTransaction(session -> {
            Professor p = null;
            if (dniProfessor != null) {
                p = (Professor) session.get(Professor.class, dniProfessor);
            }

            Career c = (Career) session.get(Career.class, idCareer);
            if (c == null) {
                throw new IllegalArgumentException("Career does not exist with id: " + idCareer);
            }

            Subject m = new Subject(
                    name.trim(),
                    level,
                    order,
                    p,
                    c,
                    null
            );

            session.persist(m);
            return m;
        });
    }

    @Override
    public void update(Subject subject) {
        if (subject == null || subject.getIdSubject() == null) {
            throw new IllegalArgumentException("Subject cannot be null and must have idSubject");
        }
        
        executeInTransaction(session -> {
            Subject m = (Subject) session.get(Subject.class, subject.getIdSubject());
            if (m == null) {
                throw new IllegalArgumentException("Subject not found with id: " + subject.getIdSubject());
            }
            
            m.setName(subject.getName());
            m.setLevel(subject.getLevel());
            m.setOrder(subject.getOrder());
            
            if (subject.getProfessor() != null) {
                Professor p = (Professor) session.get(Professor.class, subject.getProfessor().getDni());
                m.setProfessor(p);
            } else {
                m.setProfessor(null);
            }
            
            if (subject.getCareer() != null) {
                Career c = (Career) session.get(Career.class, subject.getCareer().getIdCareer());
                if (c == null) {
                    throw new IllegalArgumentException("Career not found with id: " + subject.getCareer().getIdCareer());
                }
                m.setCareer(c);
            } else {
                throw new IllegalArgumentException("Career is required for Subject");
            }
            
            session.update(m);
            return null;
        });
    }

    @Override
    public void delete(Subject subject) {
        if (subject == null) {
            throw new IllegalArgumentException("Subject cannot be null");
        }
        
        executeInTransaction(session -> {
            Subject m = (Subject) session.get(Subject.class, subject.getIdSubject());
            if (m != null) {
                session.delete(m);
            }
            return null;
        });
    }
    
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
