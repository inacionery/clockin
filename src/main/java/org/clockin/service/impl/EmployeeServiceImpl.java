package org.clockin.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.clockin.domain.Employee;
import org.clockin.domain.User;
import org.clockin.repository.EmployeeRepository;
import org.clockin.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing Employee.
 */
@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final Logger log = LoggerFactory
        .getLogger(EmployeeServiceImpl.class);

    @Inject
    private EmployeeRepository employeeRepository;

    /**
     * Save a employee.
     * 
     * @param employee the entity to save
     * @return the persisted entity
     */
    public Employee save(Employee employee) {
        log.debug("Request to save Employee : {}", employee);
        Employee result = employeeRepository.save(employee);
        return result;
    }

    /**
     *  Get all the employees.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Employee> findAll(Pageable pageable) {
        log.debug("Request to get all Employees");
        Page<Employee> result = employeeRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one employee by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Employee findOne(Long id) {
        log.debug("Request to get Employee : {}", id);
        Employee employee = employeeRepository
            .findOneWithEagerRelationships(id);
        return employee;
    }

    /**
     *  Delete the  employee by id.
     *  
     *  @param id the id of the entity
     * @return 
     */
    @Override
    public Employee delete(Long id) {
        log.debug("Request to delete Employee : {}", id);
        Employee employee = employeeRepository.findOne(id);
        employeeRepository.delete(id);
        return employee;
    }

    @Override
    public Employee findByUser(User user) {
        log.debug("Request to get Employee : {}", user);
        return employeeRepository.findByUser(user);
    }

    @Override
    public Employee findBySocialIdentificationNumber(
        String socialIdentificationNumber) {
        log.debug("Request to get Employee : {}", socialIdentificationNumber);
        return employeeRepository
            .findBySocialIdentificationNumber(socialIdentificationNumber);
    }

    @Transactional(readOnly = true)
    public Page<Employee> findByHiddenIsFalse(Pageable pageable) {
        log.debug("Request to get all Employees by hidden false");
        Page<Employee> result = employeeRepository
            .findByHiddenIsFalse(pageable);
        return result;
    }

    @Transactional(readOnly = true)
    public List<Employee> findByHiddenIsFalse() {
        log.debug("Request to get all Employees by hidden false");
        return employeeRepository.findByHiddenIsFalse();
    }

    @Transactional(readOnly = true)
    public List<Employee> findByManager(User manage) {
        log.debug("Request to get all Employees by manager : {}", manage);
        return employeeRepository.findByManager(manage);
    }
}
