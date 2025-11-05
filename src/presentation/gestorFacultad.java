package presentation;

import model.Student;
import model.Career;
import model.City;
import model.Faculty;
import model.Subject;
import model.Professor;
import model.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.Date;

public class gestorFaculty {

    public static void main(String[] args) {
        Session ss = HibernateUtil.getSF().openSession();
        Transaction tx = ss.beginTransaction();
        try {
            // CIUDAD, FACULTAD, CARRERA
            City c1 = getOrCreateCity(ss, "Cordoba Capital");
            Faculty f1 = getOrCreateFaculty(ss, "Faculty de Sistemas", c1);
            Career ca1 = getOrCreateCareer(ss, "Tecnicatura en TI", f1);

            // PROFESOR (JOINED) – merge evita duplicados por DNI
            Professor p1 = new Professor(
                    10, // antigüedad (años)
                    "Martinez", // apellido
                    "Carlos", // nombre
                    140559322, // DNI
                    "1970-12-19", // fecha nac (yyyy-MM-dd)  -> tu Professor acepta String
                    c1 // ciudad
            );
            p1 = (Professor) ss.merge(p1);

            // MATERIA – dueña de dniProfessor e idCareer
            // firma actual: (nombre, nivel, orden, profesor, carrera, alumnos)
            Subject m1 = getOrCreateSubject(ss, "PAV", 3, 1, p1, ca1);

            // ALUMNO (JOINED) – anioIngreso = int, y relación muchos-a-muchos
            Student a1 = new Student(
                    "Dip Popich", // apellido
                    "Bruno", // nombre
                    39575877, // DNI
                    Date.valueOf("1996-04-02"), // fecha nac
                    c1, // ciudad
                    40, // num legajo
                    2020, // año de ingreso (int)
                    null // Set<Subject> (opcional)
            );
            a1.addSubject(m1);                  // mantiene ambos lados
            a1 = (Student) ss.merge(a1);         // idempotente por DNI

            tx.commit();
            System.out.println("Semilla ejecutada correctamente (idempotente).");

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            ss.close();
        }
    }

    // ----------------- Helpers Get-or-Create -----------------
    private static City getOrCreateCity(Session ss, String nombre) {
        City c = (City) ss.createQuery(
                "from City c where c.nombre = :n")
                .setParameter("n", nombre)
                .uniqueResult();
        if (c == null) {
            c = new City(nombre);
            ss.save(c);
        }
        return c;
    }

    private static Faculty getOrCreateFaculty(Session ss, String nombre, City ciudad) {
        Faculty f = (Faculty) ss.createQuery(
                "from Faculty f where f.nombre = :n and f.ciudad.idCity = :cid")
                .setParameter("n", nombre)
                .setParameter("cid", ciudad.getIdCity())
                .uniqueResult();
        if (f == null) {
            f = new Faculty(nombre, ciudad);
            ss.save(f);
        }
        return f;
    }

    private static Career getOrCreateCareer(Session ss, String nombre, Faculty fac) {
        Career c = (Career) ss.createQuery(
                "from Career c where c.nombre = :n and c.facultad.idFaculty = :fid")
                .setParameter("n", nombre)
                .setParameter("fid", fac.getIdFaculty())
                .uniqueResult();
        if (c == null) {
            // asegurate de tener el constructor (String, Faculty)
            c = new Career(nombre, fac);
            ss.save(c);
        }
        return c;
    }

    // firma nueva: nombre, nivel, orden, profesor, carrera
    private static Subject getOrCreateSubject(Session ss, String nombre, int nivel,
            Integer orden, Professor prof, Career carr) {
        Subject m = (Subject) ss.createQuery(
                "from Subject m where m.nombre = :n and m.carrera.idCareer = :cid")
                .setParameter("n", nombre)
                .setParameter("cid", carr.getIdCareer())
                .uniqueResult();

        if (m == null) {
            m = new Subject(nombre, nivel, orden, prof, carr, null);
            ss.save(m);
        } else {
            // idempotencia: si existe, actualizamos datos “volátiles”
            m.setNivel(nivel);
            m.setOrden(orden);
            m.setProfessor(prof);   // puede ser null
            m.setCareer(carr);
            ss.merge(m);
        }
        return m;
    }
}
