package org.clockin.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.clockin.domain.Clockin;
import org.clockin.repository.ClockinRepository;
import org.clockin.repository.search.ClockinSearchRepository;
import org.clockin.web.rest.dto.WorkDayDTO;
import org.clockin.web.rest.util.HeaderUtil;
import org.clockin.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Clockin.
 */
@RestController
@RequestMapping("/api")
public class ClockinResource {

    private final Logger log = LoggerFactory.getLogger(ClockinResource.class);

    @Inject
    private ClockinRepository clockinRepository;

    @Inject
    private ClockinSearchRepository clockinSearchRepository;

    /**
     * POST  /clockins -> Create a new clockin.
     */
    @RequestMapping(value = "/clockins",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Clockin> createClockin(@RequestBody Clockin clockin) throws URISyntaxException {
        log.debug("REST request to save Clockin : {}", clockin);
        if (clockin.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new clockin cannot already have an ID").body(null);
        }
        Clockin result = clockinRepository.save(clockin);
        clockinSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/clockins/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("clockin", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /clockins -> Updates an existing clockin.
     */
    @RequestMapping(value = "/clockins",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Clockin> updateClockin(@RequestBody Clockin clockin) throws URISyntaxException {
        log.debug("REST request to update Clockin : {}", clockin);
        if (clockin.getId() == null) {
            return createClockin(clockin);
        }
        Clockin result = clockinRepository.save(clockin);
        clockinSearchRepository.save(clockin);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("clockin", clockin.getId().toString()))
            .body(result);
    }

    /**
     * GET  /clockins -> get all the clockins.
     */
    @RequestMapping(value = "/clockins",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Clockin>> getAllClockins(Pageable pageable)
        throws URISyntaxException {
        Page<Clockin> page = clockinRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/clockins");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/workdays",
    				method = RequestMethod.GET,
    				produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<WorkDayDTO> getAllWorkDays()
    				throws URISyntaxException {
    	List<Clockin> clockins = clockinRepository.findAll();
    	List<WorkDayDTO> workDays = new ArrayList<>();

    	WorkDayDTO workDayDTO = null;
    	for (Clockin clockin : clockins) {

    		if (workDayDTO == null || !workDayDTO.getDate().isEqual(clockin.getDate())) {
    			workDayDTO = new WorkDayDTO(clockin.getDate());
    			workDays.add(workDayDTO);
			}

    		workDayDTO.addClockinValues(clockin);
		}

    	return workDays;
    }

    /**
     * GET  /clockins/:id -> get the "id" clockin.
     */
    @RequestMapping(value = "/clockins/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Clockin> getClockin(@PathVariable Long id) {
        log.debug("REST request to get Clockin : {}", id);
        return Optional.ofNullable(clockinRepository.findOne(id))
            .map(clockin -> new ResponseEntity<>(
                clockin,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /clockins/:id -> delete the "id" clockin.
     */
    @RequestMapping(value = "/clockins/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteClockin(@PathVariable Long id) {
        log.debug("REST request to delete Clockin : {}", id);
        clockinRepository.delete(id);
        clockinSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("clockin", id.toString())).build();
    }

    /**
     * SEARCH  /_search/clockins/:query -> search for the clockin corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/clockins/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Clockin> searchClockins(@PathVariable String query) {
        return StreamSupport
            .stream(clockinSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
