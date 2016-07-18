package org.clockin.repository;

import java.time.LocalDate;
import java.util.List;

import org.clockin.domain.Employee;
import org.clockin.domain.Workday;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Workday entity.
 */
public interface WorkdayRepository extends JpaRepository<Workday, Long> {

    Workday findByEmployeeAndDate(Employee employee, LocalDate date);

    List<Workday> findByEmployeeAndDateBetweenOrderByDate(Employee employee,
        LocalDate start, LocalDate end);
}
