package org.clockin.web.rest;

import com.codahale.metrics.annotation.Timed;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.clockin.domain.Employee;
import org.clockin.service.EmployeeService;
import org.clockin.web.rest.util.HeaderUtil;
import org.clockin.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing Employee.
 */
@RestController
@RequestMapping("/api")
public class EmployeeResource {

    private final Logger log = LoggerFactory.getLogger(EmployeeResource.class);

    @Inject
    private EmployeeService employeeService;

    /**
     * POST  /employees : Create a new employee.
     *
     * @param employee the employee to create
     * @return the ResponseEntity with status 201 (Created) and with body the new employee, or with status 400 (Bad Request) if the employee has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/employees")
    @Timed
    public ResponseEntity<Employee> createEmployee(
        @RequestBody Employee employee) throws URISyntaxException {
        log.debug("REST request to save Employee : {}", employee);
        if (employee.getId() != null) {
            return ResponseEntity
                .badRequest().headers(HeaderUtil.createFailureAlert("employee",
                    "idexists", "A new employee cannot already have an ID"))
                .body(null);
        }

        if (employee.getUser() != null
            && employeeService.findByUser(employee.getUser()) != null) {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert("employee",
                    "useralreadyassociated",
                    "A new employee cannot already have User"))
                .body(null);
        }
        if (employeeService.findBySocialIdentificationNumber(
            employee.getSocialIdentificationNumber()) != null) {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert("employee",
                    "socialidentificationnumberreadyassociated",
                    "A new employee cannot already have an SocialIdentificationNumber"))
                .body(null);
        }
        Employee result = employeeService.save(employee);
        return ResponseEntity
            .created(new URI("/api/employees/" + result.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert("employee",
                    employee.getUser() != null ? result.getUser().getFirstName()
                        + " " + result.getUser().getLastName() : ""))
            .body(result);
    }

    /**
     * PUT  /employees : Updates an existing employee.
     *
     * @param employee the employee to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated employee,
     * or with status 400 (Bad Request) if the employee is not valid,
     * or with status 500 (Internal Server Error) if the employee couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/employees")
    @Timed
    public ResponseEntity<Employee> updateEmployee(
        @RequestBody Employee employee) throws URISyntaxException {
        log.debug("REST request to update Employee : {}", employee);
        if (employee.getId() == null) {
            return createEmployee(employee);
        }
        if (employee.getUser() != null) {
            Employee oldEmployee = employeeService
                .findByUser(employee.getUser());
            if (oldEmployee != null
                && oldEmployee.getId() != employee.getId()) {
                return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert("employee",
                        "useralreadyassociated",
                        "User already associated a employee"))
                    .body(null);
            }
        }
        Employee oldEmployee = employeeService.findBySocialIdentificationNumber(
            employee.getSocialIdentificationNumber());
        if (oldEmployee != null && oldEmployee.getId() != employee.getId()) {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert("employee",
                    "socialidentificationnumberreadyassociated",
                    "Social Identification Number already associated a employee"))
                .body(null);
        }
        Employee result = employeeService.save(employee);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("employee",
                employee.getUser() != null ? employee.getUser().getFirstName()
                    + " " + employee.getUser().getLastName() : ""))
            .body(result);
    }

    /**
     * GET  /employees : get all the employees.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of employees in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/employees")
    @Timed
    public ResponseEntity<List<Employee>> getAllEmployees(
        @RequestParam(value = "hidden") Optional<Integer> hiddenParam,
        Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get a page of Employees");

        int hidden = 0;
        if (hiddenParam.isPresent()) {
            hidden = hiddenParam.get();
        }

        Page<Employee> page;
        if (hidden == 0) {
            page = employeeService.findByHiddenIsFalse(pageable);
        }
        else {
            page = employeeService.findAll(pageable);
        }

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page,
            "/api/employees");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /employees/:id : get the "id" employee.
     *
     * @param id the id of the employee to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the employee, or with status 404 (Not Found)
     */
    @GetMapping("/employees/{id}")
    @Timed
    public ResponseEntity<Employee> getEmployee(@PathVariable Long id) {
        log.debug("REST request to get Employee : {}", id);
        Employee employee = employeeService.findOne(id);
        return Optional.ofNullable(employee)
            .map(result -> new ResponseEntity<>(result, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
