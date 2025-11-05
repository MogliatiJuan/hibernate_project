package infra.service;

import app.service.StudentService;
import model.Alumno;
import model.Ciudad;
import model.Materia;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public class HibernateStudentService extends BaseHibernateService implements StudentService {

    @Override
    public List<Alumno> list() {
        return executeInSession(session -> {
            return createQuery(session, "from Alumno a order by a.apellido, a.nombre", Alumno.class);
        });
    }

    @Override
    public Optional<Alumno> findById(Integer dni) {
        if (dni == null) {
            return Optional.empty();
        }
        return executeInSession(session -> {
            Alumno a = (Alumno) session.get(Alumno.class, dni);
            return Optional.ofNullable(a);
        });
    }

    @Override
    public Optional<Alumno> findByLastNameAndFirstName(String apellido, String nombre) {
        if (apellido == null || nombre == null || apellido.trim().isEmpty() || nombre.trim().isEmpty()) {
            return Optional.empty();
        }
        return executeInSession(session -> {
            List<Alumno> list = createQueryWithParams(session,
                "from Alumno a where a.apellido = :a and a.nombre = :n",
                Alumno.class,
                "a", apellido.trim(),
                "n", nombre.trim());
            if (list != null && !list.isEmpty()) {
                return Optional.of(list.get(0));
            }
            return Optional.empty();
        });
    }

    @Override
    public Alumno create(String apellido, String nombre, Integer dni, Date fechaNac, Integer idCiudad, Integer numLegajo, Integer anioIngreso, Integer idMateria) {
        if (apellido == null || nombre == null || dni == null || idCiudad == null || numLegajo == null || anioIngreso == null) {
            throw new IllegalArgumentException("Last name, first name, DNI, idCiudad, numLegajo and anioIngreso are required");
        }
        
        return executeInTransaction(session -> {
            Ciudad c = (Ciudad) session.get(Ciudad.class, idCiudad);
            if (c == null) {
                throw new IllegalArgumentException("City does not exist with id: " + idCiudad);
            }
            
            Materia m = null;
            if (idMateria != null) {
                m = (Materia) session.get(Materia.class, idMateria);
                // If it doesn't exist, create without subject (no error thrown)
            }
            
            Alumno a = new Alumno(apellido.trim(), nombre.trim(), dni, fechaNac, c, numLegajo, anioIngreso, null);
            if (m != null) {
                a.addMateria(m);
            }
            
            session.persist(a);
            return a;
        });
    }

    @Override
    public void update(Alumno alumno) {
        if (alumno == null || alumno.getDni() == null) {
            throw new IllegalArgumentException("Student cannot be null and must have DNI");
        }
        
        executeInTransaction(session -> {
            Alumno a = (Alumno) session.get(Alumno.class, alumno.getDni());
            if (a == null) {
                throw new IllegalArgumentException("Student not found with DNI: " + alumno.getDni());
            }
            
            a.setNumLegajo(alumno.getNumLegajo());
            a.setAnioIngreso(alumno.getAnioIngreso());
            if (alumno.getCiudad() != null) {
                Ciudad c = (Ciudad) session.get(Ciudad.class, alumno.getCiudad().getIdCiudad());
                if (c != null) {
                    a.setCiudad(c);
                }
            }
            
            // Update materias collection by syncing with the detached entity's materias
            // This is simplified - in a real scenario you might want more sophisticated merging
            a.getMaterias().clear();
            if (alumno.getMaterias() != null) {
                for (Materia m : alumno.getMaterias()) {
                    Materia materiaAttached = (Materia) session.get(Materia.class, m.getIdMateria());
                    if (materiaAttached != null) {
                        a.addMateria(materiaAttached);
                    }
                }
            }
            
            session.update(a);
            return null;
        });
    }

    @Override
    public void delete(Alumno alumno) {
        if (alumno == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }
        
        executeInTransaction(session -> {
            Alumno a = (Alumno) session.get(Alumno.class, alumno.getDni());
            if (a != null) {
                session.delete(a);
            }
            return null;
        });
    }

    @Override
    public void addSubject(Integer dniAlumno, Integer idMateria) {
        if (dniAlumno == null || idMateria == null) {
            throw new IllegalArgumentException("DNI and idMateria are required");
        }
        
        executeInTransaction(session -> {
            Alumno a = (Alumno) session.get(Alumno.class, dniAlumno);
            if (a == null) {
                throw new IllegalArgumentException("Student not found with DNI: " + dniAlumno);
            }
            
            Materia m = (Materia) session.get(Materia.class, idMateria);
            if (m == null) {
                throw new IllegalArgumentException("Subject not found with id: " + idMateria);
            }
            
            a.addMateria(m);
            session.update(a);
            return null;
        });
    }

    @Override
    public void removeSubject(Integer dniAlumno, Integer idMateria) {
        if (dniAlumno == null || idMateria == null) {
            throw new IllegalArgumentException("DNI and idMateria are required");
        }
        
        executeInTransaction(session -> {
            Alumno a = (Alumno) session.get(Alumno.class, dniAlumno);
            if (a == null) {
                throw new IllegalArgumentException("Student not found with DNI: " + dniAlumno);
            }
            
            Materia m = (Materia) session.get(Materia.class, idMateria);
            if (m == null) {
                throw new IllegalArgumentException("Subject not found with id: " + idMateria);
            }
            
            a.removeMateria(m);
            session.update(a);
            return null;
        });
    }
}
