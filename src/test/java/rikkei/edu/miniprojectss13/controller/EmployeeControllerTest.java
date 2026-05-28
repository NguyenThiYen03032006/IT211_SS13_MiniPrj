package rikkei.edu.miniprojectss13.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import rikkei.edu.miniprojectss13.exception.EmployeeNotFoundException;
import rikkei.edu.miniprojectss13.model.Employee;
import rikkei.edu.miniprojectss13.service.EmployeeService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllEmployees_shouldReturnStatus200AndEmployeeList() throws Exception {

        List<Employee> employees = List.of(
                new Employee(1L, "Nguyen Van A", "Engineering", 15000000.0),
                new Employee(2L, "Tran Thi B", "HR", 12000000.0)
        );

        when(employeeService.getAllEmployees()).thenReturn(employees);

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getEmployeeById_shouldReturn200() throws Exception {

        Employee employee =
                new Employee(1L, "Nguyen Van A", "Engineering", 15000000.0);

        when(employeeService.getById(1L)).thenReturn(employee);

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.fullName").value("Nguyen Van A"));
    }

    @Test
    void getEmployeeById_shouldReturn404() throws Exception {

        when(employeeService.getById(999L))
                .thenThrow(new EmployeeNotFoundException(999L));

        mockMvc.perform(get("/api/employees/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createEmployee_shouldReturn201() throws Exception {

        Employee payload =
                new Employee(null, "Le Van C", "Finance", 13500000.0);

        Employee savedEmployee =
                new Employee(3L, "Le Van C", "Finance", 13500000.0);

        when(employeeService.addEmployee(any(Employee.class)))
                .thenReturn(savedEmployee);

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3));
    }

    @Test
    void updateEmployee_shouldReturn200() throws Exception {

        Employee payload =
                new Employee(null, "Nguyen Van D", "Marketing", 18000000.0);

        Employee updatedEmployee =
                new Employee(1L, "Nguyen Van D", "Marketing", 18000000.0);

        when(employeeService.updateEmployee(any(Long.class), any(Employee.class)))
                .thenReturn(updatedEmployee);

        mockMvc.perform(put("/api/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Nguyen Van D"));
    }

    @Test
    void deleteEmployee_shouldReturn204() throws Exception {

        doNothing().when(employeeService).deleteById(1L);

        mockMvc.perform(delete("/api/employees/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteEmployee_shouldReturn404() throws Exception {

        doThrow(new EmployeeNotFoundException(999L))
                .when(employeeService)
                .deleteById(999L);

        mockMvc.perform(delete("/api/employees/999"))
                .andExpect(status().isNotFound());
    }
}