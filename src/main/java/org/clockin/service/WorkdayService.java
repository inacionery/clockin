package org.clockin.service;

import java.time.LocalDate;
import java.util.List;

import org.clockin.domain.Employee;
import org.clockin.domain.Workday;

/**
 * Service Interface for managing Workday.
 */
public interface WorkdayService {

    /**
     * Save a workday.
     *
     * @param workday the entity to save
     * @return the persisted entity
     */
    Workday save(Workday workday);

    /**
     *  Get all the workdays.
     *  
     *  @return the list of entities
     */
    List<Workday> findAll();

    /**
     *  Get the "id" workday.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Workday findOne(Long id);

    /**
     *  Delete the "id" workday.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    Workday findByEmployeeAndDate(Employee employee, LocalDate date);

    List<Workday> findByEmployeeAndDateBetweenOrderByDate(Employee employee,
        LocalDate start, LocalDate end);
}
