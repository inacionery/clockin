package org.clockin.repository;

import java.time.LocalDate;
import java.util.List;

import org.clockin.domain.Employee;
import org.clockin.domain.Workday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for the Workday entity.
 */
@SuppressWarnings("unused")
public interface WorkdayRepository extends JpaRepository<Workday, Long> {

    Workday findByEmployeeAndDate(Employee employee, LocalDate date);

    List<Workday> findByEmployeeAndDateBetweenOrderByDate(Employee employee,
        LocalDate start, LocalDate end);

    @Query("SELECT sum(workday.workDone) as workDone, MONTH(workday.date) FROM Workday workday where workday.date between :start and :end and workday.employee = :employee GROUP BY MONTH(workday.date)")
    List<Object[]> getSumWorkDoneByEmployeeAndDateBetween(
        @Param("employee") Employee employee, @Param("start") LocalDate start,
        @Param("end") LocalDate end);
}
