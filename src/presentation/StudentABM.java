package presentation;

import app.service.StudentService;
import model.Student;
import model.City;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

public class StudentABM extends JFrame {

    private final JTextArea out = new JTextArea(18, 60);
    private final StudentService studentService;

    public StudentABM(StudentService studentService) {
        this.studentService = studentService;
        initComponents();
        refreshList();
    }

    private void initComponents() {
        setTitle("STUDENT – CRUD");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        top.add(btn("Create", e -> create()));
        top.add(btn("Update", e -> update()));
        top.add(btn("Delete", e -> delete()));
        top.add(btn("List", e -> showList()));
        top.add(btn("Close", e -> dispose()));
        add(top, BorderLayout.NORTH);

        out.setEditable(false);
        add(new JScrollPane(out), BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }

    public StudentABM(StudentService studentService, String action) {
        this(studentService);
        setTitle(getTitle() + " - " + action);

        SwingUtilities.invokeLater(() -> {
            switch (action) {
                case "Create":
                    create();
                    break;
                case "Update":
                    update();
                    break;
                case "Delete":
                    delete();
                    break;
                case "List":
                    showList();
                    break;
                default:
                    break;
            }
        });
    }

    private JButton btn(String t, java.awt.event.ActionListener al) {
        JButton b = new JButton(t);
        b.addActionListener(al);
        return b;
    }

    private void create() {
        String dni = JOptionPane.showInputDialog(this, "DNI (PK):");
        String apellido = JOptionPane.showInputDialog(this, "Last name:");
        String nombre = JOptionPane.showInputDialog(this, "First name:");
        String fechaNac = JOptionPane.showInputDialog(this, "Birth date (yyyy-MM-dd):");
        String idCity = JOptionPane.showInputDialog(this, "idCity:");
        String legajo = JOptionPane.showInputDialog(this, "Student number (int):");
        String anioIng = JOptionPane.showInputDialog(this, "Enrollment year (int):");
        String idMateria = JOptionPane.showInputDialog(this, "idMateria (optional):");

        if (dni == null || apellido == null || nombre == null || fechaNac == null
                || idCity == null || legajo == null || anioIng == null) {
            return;
        }

        if (dni.trim().isEmpty() || idCity.trim().isEmpty()
                || legajo.trim().isEmpty() || anioIng.trim().isEmpty()) {
            out.append("✖ Numeric fields are required\n");
            return;
        }

        try {
            out.setText("");
            Integer idMateriaInt = null;
            if (idMateria != null && !idMateria.trim().isEmpty()) {
                idMateriaInt = Integer.parseInt(idMateria.trim());
            }

            Student a = studentService.create(
                    apellido.trim(),
                    nombre.trim(),
                    Integer.parseInt(dni.trim()),
                    Date.valueOf(fechaNac.trim().replace('/', '-')),
                    Integer.parseInt(idCity.trim()),
                    Integer.parseInt(legajo.trim()),
                    Integer.parseInt(anioIng.trim()),
                    idMateriaInt
            );
            out.append("✔ Student created -> dni=" + a.getDni() + " | student number=" + a.getStudentNumber() + "\n");
            refreshList();
        } catch (Exception e) {
            out.append("✖ ERROR: " + e.getMessage() + "\n");
        }
    }

    private void update() {
        String dni = JOptionPane.showInputDialog(this, "Student DNI (Enter to search by LastName+FirstName):");
        try {
            out.setText("");
            Optional<Student> optStudent;
            if (dni != null && !dni.trim().isEmpty()) {
                optStudent = studentService.findById(Integer.parseInt(dni.trim()));
            } else {
                String ape = JOptionPane.showInputDialog(this, "Exact last name:");
                String nom = JOptionPane.showInputDialog(this, "Exact first name:");
                if (ape == null || nom == null) {
                    return;
                }
                optStudent = studentService.findByLastNameAndFirstName(ape.trim(), nom.trim());
            }
            
            if (!optStudent.isPresent()) {
                out.append("✖ Student does not exist\n");
                return;
            }

            Student a = optStudent.get();
            String legajo = JOptionPane.showInputDialog(this, "New student number:", String.valueOf(a.getStudentNumber()));
            String anioIng = JOptionPane.showInputDialog(this, "New enrollment year (int, Enter to keep):");
            String idCity = JOptionPane.showInputDialog(this, "New idCity (Enter to keep):");
            String addMateria = JOptionPane.showInputDialog(this, "Add idMateria (Enter to skip):");
            String delMateria = JOptionPane.showInputDialog(this, "Remove idMateria (Enter to skip):");

            if (legajo != null && !legajo.trim().isEmpty()) {
                a.setStudentNumber(Integer.parseInt(legajo.trim()));
            }
            if (anioIng != null && !anioIng.trim().isEmpty()) {
                a.setEnrollmentYear(Integer.parseInt(anioIng.trim()));
            }
            if (idCity != null && !idCity.trim().isEmpty()) {
                City c = new City();
                c.setIdCity(Integer.parseInt(idCity.trim()));
                a.setCity(c);
            }
            
            studentService.update(a);
            
            if (addMateria != null && !addMateria.trim().isEmpty()) {
                try {
                    studentService.addSubject(a.getDni(), Integer.parseInt(addMateria.trim()));
                } catch (Exception e) {
                    out.append("⚠ Error adding subject: " + e.getMessage() + "\n");
                }
            }
            if (delMateria != null && !delMateria.trim().isEmpty()) {
                try {
                    studentService.removeSubject(a.getDni(), Integer.parseInt(delMateria.trim()));
                } catch (Exception e) {
                    out.append("⚠ Error removing subject: " + e.getMessage() + "\n");
                }
            }

            out.append("✔ Student updated -> dni=" + a.getDni() + "\n");
            refreshList();
        } catch (Exception e) {
            out.append("✖ ERROR: " + e.getMessage() + "\n");
        }
    }

    private void delete() {
        String dni = JOptionPane.showInputDialog(this, "DNI to delete:");
        if (dni == null || dni.trim().isEmpty()) {
            return;
        }

        try {
            out.setText("");
            Optional<Student> optStudent = studentService.findById(Integer.parseInt(dni.trim()));
            if (!optStudent.isPresent()) {
                out.append("✖ Does not exist\n");
                return;
            }
            
            Student a = optStudent.get();
            studentService.delete(a);
            out.append("✔ Student deleted\n");
            refreshList();
        } catch (Exception e) {
            out.append("✖ ERROR (FKs?): " + e.getMessage() + "\n");
        }
    }

    private void showList() {
        refreshList();
    }
    
    private void refreshList() {
        try {
            out.setText("");
            List<Student> alumnos = studentService.list();
            out.append("STUDENTS:\n");
            if (alumnos.isEmpty()) {
                out.append("  (No students yet)\n");
            } else {
                for (Student a : alumnos) {
                    out.append(" - dni=" + a.getDni()
                            + " | " + a.getLastName() + ", " + a.getFirstName()
                            + " | student number=" + a.getStudentNumber()
                            + " | enrollment year=" + a.getEnrollmentYear()
                            + " | idCity=" + (a.getCity() != null ? a.getCity().getIdCity() : null)
                            + " | subjects=" + (a.getSubjects() != null ? a.getSubjects().size() : 0)
                            + "\n");
                }
            }
        } catch (Exception e) {
            out.append("✖ ERROR: " + e.getMessage() + "\n");
        }
    }
}
