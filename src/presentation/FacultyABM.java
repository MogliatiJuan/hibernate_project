package presentation;

import app.service.FacultyService;
import infra.service.HibernateFacultyService;
import model.City;
import model.Faculty;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Optional;

public class FacultyABM extends JFrame {

    private final JTextArea out = new JTextArea(18, 60);
    private final FacultyService facultyService;

    public FacultyABM(FacultyService facultyService) {
        this.facultyService = facultyService;
        initComponents();
        refreshList();
    }

    private void initComponents() {
        setTitle("FACULTY – CRUD");
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

    public FacultyABM(FacultyService facultyService, String action) {
        this(facultyService);
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
        String nombre = JOptionPane.showInputDialog(this, "Faculty name:");
        String idCity = JOptionPane.showInputDialog(this, "idCity:");
        if (nombre == null || idCity == null) {
            return;
        }

        try {
            out.setText("");
            Faculty f = facultyService.create(nombre.trim(), Integer.parseInt(idCity.trim()));
            out.append("✔ Faculty created -> id=" + f.getIdFaculty() + ", name=" + f.getName() + ", idCity=" + f.getCity().getIdCity() + "\n");
            refreshList();
        } catch (Exception e) {
            out.append("✖ ERROR: " + e.getMessage() + "\n");
        }
    }

    private void update() {
        String idStr = JOptionPane.showInputDialog(this, "idFaculty (Enter to search by name):");
        try {
            out.setText("");
            Optional<Faculty> optFaculty;
            if (idStr != null && !idStr.trim().isEmpty()) {
                optFaculty = facultyService.findById(Integer.parseInt(idStr.trim()));
            } else {
                String nombre = JOptionPane.showInputDialog(this, "Exact name to update:");
                if (nombre == null) {
                    return;
                }
                optFaculty = facultyService.findByName(nombre.trim());
            }
            
            if (!optFaculty.isPresent()) {
                out.append("✖ Faculty does not exist\n");
                return;
            }

            Faculty f = optFaculty.get();
            String nuevoNom = JOptionPane.showInputDialog(this, "New name:", f.getName());
            String idCity = JOptionPane.showInputDialog(this, "New idCity (Enter to keep):");
            
            if (nuevoNom != null && !nuevoNom.trim().isEmpty()) {
                f.setName(nuevoNom.trim());
            }
            if (idCity != null && !idCity.trim().isEmpty()) {
                if (facultyService instanceof HibernateFacultyService) {
                    City c = new City();
                    c.setIdCity(Integer.parseInt(idCity.trim()));
                    f.setCity(c);
                }
            }
            
            facultyService.update(f);
            out.append("✔ Faculty updated -> id=" + f.getIdFaculty() + ", name=" + f.getName() + "\n");
            refreshList();
        } catch (Exception e) {
            out.append("✖ ERROR: " + e.getMessage() + "\n");
        }
    }

    private void delete() {
        String idStr = JOptionPane.showInputDialog(this, "idFaculty (Enter to delete by name):");
        try {
            out.setText("");
            Optional<Faculty> optFaculty;
            if (idStr != null && !idStr.trim().isEmpty()) {
                optFaculty = facultyService.findById(Integer.parseInt(idStr.trim()));
            } else {
                String nombre = JOptionPane.showInputDialog(this, "Exact name to delete:");
                if (nombre == null) {
                    return;
                }
                optFaculty = facultyService.findByName(nombre.trim());
            }
            
            if (!optFaculty.isPresent()) {
                out.append("✖ Faculty does not exist\n");
                return;
            }
            
            Faculty f = optFaculty.get();
            facultyService.delete(f);
            out.append("✔ Faculty deleted -> id=" + f.getIdFaculty() + "\n");
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
            List<Faculty> facultades = facultyService.list();
            out.append("FACULTIES:\n");

            if (facultades.isEmpty()) {
                out.append("  (No faculties yet)\n");
            } else {
                for (Faculty f : facultades) {
                    out.append(" - id=" + f.getIdFaculty() + " | " + f.getName()
                            + " | idCity=" + (f.getCity() != null ? f.getCity().getIdCity() : null) + "\n");
                }
            }
        } catch (Exception e) {
            out.append("✖ ERROR: " + e.getMessage() + "\n");
        }
    }
}
