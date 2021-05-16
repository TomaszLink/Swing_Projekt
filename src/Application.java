import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.SQLOutput;

public class Application {

    public static void main(String[] args){
        SwingUtilities.invokeLater(()->createAndShowGUI());


    }
    private static void refreshModel(JTable table, EmployeeModel employeeModel){
        if(table.getModel().toString() != employeeModel.toString()){
            table.setModel(employeeModel);
        }
    }

    private static void createAndShowGUI() {

        FileFilter csvFileFilter = new FileFilter() {
            @Override
            public boolean accept(File f) {
                if(f.getName().endsWith(".csv"))
                    return true;
                return false;
            }

            @Override
            public String getDescription() {
                return "CSV files";
            }
        };

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(630,550);
        frame.setLayout(null);
        frame.setVisible(true);

        JLabel nameText = new JLabel("Name:");
        nameText.setBounds(420,20,50,25);

        JTextField nameField = new JTextField();
        nameField.setBounds(480,20,100,25);

        JLabel surnameText = new JLabel("Surname:");
        surnameText.setBounds(420,55,70,25);

        JTextField surnameField = new JTextField();
        surnameField.setBounds(480,55,100,25);

        JLabel positionText = new JLabel("Position:");
        positionText.setBounds(420,90,70,25);

        JComboBox positionField = new JComboBox(Position.values());
        positionField.setSelectedIndex(-1);
        positionField.setBounds(480,90,100,25);

        JLabel periodText = new JLabel("Period:");
        periodText.setBounds(420,125,70,25);

        JTextField periodField = new JTextField();
        periodField.setBounds(480,125,100,25);

        JLabel salaryText = new JLabel("Salary:");
        salaryText.setBounds(420,160,70,25);

        JTextField salaryField = new JTextField();
        salaryField.setBounds(480,160,100,25);

        JButton addButton = new JButton("Add");
        addButton.setBounds(450,200,100,25);

        JButton removeButton = new JButton("Delete");
        removeButton.setBounds(450,235,100,25);

        JTextField nameSearchField = new JTextField();
        nameSearchField.setBounds(10,315,75,20);

        JTextField surnameSearchField = new JTextField();
        surnameSearchField.setBounds(90,315,75,20);

        JComboBox positionSearchField = new JComboBox(Position.values());
        positionSearchField.setSelectedIndex(-1);
        positionSearchField.setBounds(170,315,75,20);

        JButton salarySearchButton = new JButton("=");
        salarySearchButton.setBounds(345,340,45,20);

        JButton periodSearchButton = new JButton("=");
        periodSearchButton.setBounds(265,340,45,20);

        JTextField periodSearchField = new JTextField();
        periodSearchField.setBounds(250,315,75,20);

        JTextField salarySearchField = new JTextField();
        salarySearchField.setBounds(330,315,75,20);

        JButton searchButton = new JButton("Search");
        searchButton.setBounds(450,270,100,25);

        JButton clearButton = new JButton("Clear");
        clearButton.setBounds(450,305,100,25);

        String [] options = {"Name","Surname","Position","Period","Salary"};
        JComboBox sortBox = new JComboBox(options);
        sortBox.setSelectedIndex(-1);
        sortBox.setBounds(450,340,100,25);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Data");
        menuBar.add(menu);
        JMenuItem importButton = new JMenuItem("Import");
        menu.add(importButton);

        JMenuItem exportButton = new JMenuItem("Export");
        menu.add(exportButton);

        JMenuItem removeAllButton = new JMenuItem("Remove all");
        menu.add(removeAllButton);

        frame.setJMenuBar(menuBar);


        EmployeeModel employeeModel = new EmployeeModel();


        JTable table = new JTable(employeeModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10,10,400,300);
        frame.setResizable(false);


        //dodawanie komponentów
        frame.add(scrollPane);
        frame.add(nameText);
        frame.add(nameField);
        frame.add(surnameText);
        frame.add(surnameField);
        frame.add(positionText);
        frame.add(positionField);
        frame.add(periodText);
        frame.add(periodField);
        frame.add(salaryText);
        frame.add(salaryField);
        frame.add(addButton);
        frame.add(removeButton);
        frame.add(nameSearchField);
        frame.add(surnameSearchField);
        frame.add(positionSearchField);
        frame.add(periodSearchField);
        frame.add(salarySearchField);
        frame.add(searchButton);
        frame.add(clearButton);
        frame.add(periodSearchButton);
        frame.add(salarySearchButton);
        frame.add(sortBox);



        //funkcje

        exportButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();

            fileChooser.setFileFilter(csvFileFilter);

            int returnVal = fileChooser.showSaveDialog(null);
            if(returnVal == JFileChooser.APPROVE_OPTION) {


                try {
                    employeeModel.exportFile(fileChooser.getSelectedFile().toString());
                    JOptionPane.showMessageDialog(null, "The data has been saved");
                } catch (FileNotCsvformat ex) {
                    JOptionPane.showMessageDialog(null, ex.toString());
                }
            }
        });

        removeAllButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(null,"Are you sure you want to delete all records?");
            if(choice == JOptionPane.YES_OPTION) {
                employeeModel.removeAll();
            }

        });


        importButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(csvFileFilter);
           int returnVal = fileChooser.showOpenDialog(null);

           if(returnVal == JFileChooser.APPROVE_OPTION) {
               try {
                   employeeModel.importFile(fileChooser.getSelectedFile().toString());
               }
               catch(NumberFormatException nfe ){
                   JOptionPane.showMessageDialog(null, "This file has an invalid format");
               }
               catch (FileNotCsvformat ex){
                   JOptionPane.showMessageDialog(null, ex.toString());
               }
           }
        });

        sortBox.addActionListener(e -> {

            String sorter = (String) sortBox.getSelectedItem();
            employeeModel.sort(sorter);

        });

        salarySearchButton.addActionListener(e ->{

            String actualValue = salarySearchButton.getText();
            if(actualValue=="=")
                salarySearchButton.setText("<");
            else if(actualValue=="<")
                salarySearchButton.setText(">");
            else
                salarySearchButton.setText("=");

        });

        periodSearchButton.addActionListener(e->{
            String actualValue = periodSearchButton.getText();
            if(actualValue=="=")
                periodSearchButton.setText("<");
            else if(actualValue=="<")
                periodSearchButton.setText(">");
            else
                periodSearchButton.setText("=");
        });

        clearButton.addActionListener(e->{



            table.setModel(employeeModel);

            nameSearchField.setText("");
            surnameSearchField.setText("");
            positionSearchField.setSelectedIndex(-1);
            periodSearchField.setText("");
            salarySearchField.setText("");
            periodSearchButton.setText("=");
            salarySearchButton.setText("=");

            System.out.println(positionField.getSelectedIndex());
        });

        searchButton.addActionListener(e ->{
            String searchName = nameSearchField.getText();
            String searchSurname = surnameSearchField.getText();
            Position searchposition = (Position) positionSearchField.getSelectedItem();
            String searchPeriod = periodSearchField.getText();
            String searchSalary = salarySearchField.getText();
            char periodOperator = periodSearchButton.getText().charAt(0);
            char salaryOperator = salarySearchButton.getText().charAt(0);



            EmployeeModel searchEmployeeModel = employeeModel.search(searchName,searchSurname,searchposition,searchPeriod,periodOperator,searchSalary,salaryOperator);

            table.setModel(searchEmployeeModel);




        });

       removeButton.addActionListener(e->{
           int index = table.getSelectedRow();
           if (index != -1) {

               int choice = JOptionPane.showConfirmDialog(null,"Are you sure you want to delete the selected employee?");
               if(choice == JOptionPane.YES_OPTION) {
                   index = table.convertRowIndexToModel(index);
                   employeeModel.deleteEmployee(index);
                   refreshModel(table,employeeModel);
               }
           }
       });

        addButton.addActionListener(e -> {


                String name = nameField.getText();

                if(!name.isBlank()){
                    String surname = surnameField.getText();
                    if(!surname.isBlank()) {
                        Position position = (Position) positionField.getSelectedItem();
                        if(position != null) {
                            try {
                                int period = Integer.parseInt(periodField.getText());
                                double salary = Double.parseDouble(salaryField.getText());
                                Employee add = new Employee(name, surname, position, period, salary);
                                employeeModel.addEmployee(add);
                                nameField.setText("");
                                surnameField.setText("");
                                positionField.setSelectedIndex(-1);
                                periodField.setText("");
                                salaryField.setText("");

                               refreshModel(table,employeeModel);


                            } catch (NumberFormatException exception) {
                                JOptionPane.showMessageDialog(null, "Fields Salary and Period must be number!");
                            } catch (EmployeeAlreadyExistsException exception) {
                                JOptionPane.showMessageDialog(null, exception.toString());
                            }
                        }else
                        {
                            JOptionPane.showMessageDialog(null,"Position can not be empty!");
                        }
                    }else{
                        JOptionPane.showMessageDialog(null,"Surname can not be empty!");
                    }
                }
                else{

                    JOptionPane.showMessageDialog(null,"Name can not be empty!");
                }




            });








    }
}
