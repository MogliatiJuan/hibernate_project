package presentation;

import app.service.CareerService;
import model.Carrera;
import model.Facultad;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Optional;

public class CareerABM extends JFrame {

    private final JTextArea out = new JTextArea(18, 60);
    private final CareerService careerService;

    public CareerABM(CareerService careerService) {
        this.careerService = careerService;
        initComponents();
        refreshList();
    }

    private void initComponents() {
        setTitle("CAREER – CRUD");
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

    public CareerABM(CareerService careerService, String action) {
        this(careerService);
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
        String nombre = JOptionPane.showInputDialog(this, "Career name:");
        String idFacultad = JOptionPane.showInputDialog(this, "idFacultad:");
        if (nombre == null || idFacultad == null) {
            return;
        }
        if (nombre.trim().isEmpty() || idFacultad.trim().isEmpty()) {
            out.append("✖ Name and idFacultad are required\n");
            return;
        }

        try {
            out.setText("");
            Carrera c = careerService.create(nombre.trim(), Integer.parseInt(idFacultad.trim()));
            out.append("✔ Career created -> id=" + c.getIdCarrera() + ", name=" + c.getNombre()
                    + ", idFacultad=" + c.getFacultad().getIdFacultad() + "\n");
            refreshList();
        } catch (Exception e) {
            out.append("✖ ERROR: " + e.getMessage() + "\n");
        }
    }

    private void update() {
        String idStr = JOptionPane.showInputDialog(this, "idCarrera (Enter to search by name):");
        try {
            out.setText("");
            Optional<Carrera> optCarrera;
            if (idStr != null && !idStr.trim().isEmpty()) {
                optCarrera = careerService.findById(Integer.parseInt(idStr.trim()));
            } else {
                String nombre = JOptionPane.showInputDialog(this, "Exact name to update:");
                if (nombre == null) {
                    return;
                }
                optCarrera = careerService.findByName(nombre.trim());
            }
            
            if (!optCarrera.isPresent()) {
                out.append("✖ Career does not exist\n");
                return;
            }

            Carrera c = optCarrera.get();
            String nuevoNom = JOptionPane.showInputDialog(this, "New name:", c.getNombre());
            String idFac = JOptionPane.showInputDialog(this, "New idFacultad (Enter to keep):");

            if (nuevoNom != null && !nuevoNom.trim().isEmpty()) {
                c.setNombre(nuevoNom.trim());
            }
            if (idFac != null && !idFac.trim().isEmpty()) {
                Facultad f = new Facultad();
                f.setIdFacultad(Integer.parseInt(idFac.trim()));
                c.setFacultad(f);
            }

            careerService.update(c);
            out.append("✔ Career updated -> id=" + c.getIdCarrera() + ", name=" + c.getNombre() + "\n");
            refreshList();
        } catch (Exception e) {
            out.append("✖ ERROR: " + e.getMessage() + "\n");
        }
    }

    private void delete() {
        String idStr = JOptionPane.showInputDialog(this, "idCarrera (Enter to delete by name):");
        try {
            out.setText("");
            Optional<Carrera> optCarrera;
            if (idStr != null && !idStr.trim().isEmpty()) {
                optCarrera = careerService.findById(Integer.parseInt(idStr.trim()));
            } else {
                String nombre = JOptionPane.showInputDialog(this, "Exact name to delete:");
                if (nombre == null) {
                    return;
                }
                optCarrera = careerService.findByName(nombre.trim());
            }
            
            if (!optCarrera.isPresent()) {
                out.append("✖ Career does not exist\n");
                return;
            }

            Carrera c = optCarrera.get();
            careerService.delete(c);
            out.append("✔ Career deleted -> id=" + c.getIdCarrera() + "\n");
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
            List<Carrera> carreras = careerService.list();
            out.append("CAREERS:\n");

            if (carreras.isEmpty()) {
                out.append("  (No careers yet)\n");
            } else {
                for (Carrera c : carreras) {
                    out.append(" - id=" + c.getIdCarrera() + " | " + c.getNombre()
                            + " | idFacultad=" + (c.getFacultad() != null ? c.getFacultad().getIdFacultad() : null) + "\n");
                }
            }
        } catch (Exception e) {
            out.append("✖ ERROR: " + e.getMessage() + "\n");
        }
    }
}
