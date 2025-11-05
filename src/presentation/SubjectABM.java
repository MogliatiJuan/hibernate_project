package presentation;

import app.service.SubjectService;
import infra.service.HibernateSubjectService;
import model.Career;
import model.Subject;
import model.Professor;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Optional;

public class SubjectABM extends JFrame {

    private final JTextArea out = new JTextArea(18, 60);
    private final SubjectService subjectService;

    public SubjectABM(SubjectService subjectService) {
        this.subjectService = subjectService;
        initComponents();
        refreshList();
    }

    private void initComponents() {
        setTitle("SUBJECT – CRUD");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        top.add(btn("Create", e -> create()));
        top.add(btn("Update", e -> update()));
        top.add(btn("Delete", e -> delete()));
        top.add(btn("List", e -> showList()));
        top.add(btn("List by Level", e -> listByLevelDialog()));
        top.add(btn("Close", e -> dispose()));
        add(top, BorderLayout.NORTH);

        out.setEditable(false);
        add(new JScrollPane(out), BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }

    public SubjectABM(SubjectService subjectService, String action) {
        this(subjectService);
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
                case "ListByLevel":
                    listByLevelDialog();
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
        String nombre = JOptionPane.showInputDialog(this, "Name:");
        String nivel = JOptionPane.showInputDialog(this, "Level (int):");
        String orden = JOptionPane.showInputDialog(this, "Order (int):");
        String dniProf = JOptionPane.showInputDialog(this, "Professor DNI:");
        String idCareer = JOptionPane.showInputDialog(this, "idCareer:");
        if (nombre == null || nivel == null || orden == null || idCareer == null) {
            return;
        }
        if (nombre.trim().isEmpty() || nivel.trim().isEmpty() || orden.trim().isEmpty() || idCareer.trim().isEmpty()) {
            out.append("✖ Name, level, order and idCareer are required\n");
            return;
        }

        try {
            out.setText("");
            Integer dniProfessor = null;
            if (dniProf != null && !dniProf.trim().isEmpty()) {
                dniProfessor = Integer.parseInt(dniProf.trim());
            }

            Subject m = subjectService.create(
                    nombre.trim(),
                    Integer.parseInt(nivel.trim()),
                    Integer.parseInt(orden.trim()),
                    dniProfessor,
                    Integer.parseInt(idCareer.trim())
            );

            out.append("✔ Subject created -> id=" + m.getIdSubject() + ", name=" + m.getNombre()
                    + " | level=" + m.getNivel() + " | prof=" + (m.getProfessor() != null ? m.getProfessor().getDni() : null) + " | idCareer=" + m.getCareer().getIdCareer() + "\n");
            refreshList();
        } catch (Exception e) {
            out.append("✖ ERROR: " + e.getMessage() + "\n");
        }
    }

    private void update() {
        String idStr = JOptionPane.showInputDialog(this, "idSubject (Enter to search by name):");
        try {
            out.setText("");
            Optional<Subject> optSubject;
            if (idStr != null && !idStr.trim().isEmpty()) {
                optSubject = subjectService.findById(Integer.parseInt(idStr.trim()));
            } else {
                String nombre = JOptionPane.showInputDialog(this, "Exact name to update:");
                if (nombre == null) {
                    return;
                }
                optSubject = subjectService.findByName(nombre.trim());
            }
            
            if (!optSubject.isPresent()) {
                out.append("✖ Subject does not exist\n");
                return;
            }

            Subject m = optSubject.get();
            String nuevoNom = JOptionPane.showInputDialog(this, "New name:", m.getNombre());
            String nivel = JOptionPane.showInputDialog(this, "New level (int):", String.valueOf(m.getNivel()));
            String orden = JOptionPane.showInputDialog(this, "New order (int):", String.valueOf(m.getOrden() != null ? m.getOrden() : 0));
            String dniProf = JOptionPane.showInputDialog(this, "New professor DNI (Enter to keep):");
            String idCareer = JOptionPane.showInputDialog(this, "New idCareer (Enter to keep):");

            if (nuevoNom != null && !nuevoNom.trim().isEmpty()) {
                m.setNombre(nuevoNom.trim());
            }
            if (nivel != null && !nivel.trim().isEmpty()) {
                m.setNivel(Integer.parseInt(nivel.trim()));
            }
            if (orden != null && !orden.trim().isEmpty()) {
                m.setOrden(Integer.parseInt(orden.trim()));
            }

            if (dniProf != null && !dniProf.trim().isEmpty()) {
                if (subjectService instanceof HibernateSubjectService) {
                    HibernateSubjectService hibernateService = (HibernateSubjectService) subjectService;
                    Professor p = hibernateService.findProfessorById(Integer.parseInt(dniProf.trim()));
                    if (p != null) {
                        m.setProfessor(p);
                    }
                }
            }
            if (idCareer != null && !idCareer.trim().isEmpty()) {
                if (subjectService instanceof HibernateSubjectService) {
                    HibernateSubjectService hibernateService = (HibernateSubjectService) subjectService;
                    Career c = hibernateService.findCareerById(Integer.parseInt(idCareer.trim()));
                    if (c != null) {
                        m.setCareer(c);
                    }
                }
            }

            subjectService.update(m);
            out.append("✔ Subject updated -> id=" + m.getIdSubject() + "\n");
            refreshList();
        } catch (Exception e) {
            out.append("✖ ERROR: " + e.getMessage() + "\n");
        }
    }

    private void delete() {
        String idStr = JOptionPane.showInputDialog(this, "idSubject (Enter to delete by name):");
        try {
            out.setText("");
            Optional<Subject> optSubject;
            if (idStr != null && !idStr.trim().isEmpty()) {
                optSubject = subjectService.findById(Integer.parseInt(idStr.trim()));
            } else {
                String nombre = JOptionPane.showInputDialog(this, "Exact name to delete:");
                if (nombre == null) {
                    return;
                }
                optSubject = subjectService.findByName(nombre.trim());
            }
            
            if (!optSubject.isPresent()) {
                out.append("✖ Subject does not exist\n");
                return;
            }

            Subject m = optSubject.get();
            subjectService.delete(m);
            out.append("✔ Subject deleted -> id=" + m.getIdSubject() + "\n");
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
            List<Subject> materias = subjectService.list();
            out.append("SUBJECTS:\n");

            if (materias.isEmpty()) {
                out.append("  (No subjects yet)\n");
            } else {
                for (Subject m : materias) {
                    out.append(" - id=" + m.getIdSubject()
                            + " | " + m.getNombre()
                            + " | level=" + m.getNivel()
                            + " | order=" + m.getOrden()
                            + " | prof=" + (m.getProfessor() != null ? m.getProfessor().getDni() : null)
                            + " | idCareer=" + (m.getCareer() != null ? m.getCareer().getIdCareer() : null)
                            + "\n");
                }
            }
        } catch (Exception e) {
            out.append("✖ ERROR: " + e.getMessage() + "\n");
        }
    }

    private void listByLevelDialog() {
        String in = JOptionPane.showInputDialog(this, "Level (int):");
        if (in == null || in.trim().isEmpty()) {
            return;
        }
        try {
            int nivel = Integer.parseInt(in.trim());
            listByLevel(nivel);
        } catch (NumberFormatException ex) {
            out.append("✖ Invalid level: " + in + "\n");
        }
    }

    public void listByLevel(int nivel) {
        try {
            out.setText("");
            out.append("SUBJECTS (level=" + nivel + "):\n");

            List<Subject> materias = subjectService.listByLevel(nivel);
            if (materias == null || materias.isEmpty()) {
                out.append("(No subjects for that level)\n");
                return;
            }

            for (Subject m : materias) {
                appendSubjectLine(m);
            }
        } catch (Exception e) {
            out.append("✖ ERROR: " + e.getMessage() + "\n");
        }
    }

    private void appendSubjectLine(Subject m) {
        out.append(" - id=" + safeId(m.getIdSubject())
                + " | " + safe(m.getNombre())
                + " | level=" + safe(m.getNivel())
                + " | order=" + safe(m.getOrden())
                + " | prof=" + (m.getProfessor() != null ? m.getProfessor().getDni() : null)
                + " | idCareer=" + (m.getCareer() != null ? m.getCareer().getIdCareer() : null)
                + "\n");
    }

    private static Object safe(Object v) {
        return v == null ? "null" : v;
    }

    private static Object safeId(Object v) {
        return v == null ? "?" : v;
    }

}
