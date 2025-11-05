package infra.service;

import app.service.StudentService;
import model.Student;
import model.City;
import model.Subject;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public class HibernateStudentService extends BaseHibernateService implements StudentService {

    @Override
    public List<Student> list() {
        return executeInSession(session -> {
            return createQuery(session, "from Student a order by a.apellido, a.nombre", Student.class);
        });
    }

    @Override
    public Optional<Student> findById(Integer dni) {
        if (dni == null) {
            return Optional.empty();
        }
        return executeInSession(session -> {
            Student a = (Student) session.get(Student.class, dni);
            return Optional.ofNullable(a);
        });
    }

    @Override
    public Optional<Student> findByLastNameAndFirstName(String apellido, String nombre) {
        if (apellido == null || nombre == null || apellido.trim().isEmpty() || nombre.trim().isEmpty()) {
            return Optional.empty();
        }
        return executeInSession(session -> {
            List<Student> list = createQueryWithParams(session,
                "from Student a where a.apellido = :a and a.nombre = :n",
                Student.class,
                "a", apellido.trim(),
                "n", nombre.trim());
            if (list != null && !list.isEmpty()) {
                return Optional.of(list.get(0));
            }
            return Optional.empty();
        });
    }

    @Override
    public Student create(String apellido, String nombre, Integer dni, Date fechaNac, Integer idCity, Integer numLegajo, Integer anioIngreso, Integer idSubject) {
        if (apellido == null || nombre == null || dni == null || idCity == null || numLegajo == null || anioIngreso == null) {
            throw new IllegalArgumentException("Last name, first name, DNI, idCity, numLegajo and anioIngreso are required");
        }
        
        return executeInTransaction(session -> {
            City c = (City) session.get(City.class, idCity);
            if (c == null) {
                throw new IllegalArgumentException("City does not exist with id: " + idCity);
            }
            
            Subject m = null;
            if (idSubject != null) {
                m = (Subject) session.get(Subject.class, idSubject);
            }
            
            Student a = new Student(apellido.trim(), nombre.trim(), dni, fechaNac, c, numLegajo, anioIngreso, null);
            if (m != null) {
                a.addSubject(m);
            }
            
            session.persist(a);
            return a;
        });
    }

    @Override
    public void update(Student alumno) {
        if (alumno == null || alumno.getDni() == null) {
            throw new IllegalArgumentException("Student cannot be null and must have DNI");
        }
        
        executeInTransaction(session -> {
            Student a = (Student) session.get(Student.class, alumno.getDni());
            if (a == null) {
                throw new IllegalArgumentException("Student not found with DNI: " + alumno.getDni());
            }
            
            a.setNumLegajo(alumno.getNumLegajo());
            a.setAnioIngreso(alumno.getAnioIngreso());
            if (alumno.getCity() != null) {
                City c = (City) session.get(City.class, alumno.getCity().getIdCity());
                if (c != null) {
                    a.setCity(c);
                }
            }
            
            a.getSubjects().clear();
            if (alumno.getSubjects() != null) {
                for (Subject m : alumno.getSubjects()) {
                    Subject materiaAttached = (Subject) session.get(Subject.class, m.getIdSubject());
                    if (materiaAttached != null) {
                        a.addSubject(materiaAttached);
                    }
                }
            }
            
            session.update(a);
            return null;
        });
    }

    @Override
    public void delete(Student alumno) {
        if (alumno == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }
        
        executeInTransaction(session -> {
            Student a = (Student) session.get(Student.class, alumno.getDni());
            if (a != null) {
                session.delete(a);
            }
            return null;
        });
    }

    @Override
    public void addSubject(Integer dniStudent, Integer idSubject) {
        if (dniStudent == null || idSubject == null) {
            throw new IllegalArgumentException("DNI and idSubject are required");
        }
        
        executeInTransaction(session -> {
            Student a = (Student) session.get(Student.class, dniStudent);
            if (a == null) {
                throw new IllegalArgumentException("Student not found with DNI: " + dniStudent);
            }
            
            Subject m = (Subject) session.get(Subject.class, idSubject);
            if (m == null) {
                throw new IllegalArgumentException("Subject not found with id: " + idSubject);
            }
            
            a.addSubject(m);
            session.update(a);
            return null;
        });
    }

    @Override
    public void removeSubject(Integer dniStudent, Integer idSubject) {
        if (dniStudent == null || idSubject == null) {
            throw new IllegalArgumentException("DNI and idSubject are required");
        }
        
        executeInTransaction(session -> {
            Student a = (Student) session.get(Student.class, dniStudent);
            if (a == null) {
                throw new IllegalArgumentException("Student not found with DNI: " + dniStudent);
            }
            
            Subject m = (Subject) session.get(Subject.class, idSubject);
            if (m == null) {
                throw new IllegalArgumentException("Subject not found with id: " + idSubject);
            }
            
            a.removeSubject(m);
            session.update(a);
            return null;
        });
    }
}
