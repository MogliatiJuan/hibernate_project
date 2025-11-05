package presentation;

import infra.service.HibernateStudentService;
import infra.service.HibernateCareerService;
import infra.service.HibernateCityService;
import infra.service.HibernateFacultyService;
import infra.service.HibernateSubjectService;
import infra.service.HibernateProfessorService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MenuFacultad extends JFrame {

    public MenuFacultad() {
        setTitle("Hibernate Project — Main Menu");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}

        JPanel root = new JPanel(new BorderLayout(16, 16));
        root.setBorder(new EmptyBorder(18, 18, 18, 18));
        setContentPane(root);

        JLabel titulo = new JLabel("Faculty Management", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        root.add(titulo, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(3, 2, 12, 12));
        root.add(grid, BorderLayout.CENTER);

        JButton btnCiudad   = createButton("City",   'C', "City CRUD");
        JButton btnFacultad = createButton("Faculty", 'F', "Faculty CRUD");
        JButton btnCarrera  = createButton("Career",  'R', "Career CRUD");
        JButton btnProfesor = createButton("Professor", 'P', "Professor CRUD");
        JButton btnMateria  = createButton("Subject",  'M', "Subject CRUD");
        JButton btnAlumno   = createButton("Student",   'A', "Student CRUD");

        Dimension pref = new Dimension(160, 52);
        for (JButton b : new JButton[]{btnCiudad, btnFacultad, btnCarrera, btnProfesor, btnMateria, btnAlumno}) {
            b.setPreferredSize(pref);
            grid.add(b);
        }

        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        JButton btnSalir = new JButton("Exit");
        btnSalir.setMnemonic('E');
        btnSalir.setToolTipText("Close menu");
        btnSalir.setPreferredSize(new Dimension(120, 38));
        btnSalir.addActionListener(e -> dispose());
        south.add(btnSalir);
        root.add(south, BorderLayout.SOUTH);

        btnCiudad.addActionListener(e -> openABM("City"));
        btnFacultad.addActionListener(e -> openABM("Faculty"));
        btnCarrera.addActionListener(e -> openABM("Career"));
        btnProfesor.addActionListener(e -> openABM("Professor"));
        btnMateria.addActionListener(e -> openABM("Subject"));
        btnAlumno.addActionListener(e -> openABM("Student"));

        pack();
        setSize(420, getHeight());
        setLocationRelativeTo(null);
    }

    private JButton createButton(String texto, char mnemonic, String tooltip) {
        JButton b = new JButton(texto);
        b.setMnemonic(mnemonic);
        b.setToolTipText(tooltip);
        b.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        return b;
    }

    private void openABM(String entity) {
    String[] ops = {"Create", "Update", "Delete", "List"};
    int i = JOptionPane.showOptionDialog(
            this,
            "Select an action for " + entity + ":",
            entity + " — Actions",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null, ops, ops[0]);

    if (i < 0) return;
    String action = ops[i];

    JFrame frame = null;
    switch (entity) {
        case "City":
            frame = new CityABM(new HibernateCityService(), action);
            break;
        case "Faculty":
            frame = new FacultyABM(new HibernateFacultyService(), action);
            break;
        case "Career":
            frame = new CareerABM(new HibernateCareerService(), action);
            break;
        case "Professor":
            frame = new ProfessorABM(new HibernateProfessorService(), action);
            break;
        case "Subject":
            frame = new SubjectABM(new HibernateSubjectService(), action);
            break;
        case "Student":
            frame = new StudentABM(new HibernateStudentService(), action);
            break;
        default:
            return;
    }
    if (frame != null) frame.setVisible(true);
}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuFacultad().setVisible(true));
    }
}
