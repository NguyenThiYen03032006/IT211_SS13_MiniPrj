package rikkei.edu.miniprojectss13.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import rikkei.edu.miniprojectss13.exception.EmployeeNotFoundException;
import rikkei.edu.miniprojectss13.model.Employee;
import rikkei.edu.miniprojectss13.service.EmployeeService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    // Test case 1: GET /api/employees – trả về HTTP 200 và danh sách JSON
    @Test
    void getAllEmployees_shouldReturnStatus200AndJsonList() throws Exception {
        List<Employee> employees = List.of(
                new Employee(1L, "Nguyen Van A", "Engineering", 15000000.0),
                new Employee(2L, "Tran Thi B", "Human Resources", 12000000.0)
        );

        when(employeeService.getAllEmployees()).thenReturn(employees);

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].fullName").value("Nguyen Van A"))
                .andExpect(jsonPath("$[0].department").value("Engineering"))
                .andExpect(jsonPath("$[0].salary").value(15000000.0));
    }

    // Test case 2: GET /api/employees/{id} – trả về HTTP 200 khi tìm thấy
    @Test
    void getEmployeeById_shouldReturnStatus200_whenEmployeeFound() throws Exception {
        Employee employee = new Employee(1L, "Nguyen Van A", "Engineering", 15000000.0);

        when(employeeService.getById(1L)).thenReturn(employee);

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.fullName").value("Nguyen Van A"))
                .andExpect(jsonPath("$.department").value("Engineering"))
                .andExpect(jsonPath("$.salary").value(15000000.0));
    }

    // Test case 3: GET /api/employees/{id} – trả về HTTP 404 khi không tìm thấy
    @Test
    void getEmployeeById_shouldReturnStatus404_whenEmployeeNotFound() throws Exception {
        when(employeeService.getById(999L))
                .thenThrow(new EmployeeNotFoundException(999L));

        mockMvc.perform(get("/api/employees/999"))
                .andExpect(status().isNotFound());
    }

    // Test case 4: POST /api/employees – trả về HTTP 201 sau khi tạo thành công
    @Test
    void addEmployee_shouldReturnStatus201_whenCreatedSuccessfully() throws Exception {
        Employee payload = new Employee(null, "Le Van C", "Finance", 13500000.0);
        Employee savedEmployee = new Employee(6L, "Le Van C", "Finance", 13500000.0);

        when(employeeService.addEmployee(any(Employee.class))).thenReturn(savedEmployee);

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(6))
                .andExpect(jsonPath("$.fullName").value("Le Van C"))
                .andExpect(jsonPath("$.department").value("Finance"))
                .andExpect(jsonPath("$.salary").value(13500000.0));
    }
}

