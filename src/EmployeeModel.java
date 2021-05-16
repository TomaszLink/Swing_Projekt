import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class EmployeeModel extends AbstractTableModel {

    private List<Employee> employees = new ArrayList<>();

    HashMap<String,Position> positions = new HashMap<String,Position>();


    public EmployeeModel(){

        positions.put("PREZES",Position.PREZES);
        positions.put("KONSULTANT",Position.KONSULTANT);
        positions.put("SPECJALISTA_HR",Position.SPECJALISTA_HR);
        positions.put("KIEROWCA",Position.KIEROWCA);
        positions.put("PROGRAMISTA",Position.PROGRAMISTA);
        positions.put("TESTER",Position.TESTER);
        positions.put("ANALITYK",Position.ANALITYK);
    }
    public EmployeeModel(List<Employee> employees){
        this.employees=employees;
    }

    public void addEmployee(Employee employee) throws EmployeeAlreadyExistsException {

        for (Employee e:employees) {
            if(employee.compareTo(e)==0){
                throw new EmployeeAlreadyExistsException();
            }

        }


        employees.add(employee);
        fireTableStructureChanged();
    }

    public void deleteEmployee(int index){
        employees.remove(index);
        fireTableStructureChanged();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Employee employee = employees.get(rowIndex);
        if(columnIndex==0){
            employee.setName((String) aValue);
        }
        else if(columnIndex==1){
            employee.setSurname((String) aValue);
        }
        else if(columnIndex==2){
            employee.setPosition((Position) aValue);;
        }
        else if(columnIndex==3){
            employee.setPeriod((Integer) aValue);
        }
        else{
            employee.setSalary((Double) aValue);
        }

    }

    @Override
    public String getColumnName(int column) {
        String[] header = {"Name","Surname","Position","Perios","Salary"};
        return header[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if(columnIndex<2){
            return String.class;
        }
        else if(columnIndex==2){
            return Position.class;
        }
        else if(columnIndex==3){
            return Integer.class;
        }
        return Double.class;
    }


    @Override
    public int getRowCount() {
        return employees.size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }


    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        Employee e = employees.get(rowIndex);
        if(columnIndex==0){
            return e.getName();
        }
        else if(columnIndex==1){
            return e.getSurname();
        }
        else if(columnIndex==2){
            return e.getPosition();
        }
        else if(columnIndex==3){
            return e.getPeriod();
        }
        else{
            return e.getSalary();
        }
    }

    public EmployeeModel search(String name,String surname, Position position, String period,char periodOperator, String salary,char salaryOperator) {

        List<Employee> searchList = new ArrayList<>();
        for (Employee e: employees) {
            boolean add = czyDodac(e,name,surname,position,period,periodOperator,salary,salaryOperator);

            if(add){
                searchList.add(e);
            }


        }
        EmployeeModel newModel = new EmployeeModel(searchList);
        return newModel;







    }

    private static boolean czyDodac(Employee e,String name,String surname, Position position, String period,char periodOperator, String salary, char salaryOperator) {



        if (!name.isBlank()) {
            if (e.getName().compareTo(name) != 0) {
                return false;
            }
        }
        if (!surname.isBlank()) {
            if (e.getSurname().compareTo(surname) != 0) {
                return false;
            }
        }

        if (!period.isBlank()) {
            try {
                int periodInt = Integer.parseInt(period);

                if(periodOperator=='=') {

                    if (e.getPeriod() != periodInt) {
                        return false;
                    }
                }
                else if(periodOperator=='>') {

                    if (e.getPeriod() <= periodInt) {
                        return false;
                    }
                }
                else {

                    if (e.getPeriod() >= periodInt) {
                        return false;
                    }
                }
            } catch (NumberFormatException exception) {

            }

        }

        if (!salary.isBlank()) {
            try {
                double salaryD = Double.parseDouble(salary);

                if(salaryOperator == '=') {
                    if (e.getSalary() != salaryD) {
                        return false;
                    }
                }
                else if(salaryOperator == '>'){
                    if (e.getSalary() <= salaryD) {
                        return false;
                    }
                }
                else{
                    if (e.getSalary() >= salaryD) {
                        return false;
                    }
                }
            } catch (NumberFormatException exception) {

            }

        }

        if(position != null) {
            if (e.getPosition() != position)
                    return false;
        }

        return true;

    }


    public void sort(String sorter) {
        employees.sort(new Comparator<Employee>() {
            @Override
            public int compare(Employee o1, Employee o2) {


                if(sorter == "Name"){
                    return o1.getName().compareTo(o2.getName());

                }
                else if(sorter == "Surname"){
                    return o1.getSurname().compareTo(o2.getSurname());

                }
                else if(sorter == "Position"){
                    return o1.getPosition().toString().compareTo(o2.getPosition().toString());
                }
                else if(sorter == "Period"){
                    return Integer.compare(o1.getPeriod(),o2.getPeriod());
                }
                return Double.compare(o1.getSalary(),o2.getSalary());
            }
        });

        fireTableStructureChanged();
    }

    public void importFile(String filePath)  throws FileNotCsvformat{



        try {

            File file = new File(filePath);
            if(!file.getName().endsWith(".csv")){
                throw new FileNotCsvformat();
            }
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while((line = reader.readLine()) != null){

                String[] values = line.split(";");

                String name = values[0];
                String surname = values[1];
                Position position = positions.get(values[2]);
                int period = Integer.parseInt(values[3]);
                double salary = Double.parseDouble(values[4]);

                Employee employee = new Employee(name,surname,position,period,salary);
                employees.add(employee);




            }

            reader.close();
            fireTableStructureChanged();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    public void removeAll() {
        employees.clear();
        fireTableStructureChanged();

    }

    public void exportFile(String filePath) throws FileNotCsvformat {


        try {
            File file = new File(filePath);
            if(!file.getName().endsWith(".csv")){
                throw new FileNotCsvformat();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (Employee e: employees) {


                Position position = e.getPosition();
                String positionString;
                if(position == null){
                    positionString = "";
                }
                else{
                    positionString = position.toString();
                }
                String line = e.getName()+";"+e.getSurname()+";"+positionString+";"+e.getPeriod()+";"+e.getSalary();
                writer.write(line);
                writer.newLine();

            }
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
