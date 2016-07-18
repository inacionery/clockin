package org.clockin.service.impl;

import java.time.LocalDate;
import java.util.List;

import javax.inject.Inject;

import org.clockin.domain.Employee;
import org.clockin.domain.Workday;
import org.clockin.repository.WorkdayRepository;
import org.clockin.service.WorkdayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing Workday.
 */
@Service
@Transactional
public class WorkdayServiceImpl implements WorkdayService {

    private final Logger log = LoggerFactory
        .getLogger(WorkdayServiceImpl.class);

    @Inject
    private WorkdayRepository workdayRepository;

    /**
     * Save a workday.
     * 
     * @param workday the entity to save
     * @return the persisted entity
     */
    public Workday save(Workday workday) {
        log.debug("Request to save Workday : {}", workday);
        Workday result = workdayRepository.save(workday);
        return result;
    }

    /**
     *  Get all the workdays.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Workday> findAll() {
        log.debug("Request to get all Workdays");
        List<Workday> result = workdayRepository.findAll();
        return result;
    }

    /**
     *  Get one workday by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Workday findOne(Long id) {
        log.debug("Request to get Workday : {}", id);
        Workday workday = workdayRepository.findOne(id);
        return workday;
    }

    /**
     *  Delete the  workday by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Workday : {}", id);
        workdayRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public Workday findByEmployeeAndDate(Employee employee, LocalDate date) {
        log.debug("Search Workday by employee: {} and date {}", employee, date);
        return workdayRepository.findByEmployeeAndDate(employee, date);
    }

    @Transactional(readOnly = true)
    public List<Workday> findByEmployeeAndDateBetweenOrderByDate(
        Employee employee, LocalDate start, LocalDate end) {
        log.debug("Search Workday by employee: {} and date  between {} and {}",
            employee, start, end);
        return workdayRepository
            .findByEmployeeAndDateBetweenOrderByDate(employee, start, end);
    }
}
