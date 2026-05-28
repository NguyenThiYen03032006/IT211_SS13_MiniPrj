package rikkei.edu.miniprojectss13.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import rikkei.edu.miniprojectss13.exception.EmployeeNotFoundException;
import rikkei.edu.miniprojectss13.model.Employee;
import rikkei.edu.miniprojectss13.repository.EmployeeRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    // TASK 1 Lấy danh sách tất cả nhân viên
    public List<Employee> getAllEmployees() {
        log.info("Getting all employees");
        return employeeRepository.findAll();
    }

    // TASK 2 Tìm nhân viên theo ID
    public Employee getById(Long id) {
        log.info("Finding employee by id: {}", id);
        return employeeRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Không tìm thấy nhân viên với id: {}", id);
                    return new EmployeeNotFoundException(id);
                });
    }

    // TASK 3 Thêm nhân viên mới (tự sinh ID)
    public Employee addEmployee(Employee payload) {
        log.info("Adding new employee: {}", payload.getFullName());
        Employee created = new Employee(
                null,
                payload.getFullName(),
                payload.getDepartment(),
                payload.getSalary()
        );
        Employee saved = employeeRepository.save(created);
        log.info("Employee added successfully with id: {}", saved.getId());
        return saved;
    }

    // TASK 4 Cập nhật toàn bộ thông tin nhân viên
    public Employee updateEmployee(Long id, Employee payload) {
        log.info("Updating employee with id: {}", id);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Không tìm thấy nhân viên với id: {}", id);
                    return new EmployeeNotFoundException(id);
                });
        
        employee.setFullName(payload.getFullName());
        employee.setDepartment(payload.getDepartment());
        employee.setSalary(payload.getSalary());
        
        Employee updated = employeeRepository.save(employee);
        log.info("Employee updated successfully with id: {}", id);
        return updated;
    }

    // TASK 5 Xóa nhân viên theo ID
    public void deleteById(Long id) {
        log.info("Deleting employee with id: {}", id);
        if (!employeeRepository.existsById(id)) {
            log.warn("Không tìm thấy nhân viên với id: {}", id);
            throw new EmployeeNotFoundException(id);
        }
        employeeRepository.deleteById(id);
        log.info("Employee deleted successfully with id: {}", id);
    }
}

