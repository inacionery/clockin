package org.clockin.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import org.clockin.domain.Clockin;
import org.clockin.domain.Employee;
import org.clockin.repository.ClockinRepository;
import org.clockin.repository.search.ClockinSearchRepository;
import org.clockin.service.ClockinService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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

    @Inject
    private ClockinSearchRepository clockinSearchRepository;

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
        clockinSearchRepository.save(result);
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
        clockinSearchRepository.delete(id);
    }

    /**
     * Search for the clockin corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Clockin> search(String query) {
        log.debug("Request to search Clockins for query {}", query);
        return StreamSupport.stream(clockinSearchRepository
            .search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Clockin> findByEmployee(Employee employee) {
        log.debug("Request to get Clockin : {}", employee);
        List<Clockin> result = clockinRepository.findByEmployee(employee);
        return result;
    }
}
