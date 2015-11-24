package org.clockin.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.clockin.domain.Employee;
import org.clockin.repository.EmployeeRepository;
import org.clockin.repository.search.EmployeeSearchRepository;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Employee.
 */
@RestController
@RequestMapping("/api")
public class EmployeeResource {

    private final Logger log = LoggerFactory.getLogger(EmployeeResource.class);

    @Inject
    private EmployeeRepository employeeRepository;

    @Inject
    private EmployeeSearchRepository employeeSearchRepository;

    /**
     * POST  /employees -> Create a new employee.
     */
    @RequestMapping(value = "/employees",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) throws URISyntaxException {
        log.debug("REST request to save Employee : {}", employee);
        if (employee.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new employee cannot already have an ID").body(null);
        }
        Employee result = employeeRepository.save(employee);
        employeeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/employees/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("employee", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /employees -> Updates an existing employee.
     */
    @RequestMapping(value = "/employees",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Employee> updateEmployee(@RequestBody Employee employee) throws URISyntaxException {
        log.debug("REST request to update Employee : {}", employee);
        if (employee.getId() == null) {
            return createEmployee(employee);
        }
        Employee result = employeeRepository.save(employee);
        employeeSearchRepository.save(employee);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("employee", employee.getId().toString()))
            .body(result);
    }

    /**
     * GET  /employees -> get all the employees.
     */
    @RequestMapping(value = "/employees",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Employee>> getAllEmployees(Pageable pageable, @RequestParam(required = false) String filter)
        throws URISyntaxException {
        if ("user-is-null".equals(filter)) {
            log.debug("REST request to get all Employees where user is null");
            return new ResponseEntity<>(StreamSupport
                .stream(employeeRepository.findAll().spliterator(), false)
                .filter(employee -> employee.getUser() == null)
                .collect(Collectors.toList()), HttpStatus.OK);
        }
        
        Page<Employee> page = employeeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/employees");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /employees/:id -> get the "id" employee.
     */
    @RequestMapping(value = "/employees/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Employee> getEmployee(@PathVariable Long id) {
        log.debug("REST request to get Employee : {}", id);
        return Optional.ofNullable(employeeRepository.findOne(id))
            .map(employee -> new ResponseEntity<>(
                employee,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /employees/:id -> delete the "id" employee.
     */
    @RequestMapping(value = "/employees/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        log.debug("REST request to delete Employee : {}", id);
        employeeRepository.delete(id);
        employeeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("employee", id.toString())).build();
    }

    /**
     * SEARCH  /_search/employees/:query -> search for the employee corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/employees/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Employee> searchEmployees(@PathVariable String query) {
        return StreamSupport
            .stream(employeeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
