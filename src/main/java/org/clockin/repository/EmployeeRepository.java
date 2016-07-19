package org.clockin.repository;

import org.clockin.domain.Employee;
import org.clockin.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Employee entity.
 */
@SuppressWarnings("unused")
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findByUser(User user);

    Employee findBySocialIdentificationNumber(
        String socialIdentificationNumber);
}
