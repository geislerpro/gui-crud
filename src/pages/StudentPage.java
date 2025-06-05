package pages;

import dal.students.StudentDAO;
import models.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class StudentPage extends JFrame {
    private final StudentDAO studentDao = new StudentDAO();
    private final JTable table;
    private final DefaultTableModel tableModel;
    private final JTextField studentNumberField;
    private final JTextField firstNameField;
    private final JTextField lastNameField;
    private final JTextField programField;
    private final JSpinner levelSpinner;
    private final JButton addButton, updateButton, deleteButton;

    public StudentPage() {
        setTitle("Student CRUD");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Set a simple font
        Font defaultFont = new Font("SansSerif", Font.PLAIN, 14);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        studentNumberField = new JTextField(15);
        firstNameField = new JTextField(15);
        lastNameField = new JTextField(15);
        programField = new JTextField(15);
        levelSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));

        // Apply consistent font
        studentNumberField.setFont(defaultFont);
        firstNameField.setFont(defaultFont);
        lastNameField.setFont(defaultFont);
        programField.setFont(defaultFont);
        levelSpinner.setFont(defaultFont);

        int row = 0;
        addFormRow(formPanel, gbc, row++, "Student Number:", studentNumberField);
        addFormRow(formPanel, gbc, row++, "First Name:", firstNameField);
        addFormRow(formPanel, gbc, row++, "Last Name:", lastNameField);
        addFormRow(formPanel, gbc, row++, "Program:", programField);
        addFormRow(formPanel, gbc, row++, "Level:", levelSpinner);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");

        // Style buttons slightly
        Font buttonFont = new Font("SansSerif", Font.BOLD, 13);
        addButton.setFont(buttonFont);
        updateButton.setFont(buttonFont);
        deleteButton.setFont(buttonFont);

        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Student Number", "First Name", "Last Name", "Program", "Level"}, 0
        ) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setFont(defaultFont);
        table.setRowHeight(24);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Actions
        loadStudents();

        addButton.addActionListener(e -> addStudent());
        updateButton.addActionListener(e -> updateStudent());
        deleteButton.addActionListener(e -> deleteStudent());

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    studentNumberField.setText(tableModel.getValueAt(row, 1).toString());
                    firstNameField.setText(tableModel.getValueAt(row, 2).toString());
                    lastNameField.setText(tableModel.getValueAt(row, 3).toString());
                    programField.setText(tableModel.getValueAt(row, 4).toString());
                    levelSpinner.setValue((int) tableModel.getValueAt(row, 5));
                    updateButton.setEnabled(true);
                    deleteButton.setEnabled(true);
                }
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(field, gbc);
    }

    private void loadStudents() {
        tableModel.setRowCount(0);
        List<Student> students = studentDao.getAllStudents();
        for (Student student : students) {
            tableModel.addRow(new Object[]{
                    student.getId(),
                    student.getStudentNumber(),
                    student.getFirstName(),
                    student.getLastName(),
                    student.getProgram(),
                    student.getLevel()
            });
        }
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }

    private void addStudent() {
        if (validateFields()) {
            studentDao.addStudent(new Student(0,
                    studentNumberField.getText(),
                    firstNameField.getText(),
                    lastNameField.getText(),
                    programField.getText(),
                    (int) levelSpinner.getValue()
            ));
            loadStudents();
            clearFields();
        }
    }

    private void updateStudent() {
        int row = table.getSelectedRow();
        if (row != -1 && validateFields()) {
            int id = (int) tableModel.getValueAt(row, 0);
            studentDao.updateStudent(new Student(id,
                    studentNumberField.getText(),
                    firstNameField.getText(),
                    lastNameField.getText(),
                    programField.getText(),
                    (int) levelSpinner.getValue()
            ));
            loadStudents();
            clearFields();
        }
    }

    private void deleteStudent() {
        int row = table.getSelectedRow();
        if (row != -1) {
            int id = (int) tableModel.getValueAt(row, 0);
            studentDao.deleteStudent(id);
            loadStudents();
            clearFields();
        }
    }

    private boolean validateFields() {
        if (studentNumberField.getText().isEmpty() ||
                firstNameField.getText().isEmpty() ||
                lastNameField.getText().isEmpty() ||
                programField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return false;
        }
        return true;
    }

    private void clearFields() {
        studentNumberField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        programField.setText("");
        levelSpinner.setValue(1);
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }
}