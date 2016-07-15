package org.clockin.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.clockin.domain.Clockin;
import org.clockin.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Clockin entity.
 */
public interface ClockinRepository extends JpaRepository<Clockin, Long> {
    List<Clockin> findByEmployeeAndDateTimeBetweenOrderByDateTime(
        Employee employee, LocalDateTime start, LocalDateTime end);

    Clockin findByEmployeeAndDateTime(Employee employee,
        LocalDateTime dateTime);

}
