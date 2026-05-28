package rikkei.edu.miniprojectss13.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rikkei.edu.miniprojectss13.model.Employee;
import rikkei.edu.miniprojectss13.repository.EmployeeRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    /**
     * Test case 1: getAllEmployees_ReturnList
     * Trả về danh sách không rỗng khi có dữ liệu
     */
    @Test
    void getAllEmployees_ReturnList() {
        // Arrange
        List<Employee> employees = List.of(
                new Employee(1L, "Nguyen Van A", "Engineering", 15000000.0),
                new Employee(2L, "Tran Thi B", "Human Resources", 12000000.0)
        );
        when(employeeRepository.findAll()).thenReturn(employees);

        // Act
        List<Employee> result = employeeService.getAllEmployees();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(employeeRepository, times(1)).findAll();
    }

    /**
     * Test case 2: getById_Found
     * Trả về đúng Employee khi ID tồn tại
     */
    @Test
    void getById_Found() {
        // Arrange
        Employee employee = new Employee(1L, "Nguyen Van A", "Engineering", 15000000.0);
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        // Act
        Employee result = employeeService.getById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Nguyen Van A", result.getFullName());
        verify(employeeRepository, times(1)).findById(1L);
    }

    /**
     * Test case 3: getById_NotFound_ThrowException
     * Ném RuntimeException khi ID không tồn tại
     */
    @Test
    void getById_NotFound_ThrowException() {
        // Arrange
        when(employeeRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            employeeService.getById(999L);
        });

        assertTrue(exception.getMessage().contains("Không tìm thấy"));
        verify(employeeRepository, times(1)).findById(999L);
    }

    /**
     * Test case 4: addEmployee_Success
     * Thêm thành công và trả về employee vừa tạo
     */
    @Test
    void addEmployee_Success() {
        // Arrange
        Employee payload = new Employee(null, "Le Van C", "Finance", 13500000.0);
        Employee savedEmployee = new Employee(3L, "Le Van C", "Finance", 13500000.0);
        when(employeeRepository.save(any(Employee.class))).thenReturn(savedEmployee);

        // Act
        Employee result = employeeService.addEmployee(payload);

        // Assert
        assertNotNull(result);
        assertEquals(3L, result.getId());
        assertEquals("Le Van C", result.getFullName());
        assertEquals("Finance", result.getDepartment());
        assertEquals(13500000.0, result.getSalary());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    /**
     * Test case 5: deleteEmployee_RemovesCorrectElement
     * Gọi remove đúng phần tử khi ID hợp lệ
     */
    @Test
    void deleteEmployee_RemovesCorrectElement() {
        // Arrange
        Long id = 1L;
        when(employeeRepository.existsById(id)).thenReturn(true);
        doNothing().when(employeeRepository).deleteById(id);

        // Act
        employeeService.deleteById(id);

        // Assert
        verify(employeeRepository, times(1)).existsById(id);
        verify(employeeRepository, times(1)).deleteById(id);
    }
}

