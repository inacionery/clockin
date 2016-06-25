package org.clockin.web.rest;

import org.clockin.domain.Clockin;
import org.clockin.domain.Employee;
import org.clockin.domain.User;
import org.clockin.repository.UserRepository;
import org.clockin.security.SecurityUtils;
import org.clockin.service.ClockinService;
import org.clockin.service.EmployeeService;
import org.clockin.web.rest.dto.WorkDayDTO;
import org.clockin.web.rest.util.HeaderUtil;

import com.codahale.metrics.annotation.Timed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Clockin.
 */
@RestController
@RequestMapping("/api")
public class ClockinResource {

    private final Logger log = LoggerFactory.getLogger(ClockinResource.class);

    @Inject
    private ClockinService clockinService;

    @Inject
    private EmployeeService employeeService;

    @Inject
    private UserRepository userRepository;

    /**
     * POST  /clockins : Create a new clockin.
     *
     * @param clockin the clockin to create
     * @return the ResponseEntity with status 201 (Created) and with body the new clockin, or with status 400 (Bad Request) if the clockin has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/clockins",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Clockin> createClockin(@RequestBody Clockin clockin)
        throws URISyntaxException {
        log.debug("REST request to save Clockin : {}", clockin);
        if (clockin.getId() != null) {
            return ResponseEntity
                .badRequest().headers(HeaderUtil.createFailureAlert("clockin",
                    "idexists", "A new clockin cannot already have an ID"))
                .body(null);
        }
        Clockin result = clockinService.save(clockin);
        return ResponseEntity
            .created(new URI("/api/clockins/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("clockin",
                result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /clockins : Updates an existing clockin.
     *
     * @param clockin the clockin to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated clockin,
     * or with status 400 (Bad Request) if the clockin is not valid,
     * or with status 500 (Internal Server Error) if the clockin couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/clockins",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Clockin> updateClockin(@RequestBody Clockin clockin)
        throws URISyntaxException {
        log.debug("REST request to update Clockin : {}", clockin);
        if (clockin.getId() == null) {
            return createClockin(clockin);
        }
        Clockin result = clockinService.save(clockin);
        return ResponseEntity.ok().headers(HeaderUtil
            .createEntityUpdateAlert("clockin", clockin.getId().toString()))
            .body(result);
    }

    /**
     * GET  /clockins : get all the clockins.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of clockins in body
     */
    @RequestMapping(value = "/clockins",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Clockin> getAllClockins() {
        log.debug("REST request to get all Clockins");
        return clockinService.findAll();
    }

    /**
    * GET  /clockins/:id : get the "id" clockin.
    *
    * @param id the id of the clockin to retrieve
    * @return the ResponseEntity with status 200 (OK) and with body the clockin, or with status 404 (Not Found)
    */
    @RequestMapping(value = "/clockins/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Clockin> getClockin(@PathVariable Long id) {
        log.debug("REST request to get Clockin : {}", id);
        Clockin clockin = clockinService.findOne(id);
        return Optional.ofNullable(clockin)
            .map(result -> new ResponseEntity<>(result, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /clockins/:id : delete the "id" clockin.
     *
     * @param id the id of the clockin to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/clockins/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteClockin(@PathVariable Long id) {
        log.debug("REST request to delete Clockin : {}", id);
        clockinService.delete(id);
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityDeletionAlert("clockin", id.toString()))
            .build();
    }

    /**
     * SEARCH  /_search/clockins?query=:query : search for the clockin corresponding
     * to the query.
     *
     * @param query the query of the clockin search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/clockins",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Clockin> searchClockins(@RequestParam String query) {
        log.debug("REST request to search Clockins for query {}", query);
        return clockinService.search(query);
    }

    /**
     * GET /workdays/ /workdays/{year}/{month} -> get all workdays.
     * @throws ParseException
     */
    @RequestMapping(value = { "clockin", "/workdays/{year}/{month}" },
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<WorkDayDTO> getWorkDaysByYearMonthToTable(
        @PathVariable(value = "year") Optional<Integer> yearParam,
        @PathVariable(value = "month") Optional<Integer> monthParam)
        throws URISyntaxException, ParseException {

        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonth().getValue();

        if (yearParam.isPresent()) {
            year = yearParam.get();
        }
        if (monthParam.isPresent()) {
            month = monthParam.get();
        }
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = LocalDate.of(year, month,
            startDate.lengthOfMonth());

        ZonedDateTime start = ZonedDateTime.of(startDate, LocalTime.MIN,
            ZoneOffset.UTC);

        ZonedDateTime end = ZonedDateTime.of(endDate, LocalTime.MAX,
            ZoneOffset.UTC);

        Optional<User> user = userRepository
            .findOneByLogin(SecurityUtils.getCurrentUserLogin());
        Employee employee = employeeService.findByUser(user.get());

        List<WorkDayDTO> workDays = new ArrayList<>();

        if (employee != null) {
            List<Clockin> clockins = clockinService
                .findByEmployeeDatesBetween(employee, start, end);

            int clockinCount = 0;
            for (int i = 1; i <= startDate.lengthOfMonth(); i++) {
                LocalDate curDate = LocalDate.of(year, month, i);
                WorkDayDTO workDayDTO = new WorkDayDTO(curDate, employee);

                for (int j = clockinCount; j < clockins.size(); j++) {
                    Clockin clockin = clockins.get(j);

                    if (!workDayDTO.getDate().isEqual(clockin.getDate())) {
                        clockinCount = j;
                        break;
                    }

                    workDayDTO.addClockinValues(clockin);
                }
                workDays.add(workDayDTO);
            }
        }

        return workDays;
    }

    /**
     * GET -> get all workdays.
     * @throws ParseException
     */
    @RequestMapping(value = { "clockin", "/workdays-calendar/{year}/{month}" },
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<WorkDayDTO> getWorkDaysByYearMonthToCalendar(
        @PathVariable(value = "year") Optional<Integer> yearParam,
        @PathVariable(value = "month") Optional<Integer> monthParam)
        throws URISyntaxException, ParseException {

        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonth().getValue();

        if (yearParam.isPresent()) {
            year = yearParam.get();
        }
        if (monthParam.isPresent()) {
            month = monthParam.get();
        }
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = LocalDate.of(year, month,
            startDate.lengthOfMonth());

        ZonedDateTime start = ZonedDateTime.of(startDate, LocalTime.MIN,
            ZoneOffset.UTC);

        ZonedDateTime end = ZonedDateTime.of(endDate, LocalTime.MAX,
            ZoneOffset.UTC);

        Optional<User> user = userRepository
            .findOneByLogin(SecurityUtils.getCurrentUserLogin());
        Employee employee = employeeService.findByUser(user.get());

        List<WorkDayDTO> workDays = new ArrayList<>();

        int diff = startDate.getDayOfWeek().getValue() == 7 ? 0
            : startDate.getDayOfWeek().getValue();
        LocalDate previousDate = endDate.minusMonths(1);
        for (int i = 0; i < diff; i++) {
            LocalDate curDate = previousDate.minusDays(diff - i - 1);
            WorkDayDTO workDayDTO = new WorkDayDTO(curDate, employee);
            workDays.add(workDayDTO);
        }

        if (employee != null) {
            List<Clockin> clockins = clockinService
                .findByEmployeeDatesBetween(employee, start, end);

            int clockinCount = 0;
            for (int i = 1; i <= startDate.lengthOfMonth(); i++) {
                LocalDate curDate = LocalDate.of(year, month, i);
                WorkDayDTO workDayDTO = new WorkDayDTO(curDate, employee);

                for (int j = clockinCount; j < clockins.size(); j++) {
                    Clockin clockin = clockins.get(j);

                    if (!workDayDTO.getDate().isEqual(clockin.getDate())) {
                        clockinCount = j;
                        break;
                    }

                    workDayDTO.addClockinValues(clockin);
                }
                workDays.add(workDayDTO);
            }
        }

        diff = 42 - workDays.size();
        LocalDate nextStartDate = startDate.plusMonths(1).withDayOfMonth(1);
        for (int i = 0; i < diff; i++) {
            LocalDate curDate = nextStartDate.plusDays(i);
            WorkDayDTO workDayDTO = new WorkDayDTO(curDate, employee);
            workDays.add(workDayDTO);
        }

        return workDays;
    }

}
