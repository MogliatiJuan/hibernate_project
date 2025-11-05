package presentation;

import app.service.FacultyService;
import infra.service.HibernateFacultyService;
import model.Ciudad;
import model.Facultad;

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
        String idCiudad = JOptionPane.showInputDialog(this, "idCiudad:");
        if (nombre == null || idCiudad == null) {
            return;
        }

        try {
            out.setText("");
            Facultad f = facultyService.create(nombre.trim(), Integer.parseInt(idCiudad.trim()));
            out.append("✔ Faculty created -> id=" + f.getIdFacultad() + ", name=" + f.getNombre() + ", idCiudad=" + f.getCiudad().getIdCiudad() + "\n");
            refreshList();
        } catch (Exception e) {
            out.append("✖ ERROR: " + e.getMessage() + "\n");
        }
    }

    private void update() {
        String idStr = JOptionPane.showInputDialog(this, "idFacultad (Enter to search by name):");
        try {
            out.setText("");
            Optional<Facultad> optFacultad;
            if (idStr != null && !idStr.trim().isEmpty()) {
                optFacultad = facultyService.findById(Integer.parseInt(idStr.trim()));
            } else {
                String nombre = JOptionPane.showInputDialog(this, "Exact name to update:");
                if (nombre == null) {
                    return;
                }
                optFacultad = facultyService.findByName(nombre.trim());
            }
            
            if (!optFacultad.isPresent()) {
                out.append("✖ Faculty does not exist\n");
                return;
            }

            Facultad f = optFacultad.get();
            String nuevoNom = JOptionPane.showInputDialog(this, "New name:", f.getNombre());
            String idCiudad = JOptionPane.showInputDialog(this, "New idCiudad (Enter to keep):");
            
            if (nuevoNom != null && !nuevoNom.trim().isEmpty()) {
                f.setNombre(nuevoNom.trim());
            }
            if (idCiudad != null && !idCiudad.trim().isEmpty()) {
                if (facultyService instanceof HibernateFacultyService) {
                    // Load Ciudad through a helper or directly
                    // For now, we'll set it on the detached entity and let the service handle it
                    Ciudad c = new Ciudad();
                    c.setIdCiudad(Integer.parseInt(idCiudad.trim()));
                    f.setCiudad(c);
                }
            }
            
            facultyService.update(f);
            out.append("✔ Faculty updated -> id=" + f.getIdFacultad() + ", name=" + f.getNombre() + "\n");
            refreshList();
        } catch (Exception e) {
            out.append("✖ ERROR: " + e.getMessage() + "\n");
        }
    }

    private void delete() {
        String idStr = JOptionPane.showInputDialog(this, "idFacultad (Enter to delete by name):");
        try {
            out.setText("");
            Optional<Facultad> optFacultad;
            if (idStr != null && !idStr.trim().isEmpty()) {
                optFacultad = facultyService.findById(Integer.parseInt(idStr.trim()));
            } else {
                String nombre = JOptionPane.showInputDialog(this, "Exact name to delete:");
                if (nombre == null) {
                    return;
                }
                optFacultad = facultyService.findByName(nombre.trim());
            }
            
            if (!optFacultad.isPresent()) {
                out.append("✖ Faculty does not exist\n");
                return;
            }
            
            Facultad f = optFacultad.get();
            facultyService.delete(f);
            out.append("✔ Faculty deleted -> id=" + f.getIdFacultad() + "\n");
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
            List<Facultad> facultades = facultyService.list();
            out.append("FACULTIES:\n");

            if (facultades.isEmpty()) {
                out.append("  (No faculties yet)\n");
            } else {
                for (Facultad f : facultades) {
                    out.append(" - id=" + f.getIdFacultad() + " | " + f.getNombre()
                            + " | idCiudad=" + (f.getCiudad() != null ? f.getCiudad().getIdCiudad() : null) + "\n");
                }
            }
        } catch (Exception e) {
            out.append("✖ ERROR: " + e.getMessage() + "\n");
        }
    }
}
