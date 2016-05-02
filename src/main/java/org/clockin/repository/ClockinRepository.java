package org.clockin.repository;

import org.clockin.domain.Clockin;
import org.clockin.domain.Employee;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for the Clockin entity.
 */
public interface ClockinRepository extends JpaRepository<Clockin, Long> {
    List<Clockin> findByEmployeeAndDateTimeBetweenOrderByDateTime(
        Employee employee, ZonedDateTime start, ZonedDateTime end);

}
