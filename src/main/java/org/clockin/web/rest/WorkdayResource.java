package org.clockin.web.rest;

import com.codahale.metrics.annotation.Timed;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.clockin.domain.Workday;
import org.clockin.service.WorkdayService;
import org.clockin.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing Workday.
 */
@RestController
@RequestMapping("/api")
public class WorkdayResource {

    private final Logger log = LoggerFactory.getLogger(WorkdayResource.class);

    @Inject
    private WorkdayService workdayService;

    /**
     * POST  /workdays : Create a new workday.
     *
     * @param workday the workday to create
     * @return the ResponseEntity with status 201 (Created) and with body the new workday, or with status 400 (Bad Request) if the workday has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/workdays")
    @Timed
    public ResponseEntity<Workday> createWorkday(@RequestBody Workday workday)
        throws URISyntaxException {
        log.debug("REST request to save Workday : {}", workday);
        if (workday.getId() != null) {
            return ResponseEntity
                .badRequest().headers(HeaderUtil.createFailureAlert("workday",
                    "idexists", "A new workday cannot already have an ID"))
                .body(null);
        }
        Workday result = workdayService.save(workday);
        return ResponseEntity
            .created(new URI("/api/workdays/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("workday",
                result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /workdays : Updates an existing workday.
     *
     * @param workday the workday to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated workday,
     * or with status 400 (Bad Request) if the workday is not valid,
     * or with status 500 (Internal Server Error) if the workday couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/workdays")
    @Timed
    public ResponseEntity<Workday> updateWorkday(@RequestBody Workday workday)
        throws URISyntaxException {
        log.debug("REST request to update Workday : {}", workday);
        if (workday.getId() == null) {
            return createWorkday(workday);
        }
        Workday result = workdayService.save(workday);
        return ResponseEntity.ok().headers(HeaderUtil
            .createEntityUpdateAlert("workday", workday.getId().toString()))
            .body(result);
    }

    /**
     * GET  /workdays : get all the workdays.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of workdays in body
     */
    @GetMapping("/workdays")
    @Timed
    public List<Workday> getAllWorkdays() {
        log.debug("REST request to get all Workdays");
        return workdayService.findAll();
    }

    /**
     * GET  /workdays/:id : get the "id" workday.
     *
     * @param id the id of the workday to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the workday, or with status 404 (Not Found)
     */
    @GetMapping("/workdays/{id}")
    @Timed
    public ResponseEntity<Workday> getWorkday(@PathVariable Long id) {
        log.debug("REST request to get Workday : {}", id);
        Workday workday = workdayService.findOne(id);
        return Optional.ofNullable(workday)
            .map(result -> new ResponseEntity<>(result, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /workdays/:id : delete the "id" workday.
     *
     * @param id the id of the workday to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/workdays/{id}")
    @Timed
    public ResponseEntity<Void> deleteWorkday(@PathVariable Long id) {
        log.debug("REST request to delete Workday : {}", id);
        workdayService.delete(id);
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityDeletionAlert("workday", id.toString()))
            .build();
    }

}
