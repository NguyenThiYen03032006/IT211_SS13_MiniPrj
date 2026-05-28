package rikkei.edu.miniprojectss13.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rikkei.edu.miniprojectss13.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}

