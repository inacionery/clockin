package org.clockin.service;

import java.util.List;

import org.clockin.domain.Employee;
import org.clockin.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Employee.
 */
public interface EmployeeService {

    /**
     * Save a employee.
     * 
     * @param employee the entity to save
     * @return the persisted entity
     */
    Employee save(Employee employee);

    /**
     *  Get all the employees.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Employee> findAll(Pageable pageable);

    /**
     *  Get the "id" employee.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    Employee findOne(Long id);

    /**
     *  Delete the "id" employee.
     *  
     *  @param id the id of the entity
     * @return 
     */
    Employee delete(Long id);

    /**
     * Search for the employee corresponding to the query.
     * 
     *  @param query the query of the search
     *  @return the list of entities
     */
    Page<Employee> search(String query, Pageable pageable);

    Employee findByUser(User user);

    Employee findBySocialIdentificationNumber(
        String socialIdentificationNumber);
}
