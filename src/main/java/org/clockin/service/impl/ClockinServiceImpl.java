package org.clockin.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import javax.inject.Inject;

import org.clockin.domain.Clockin;
import org.clockin.domain.Employee;
import org.clockin.repository.ClockinRepository;
import org.clockin.service.ClockinService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing Clockin.
 */
@Service
@Transactional
public class ClockinServiceImpl implements ClockinService {

    private final Logger log = LoggerFactory
        .getLogger(ClockinServiceImpl.class);

    @Inject
    private ClockinRepository clockinRepository;

    /**
     * Save a clockin.
     *
     * @param clockin the entity to save
     * @return the persisted entity
     */
    @Override
    public Clockin save(Clockin clockin) {
        log.debug("Request to save Clockin : {}", clockin);
        Clockin result = clockinRepository.save(clockin);
        return result;
    }

    /**
     *  Get all the clockins.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Clockin> findAll() {
        log.debug("Request to get all Clockins");
        List<Clockin> result = clockinRepository.findAll();
        return result;
    }

    /**
     *  Get one clockin by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Clockin findOne(Long id) {
        log.debug("Request to get Clockin : {}", id);
        Clockin clockin = clockinRepository.findOne(id);
        return clockin;
    }

    /**
     *  Delete the  clockin by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Clockin : {}", id);
        clockinRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Clockin> findByEmployeeDatesBetween(Employee employee,
        LocalDateTime start, LocalDateTime end) {
        log.debug("Request to get Clockin : {} Between {} and {}", employee,
            start, end);
        List<Clockin> result = clockinRepository
            .findByEmployeeAndDateTimeBetweenOrderByDateTime(employee, start,
                end);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Clockin findByEmployeeAndDateTime(Employee employee,
        LocalDateTime dateTime) {
        log.debug("Request to get Clockin : {} DateTime {}", employee,
            dateTime);
        Clockin result = clockinRepository.findByEmployeeAndDateTime(employee,
            dateTime);
        return result;
    }
}
