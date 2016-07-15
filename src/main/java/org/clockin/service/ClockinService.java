package org.clockin.service;

import java.time.LocalDateTime;
import java.util.List;

import org.clockin.domain.Clockin;
import org.clockin.domain.Employee;

/**
 * Service Interface for managing Clockin.
 */
public interface ClockinService {

    /**
     * Save a clockin.
     *
     * @param clockin the entity to save
     * @return the persisted entity
     */
    Clockin save(Clockin clockin);

    /**
     *  Get all the clockins.
     *
     *  @return the list of entities
     */
    List<Clockin> findAll();

    /**
     *  Get the "id" clockin.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Clockin findOne(Long id);

    /**
     *  Delete the "id" clockin.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    List<Clockin> findByEmployeeDatesBetween(Employee employee,
        LocalDateTime start, LocalDateTime end);

    Clockin findByEmployeeAndDateTime(Employee employee,
        LocalDateTime dateTime);
}
