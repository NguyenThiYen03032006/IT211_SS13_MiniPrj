package rikkei.edu.miniprojectss13.exception;

public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(String message) {
        super(message);
    }

    public EmployeeNotFoundException(Long id) {
        super("Không tìm thấy nhân viên với id: " + id);
    }
}

