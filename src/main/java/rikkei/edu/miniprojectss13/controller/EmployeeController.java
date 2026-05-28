package rikkei.edu.miniprojectss13.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rikkei.edu.miniprojectss13.exception.EmployeeNotFoundException;
import rikkei.edu.miniprojectss13.model.Employee;
import rikkei.edu.miniprojectss13.service.EmployeeService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    // TASK 1 Lấy danh sách tất cả nhân viên
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        log.info("GET /api/employees called");
        List<Employee> list = employeeService.getAllEmployees();
        return ResponseEntity.ok(list);
    }

    // TASK 2 GET chi tiết 1 nhân viên theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getById(@PathVariable Long id) {
        log.info("GET /api/employees/{} called", id);
        try {
            Employee emp = employeeService.getById(id);
            return ResponseEntity.ok(emp);              // 200 OK
        } catch (EmployeeNotFoundException e) {
            log.error("Employee not found with id: {}", id);
            return ResponseEntity.notFound().build();   // 404 Not Found
        }
    }

    // TASK 3 Thêm nhân viên mới
    @PostMapping
    public ResponseEntity<Employee> create(@Valid @RequestBody Employee payload) {
        log.info("POST /api/employees called");
        try {
            Employee created = employeeService.addEmployee(payload);
            return ResponseEntity.status(HttpStatus.CREATED).body(created); // 201 Created
        } catch (Exception e) {
            log.error("Error creating employee: {}", e.getMessage());
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }
    }

    // TASK 4 Cập nhật nhân viên theo ID
    @PutMapping("/{id}")
    public ResponseEntity<Employee> update(@PathVariable Long id, @Valid @RequestBody Employee payload) {
        log.info("PUT /api/employees/{} called", id);
        try {
            Employee updated = employeeService.updateEmployee(id, payload);
            return ResponseEntity.ok(updated); // 200 OK
        } catch (EmployeeNotFoundException e) {
            log.error("Employee not found with id: {}", id);
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

    // TASK 5 Xóa nhân viên theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/employees/{} called", id);
        try {
            employeeService.deleteById(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (EmployeeNotFoundException e) {
            log.error("Employee not found with id: {}", id);
            return ResponseEntity.notFound().build();  // 404 Not Found
        }
    }
}
