package org.clockin.repository;

import java.util.List;

import org.clockin.domain.Employee;
import org.clockin.domain.User;
import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the Employee entity.
 */
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findByUser(User user);

    Employee findBySocialIdentificationNumber(
        String socialIdentificationNumber);
}
