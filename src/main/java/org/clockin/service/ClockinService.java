package org.clockin.service;

import org.clockin.domain.Clockin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

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
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Clockin> findAll(Pageable pageable);

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

    /**
     * Search for the clockin corresponding to the query.
     * 
     *  @param query the query of the search
     *  @return the list of entities
     */
    Page<Clockin> search(String query, Pageable pageable);
}
