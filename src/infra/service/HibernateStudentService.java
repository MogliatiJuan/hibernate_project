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
            // Use join fetch to eagerly load subjects collection to avoid lazy initialization errors
            List<Student> students = createQuery(session, 
                "select distinct a from Student a left join fetch a.subjects order by a.lastName, a.firstName", 
                Student.class);
            return students;
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
    public Optional<Student> findByLastNameAndFirstName(String lastName, String firstName) {
        if (lastName == null || firstName == null || lastName.trim().isEmpty() || firstName.trim().isEmpty()) {
            return Optional.empty();
        }
        return executeInSession(session -> {
            List<Student> list = createQueryWithParams(session,
                "from Student a where a.lastName = :a and a.firstName = :n",
                Student.class,
                "a", lastName.trim(),
                "n", firstName.trim());
            if (list != null && !list.isEmpty()) {
                return Optional.of(list.get(0));
            }
            return Optional.empty();
        });
    }

    @Override
    public Student create(String lastName, String firstName, Integer dni, Date birthDate, Integer idCity, Integer studentNumber, Integer enrollmentYear, Integer idSubject) {
        if (lastName == null || firstName == null || dni == null || idCity == null || studentNumber == null || enrollmentYear == null) {
            throw new IllegalArgumentException("Last name, first name, DNI, idCity, studentNumber and enrollmentYear are required");
        }
        
        return executeInTransaction(session -> {
            City c = (City) session.get(City.class, idCity);
            if (c == null) {
                throw new IllegalArgumentException("City does not exist with id: " + idCity);
            }
            
            Subject m = null;
            if (idSubject != null) {
                m = (Subject) session.get(Subject.class, idSubject);
                if (m == null) {
                    throw new IllegalArgumentException("Subject does not exist with id: " + idSubject);
                }
            }
            
            Student a = new Student(lastName.trim(), firstName.trim(), dni, birthDate, c, studentNumber, enrollmentYear, null);
            if (m != null) {
                a.addSubject(m);
            }
            
            session.persist(a);
            return a;
        });
    }

    @Override
    public void update(Student student) {
        if (student == null || student.getDni() == null) {
            throw new IllegalArgumentException("Student cannot be null and must have DNI");
        }
        
        executeInTransaction(session -> {
            // Use HQL with join fetch to eagerly load subjects collection
            org.hibernate.Query q = session.createQuery(
                "select distinct a from Student a left join fetch a.subjects where a.dni = :dni");
            q.setInteger("dni", student.getDni());
            @SuppressWarnings("unchecked")
            List<Student> students = (List<Student>) q.list();
            
            Student a = null;
            if (students != null && !students.isEmpty()) {
                a = students.get(0);
            }
            
            if (a == null) {
                throw new IllegalArgumentException("Student not found with DNI: " + student.getDni());
            }
            
            a.setStudentNumber(student.getStudentNumber());
            a.setEnrollmentYear(student.getEnrollmentYear());
            if (student.getCity() != null) {
                City c = (City) session.get(City.class, student.getCity().getIdCity());
                if (c != null) {
                    a.setCity(c);
                }
            }
            
            // Now subjects collection is already loaded, safe to access
            a.getSubjects().clear();
            if (student.getSubjects() != null) {
                for (Subject m : student.getSubjects()) {
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
    public void delete(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }
        
        executeInTransaction(session -> {
            Student a = (Student) session.get(Student.class, student.getDni());
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
