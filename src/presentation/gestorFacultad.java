package presentation;

import model.Alumno;
import model.Carrera;
import model.Ciudad;
import model.Facultad;
import model.Materia;
import model.Profesor;
import model.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.Date;

public class gestorFacultad {

    public static void main(String[] args) {
        Session ss = HibernateUtil.getSF().openSession();
        Transaction tx = ss.beginTransaction();
        try {
            // CIUDAD, FACULTAD, CARRERA
            Ciudad c1 = getOrCreateCiudad(ss, "Cordoba Capital");
            Facultad f1 = getOrCreateFacultad(ss, "Facultad de Sistemas", c1);
            Carrera ca1 = getOrCreateCarrera(ss, "Tecnicatura en TI", f1);

            // PROFESOR (JOINED) – merge evita duplicados por DNI
            Profesor p1 = new Profesor(
                    10, // antigüedad (años)
                    "Martinez", // apellido
                    "Carlos", // nombre
                    140559322, // DNI
                    "1970-12-19", // fecha nac (yyyy-MM-dd)  -> tu Profesor acepta String
                    c1 // ciudad
            );
            p1 = (Profesor) ss.merge(p1);

            // MATERIA – dueña de dniProfesor e idCarrera
            // firma actual: (nombre, nivel, orden, profesor, carrera, alumnos)
            Materia m1 = getOrCreateMateria(ss, "PAV", 3, 1, p1, ca1);

            // ALUMNO (JOINED) – anioIngreso = int, y relación muchos-a-muchos
            Alumno a1 = new Alumno(
                    "Dip Popich", // apellido
                    "Bruno", // nombre
                    39575877, // DNI
                    Date.valueOf("1996-04-02"), // fecha nac
                    c1, // ciudad
                    40, // num legajo
                    2020, // año de ingreso (int)
                    null // Set<Materia> (opcional)
            );
            a1.addMateria(m1);                  // mantiene ambos lados
            a1 = (Alumno) ss.merge(a1);         // idempotente por DNI

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
    private static Ciudad getOrCreateCiudad(Session ss, String nombre) {
        Ciudad c = (Ciudad) ss.createQuery(
                "from Ciudad c where c.nombre = :n")
                .setParameter("n", nombre)
                .uniqueResult();
        if (c == null) {
            c = new Ciudad(nombre);
            ss.save(c);
        }
        return c;
    }

    private static Facultad getOrCreateFacultad(Session ss, String nombre, Ciudad ciudad) {
        Facultad f = (Facultad) ss.createQuery(
                "from Facultad f where f.nombre = :n and f.ciudad.idCiudad = :cid")
                .setParameter("n", nombre)
                .setParameter("cid", ciudad.getIdCiudad())
                .uniqueResult();
        if (f == null) {
            f = new Facultad(nombre, ciudad);
            ss.save(f);
        }
        return f;
    }

    private static Carrera getOrCreateCarrera(Session ss, String nombre, Facultad fac) {
        Carrera c = (Carrera) ss.createQuery(
                "from Carrera c where c.nombre = :n and c.facultad.idFacultad = :fid")
                .setParameter("n", nombre)
                .setParameter("fid", fac.getIdFacultad())
                .uniqueResult();
        if (c == null) {
            // asegurate de tener el constructor (String, Facultad)
            c = new Carrera(nombre, fac);
            ss.save(c);
        }
        return c;
    }

    // firma nueva: nombre, nivel, orden, profesor, carrera
    private static Materia getOrCreateMateria(Session ss, String nombre, int nivel,
            Integer orden, Profesor prof, Carrera carr) {
        Materia m = (Materia) ss.createQuery(
                "from Materia m where m.nombre = :n and m.carrera.idCarrera = :cid")
                .setParameter("n", nombre)
                .setParameter("cid", carr.getIdCarrera())
                .uniqueResult();

        if (m == null) {
            m = new Materia(nombre, nivel, orden, prof, carr, null);
            ss.save(m);
        } else {
            // idempotencia: si existe, actualizamos datos “volátiles”
            m.setNivel(nivel);
            m.setOrden(orden);
            m.setProfesor(prof);   // puede ser null
            m.setCarrera(carr);
            ss.merge(m);
        }
        return m;
    }
}
