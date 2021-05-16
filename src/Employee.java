public class Employee implements Comparable {

    private String name;
    private String surname;
    private Position position;
    private int period;
    private double salary;

    public Employee(String name, String surname, Position position, int period, double salary) {
        this.name = name;
        this.surname = surname;
        this.position = position;
        this.period = period;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }



    @Override
    public String toString() {
        return name +" "+surname+" "+position +" "+period+" "+salary;
    }

    @Override
    public int compareTo(Object o) {
        Employee e = (Employee) o;
        int result = surname.compareTo(e.getSurname());
        if(result!=0){
            return result;
        }
        result = name.compareTo(e.getName());
        if(result!=0){
            return result;
        }
        return 0;

    }
}
