package presentation;

import app.service.ProfessorService;
import model.City;
import model.Professor;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Optional;

public class ProfessorABM extends JFrame {

    private final JTextArea out = new JTextArea(18, 60);
    private final ProfessorService professorService;

    public ProfessorABM(ProfessorService professorService) {
        this.professorService = professorService;
        initComponents();
        refreshList();
    }

    private void initComponents() {
        setTitle("PROFESSOR – CRUD");
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

    public ProfessorABM(ProfessorService professorService, String action) {
        this(professorService);
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
        String fecha = JOptionPane.showInputDialog(this, "Birth date (yyyy-MM-dd):");
        String antig = JOptionPane.showInputDialog(this, "Seniority (years):");
        String idCity = JOptionPane.showInputDialog(this, "idCity:");
        if (dni == null || apellido == null || nombre == null || fecha == null || antig == null || idCity == null) {
            return;
        }

        if (dni.trim().isEmpty() || antig.trim().isEmpty() || idCity.trim().isEmpty()) {
            out.append("✖ DNI, seniority and idCity are required\n");
            return;
        }

        try {
            out.setText("");
            Professor p = professorService.create(
                    apellido.trim(),
                    nombre.trim(),
                    Integer.parseInt(dni.trim()),
                    fecha.trim(),
                    Integer.parseInt(antig.trim()),
                    Integer.parseInt(idCity.trim())
            );
            out.append("✔ Professor created -> dni=" + p.getDni() + ", " + p.getLastName() + ", " + p.getFirstName() + "\n");
            try {
                refreshList();
            } catch (Exception e) {
                out.append("⚠ Warning: Could not refresh list: " + e.getMessage() + "\n");
            }
        } catch (Exception e) {
            out.append("✖ ERROR: " + e.getMessage() + "\n");
        }
    }

    private void update() {
        String dni = JOptionPane.showInputDialog(this, "DNI to update (Enter to search by LastName+FirstName):");
        try {
            out.setText("");
            Optional<Professor> optProfessor;
            if (dni != null && !dni.trim().isEmpty()) {
                optProfessor = professorService.findById(Integer.parseInt(dni.trim()));
            } else {
                String ape = JOptionPane.showInputDialog(this, "Exact last name:");
                String nom = JOptionPane.showInputDialog(this, "Exact first name:");
                if (ape == null || nom == null) {
                    return;
                }
                optProfessor = professorService.findByLastNameAndFirstName(ape.trim(), nom.trim());
            }
            
            if (!optProfessor.isPresent()) {
                out.append("✖ Professor does not exist\n");
                return;
            }

            Professor p = optProfessor.get();
            String antig = JOptionPane.showInputDialog(this, "New seniority:", String.valueOf(p.getSeniority()));
            String idCity = JOptionPane.showInputDialog(this, "New idCity (Enter to keep):");

            if (antig != null && !antig.trim().isEmpty()) {
                p.setSeniority(Integer.parseInt(antig.trim()));
            }
            if (idCity != null && !idCity.trim().isEmpty()) {
                City c = new City();
                c.setIdCity(Integer.parseInt(idCity.trim()));
                p.setCity(c);
            }

            professorService.update(p);
            out.append("✔ Professor updated -> dni=" + p.getDni() + "\n");
            try {
                refreshList();
            } catch (Exception e) {
                out.append("⚠ Warning: Could not refresh list: " + e.getMessage() + "\n");
            }
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
            Optional<Professor> optProfessor = professorService.findById(Integer.parseInt(dni.trim()));
            if (!optProfessor.isPresent()) {
                out.append("✖ Does not exist\n");
                return;
            }

            Professor p = optProfessor.get();
            professorService.delete(p);
            out.append("✔ Professor deleted\n");
            try {
                refreshList();
            } catch (Exception e) {
                out.append("⚠ Warning: Could not refresh list: " + e.getMessage() + "\n");
            }
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
            List<Professor> profesores = professorService.list();
            out.append("PROFESSORS (by seniority):\n");
            if (profesores.isEmpty()) {
                out.append("  (No professors yet)\n");
            } else {
                for (Professor p : profesores) {
                    out.append(" - dni=" + p.getDni() + " | " + p.getLastName() + ", " + p.getFirstName()
                            + " | seniority=" + p.getSeniority()
                            + " | idCity=" + (p.getCity() != null ? p.getCity().getIdCity() : null) + "\n");
                }
            }
        } catch (Exception e) {
            out.append("✖ ERROR: " + e.getMessage() + "\n");
        }
    }
}
