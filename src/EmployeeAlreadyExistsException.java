public class EmployeeAlreadyExistsException extends Exception{

    @Override
    public String toString() {
        return "This Employee already exists";
    }
}
