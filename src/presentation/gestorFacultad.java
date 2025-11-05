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
            City c1 = getOrCreateCity(ss, "Cordoba Capital");
            Faculty f1 = getOrCreateFaculty(ss, "Faculty de Sistemas", c1);
            Career ca1 = getOrCreateCareer(ss, "Tecnicatura en TI", f1);

            Professor p1 = new Professor(
                    10,
                    "Martinez",
                    "Carlos",
                    140559322,
                    "1970-12-19",
                    c1
            );
            p1 = (Professor) ss.merge(p1);

            Subject m1 = getOrCreateSubject(ss, "PAV", 3, 1, p1, ca1);

            Student a1 = new Student(
                    "Dip Popich",
                    "Bruno",
                    39575877,
                    Date.valueOf("1996-04-02"),
                    c1,
                    40,
                    2020,
                    null
            );
            a1.addSubject(m1);
            a1 = (Student) ss.merge(a1);

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
            c = new Career(nombre, fac);
            ss.save(c);
        }
        return c;
    }

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
            m.setNivel(nivel);
            m.setOrden(orden);
            m.setProfessor(prof);
            m.setCareer(carr);
            ss.merge(m);
        }
        return m;
    }
}
