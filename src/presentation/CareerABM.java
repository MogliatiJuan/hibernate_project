package presentation;

import app.service.CareerService;
import model.Career;
import model.Faculty;

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
        String idFaculty = JOptionPane.showInputDialog(this, "idFaculty:");
        if (nombre == null || idFaculty == null) {
            return;
        }
        if (nombre.trim().isEmpty() || idFaculty.trim().isEmpty()) {
            out.append("✖ Name and idFaculty are required\n");
            return;
        }

        try {
            out.setText("");
            Career c = careerService.create(nombre.trim(), Integer.parseInt(idFaculty.trim()));
            out.append("✔ Career created -> id=" + c.getIdCareer() + ", name=" + c.getName()
                    + ", idFaculty=" + c.getFaculty().getIdFaculty() + "\n");
            refreshList();
        } catch (Exception e) {
            out.append("✖ ERROR: " + e.getMessage() + "\n");
        }
    }

    private void update() {
        String idStr = JOptionPane.showInputDialog(this, "idCareer (Enter to search by name):");
        try {
            out.setText("");
            Optional<Career> optCareer;
            if (idStr != null && !idStr.trim().isEmpty()) {
                optCareer = careerService.findById(Integer.parseInt(idStr.trim()));
            } else {
                String nombre = JOptionPane.showInputDialog(this, "Exact name to update:");
                if (nombre == null) {
                    return;
                }
                optCareer = careerService.findByName(nombre.trim());
            }
            
            if (!optCareer.isPresent()) {
                out.append("✖ Career does not exist\n");
                return;
            }

            Career c = optCareer.get();
            String nuevoNom = JOptionPane.showInputDialog(this, "New name:", c.getName());
            String idFac = JOptionPane.showInputDialog(this, "New idFaculty (Enter to keep):");

            if (nuevoNom != null && !nuevoNom.trim().isEmpty()) {
                c.setName(nuevoNom.trim());
            }
            if (idFac != null && !idFac.trim().isEmpty()) {
                Faculty f = new Faculty();
                f.setIdFaculty(Integer.parseInt(idFac.trim()));
                c.setFaculty(f);
            }

            careerService.update(c);
            out.append("✔ Career updated -> id=" + c.getIdCareer() + ", name=" + c.getName() + "\n");
            refreshList();
        } catch (Exception e) {
            out.append("✖ ERROR: " + e.getMessage() + "\n");
        }
    }

    private void delete() {
        String idStr = JOptionPane.showInputDialog(this, "idCareer (Enter to delete by name):");
        try {
            out.setText("");
            Optional<Career> optCareer;
            if (idStr != null && !idStr.trim().isEmpty()) {
                optCareer = careerService.findById(Integer.parseInt(idStr.trim()));
            } else {
                String nombre = JOptionPane.showInputDialog(this, "Exact name to delete:");
                if (nombre == null) {
                    return;
                }
                optCareer = careerService.findByName(nombre.trim());
            }
            
            if (!optCareer.isPresent()) {
                out.append("✖ Career does not exist\n");
                return;
            }

            Career c = optCareer.get();
            careerService.delete(c);
            out.append("✔ Career deleted -> id=" + c.getIdCareer() + "\n");
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
            List<Career> carreras = careerService.list();
            out.append("CAREERS:\n");

            if (carreras.isEmpty()) {
                out.append("  (No careers yet)\n");
            } else {
                for (Career c : carreras) {
                    out.append(" - id=" + c.getIdCareer() + " | " + c.getName()
                            + " | idFaculty=" + (c.getFaculty() != null ? c.getFaculty().getIdFaculty() : null) + "\n");
                }
            }
        } catch (Exception e) {
            out.append("✖ ERROR: " + e.getMessage() + "\n");
        }
    }
}
