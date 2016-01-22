package org.clockin.repository;

import org.clockin.domain.Employee;

import org.clockin.domain.User;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Employee entity.
 */
public interface EmployeeRepository extends JpaRepository<Employee,Long> {

    Employee findByUser(User user);
    Employee findByUserId(long userId);
}
