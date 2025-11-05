package presentation;

import app.service.CityService;
import model.Ciudad;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Optional;

public class CityABM extends JFrame {

    private final JTextArea out = new JTextArea(18, 60);
    private final CityService cityService;

    public CityABM(CityService cityService) {
        this.cityService = cityService;
        initComponents();
        refreshList();
    }

    private void initComponents() {
        setTitle("CITY – CRUD");
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

    public CityABM(CityService cityService, String action) {
        this(cityService);
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
        String nombre = JOptionPane.showInputDialog(this, "City name:");
        if (nombre == null || nombre.trim().isEmpty()) {
            return;
        }

        try {
            out.setText("");
            Ciudad c = cityService.create(nombre.trim());
            out.append("✔ City created -> id=" + c.getIdCiudad() + ", name=" + c.getNombre() + "\n");
            refreshList();
        } catch (Exception e) {
            out.append("✖ ERROR: " + e.getMessage() + "\n");
        }
    }

    private void update() {
        String idStr = JOptionPane.showInputDialog(this, "idCiudad (Enter to search by name):");
        try {
            out.setText("");
            Optional<Ciudad> optCiudad;
            if (idStr != null && !idStr.trim().isEmpty()) {
                optCiudad = cityService.findById(Integer.parseInt(idStr.trim()));
            } else {
                String nombre = JOptionPane.showInputDialog(this, "Exact name to update:");
                if (nombre == null) {
                    return;
                }
                optCiudad = cityService.findByName(nombre.trim());
            }
            
            if (!optCiudad.isPresent()) {
                out.append("✖ City does not exist\n");
                return;
            }

            Ciudad c = optCiudad.get();
            String nuevo = JOptionPane.showInputDialog(this, "New name:", c.getNombre());
            if (nuevo == null || nuevo.trim().isEmpty()) {
                return;
            }
            c.setNombre(nuevo.trim());
            cityService.update(c);
            out.append("✔ City updated -> id=" + c.getIdCiudad() + ", name=" + c.getNombre() + "\n");
            refreshList();
        } catch (Exception e) {
            out.append("✖ ERROR: " + e.getMessage() + "\n");
        }
    }

    private void delete() {
        String idStr = JOptionPane.showInputDialog(this, "idCiudad (Enter to delete by name):");
        try {
            out.setText("");
            Optional<Ciudad> optCiudad;
            if (idStr != null && !idStr.trim().isEmpty()) {
                optCiudad = cityService.findById(Integer.parseInt(idStr.trim()));
            } else {
                String nombre = JOptionPane.showInputDialog(this, "Exact name to delete:");
                if (nombre == null) {
                    return;
                }
                optCiudad = cityService.findByName(nombre.trim());
            }
            
            if (!optCiudad.isPresent()) {
                out.append("✖ City does not exist\n");
                return;
            }

            Ciudad c = optCiudad.get();
            cityService.delete(c);
            out.append("✔ City deleted -> id=" + c.getIdCiudad() + "\n");
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
            out.append("CITIES:\n");
            List<Ciudad> ciudades = cityService.list();

            if (ciudades.isEmpty()) {
                out.append("  (No cities yet)\n");
            } else {
                for (Ciudad c : ciudades) {
                    out.append(" - id=" + c.getIdCiudad() + " | " + c.getNombre() + "\n");
                }
            }
        } catch (Exception e) {
            out.append("ERROR listing: " + e.getMessage() + "\n");
        }
    }

}
