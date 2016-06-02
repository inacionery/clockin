package org.clockin.web.rest;

import org.clockin.ClockinApp;
import org.clockin.domain.Employee;
import org.clockin.repository.EmployeeRepository;
import org.clockin.service.EmployeeService;
import org.clockin.repository.search.EmployeeSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the EmployeeResource REST controller.
 *
 * @see EmployeeResource
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = ClockinApp.class)
//@WebAppConfiguration
//@IntegrationTest
public class EmployeeResourceIntTest {

    private static final String DEFAULT_SOCIAL_IDENTIFICATION_NUMBER = "AAAAA";
    private static final String UPDATED_SOCIAL_IDENTIFICATION_NUMBER = "BBBBB";

    private static final Integer DEFAULT_PLANNED_DAILY_HOURS = 1;
    private static final Integer UPDATED_PLANNED_DAILY_HOURS = 2;

    @Inject
    private EmployeeRepository employeeRepository;

    @Inject
    private EmployeeService employeeService;

    @Inject
    private EmployeeSearchRepository employeeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restEmployeeMockMvc;

    private Employee employee;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EmployeeResource employeeResource = new EmployeeResource();
        ReflectionTestUtils.setField(employeeResource, "employeeService",
            employeeService);
        this.restEmployeeMockMvc = MockMvcBuilders
            .standaloneSetup(employeeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        employeeSearchRepository.deleteAll();
        employee = new Employee();
        employee.setSocialIdentificationNumber(
            DEFAULT_SOCIAL_IDENTIFICATION_NUMBER);
        employee.setPlannedDailyHours(DEFAULT_PLANNED_DAILY_HOURS);
    }

    //@Test
    @Transactional
    public void createEmployee() throws Exception {
        int databaseSizeBeforeCreate = employeeRepository.findAll().size();

        // Create the Employee

        restEmployeeMockMvc
            .perform(post("/api/employees")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(employee)))
            .andExpect(status().isCreated());

        // Validate the Employee in the database
        List<Employee> employees = employeeRepository.findAll();
        assertThat(employees).hasSize(databaseSizeBeforeCreate + 1);
        Employee testEmployee = employees.get(employees.size() - 1);
        assertThat(testEmployee.getSocialIdentificationNumber())
            .isEqualTo(DEFAULT_SOCIAL_IDENTIFICATION_NUMBER);
        assertThat(testEmployee.getPlannedDailyHours())
            .isEqualTo(DEFAULT_PLANNED_DAILY_HOURS);

        // Validate the Employee in ElasticSearch
        Employee employeeEs = employeeSearchRepository
            .findOne(testEmployee.getId());
        assertThat(employeeEs).isEqualToComparingFieldByField(testEmployee);
    }

    //@Test
    @Transactional
    public void getAllEmployees() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employees
        restEmployeeMockMvc.perform(get("/api/employees?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id")
                .value(hasItem(employee.getId().intValue())))
            .andExpect(jsonPath("$.[*].socialIdentificationNumber").value(
                hasItem(DEFAULT_SOCIAL_IDENTIFICATION_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].plannedDailyHours")
                .value(hasItem(DEFAULT_PLANNED_DAILY_HOURS)));
    }

    //@Test
    @Transactional
    public void getEmployee() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get the employee
        restEmployeeMockMvc
            .perform(get("/api/employees/{id}", employee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(employee.getId().intValue()))
            .andExpect(jsonPath("$.socialIdentificationNumber")
                .value(DEFAULT_SOCIAL_IDENTIFICATION_NUMBER.toString()))
            .andExpect(jsonPath("$.plannedDailyHours")
                .value(DEFAULT_PLANNED_DAILY_HOURS));
    }

    //@Test
    @Transactional
    public void getNonExistingEmployee() throws Exception {
        // Get the employee
        restEmployeeMockMvc.perform(get("/api/employees/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    //@Test
    @Transactional
    public void updateEmployee() throws Exception {
        // Initialize the database
        employeeService.save(employee);

        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();

        // Update the employee
        Employee updatedEmployee = new Employee();
        updatedEmployee.setId(employee.getId());
        updatedEmployee.setSocialIdentificationNumber(
            UPDATED_SOCIAL_IDENTIFICATION_NUMBER);
        updatedEmployee.setPlannedDailyHours(UPDATED_PLANNED_DAILY_HOURS);

        restEmployeeMockMvc
            .perform(put("/api/employees")
                .contentType(TestUtil.APPLICATION_JSON_UTF8).content(
                    TestUtil.convertObjectToJsonBytes(updatedEmployee)))
            .andExpect(status().isOk());

        // Validate the Employee in the database
        List<Employee> employees = employeeRepository.findAll();
        assertThat(employees).hasSize(databaseSizeBeforeUpdate);
        Employee testEmployee = employees.get(employees.size() - 1);
        assertThat(testEmployee.getSocialIdentificationNumber())
            .isEqualTo(UPDATED_SOCIAL_IDENTIFICATION_NUMBER);
        assertThat(testEmployee.getPlannedDailyHours())
            .isEqualTo(UPDATED_PLANNED_DAILY_HOURS);

        // Validate the Employee in ElasticSearch
        Employee employeeEs = employeeSearchRepository
            .findOne(testEmployee.getId());
        assertThat(employeeEs).isEqualToComparingFieldByField(testEmployee);
    }

    //@Test
    @Transactional
    public void deleteEmployee() throws Exception {
        // Initialize the database
        employeeService.save(employee);

        int databaseSizeBeforeDelete = employeeRepository.findAll().size();

        // Get the employee
        restEmployeeMockMvc
            .perform(delete("/api/employees/{id}", employee.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean employeeExistsInEs = employeeSearchRepository
            .exists(employee.getId());
        assertThat(employeeExistsInEs).isFalse();

        // Validate the database is empty
        List<Employee> employees = employeeRepository.findAll();
        assertThat(employees).hasSize(databaseSizeBeforeDelete - 1);
    }

    //@Test
    @Transactional
    public void searchEmployee() throws Exception {
        // Initialize the database
        employeeService.save(employee);

        // Search the employee
        restEmployeeMockMvc
            .perform(get("/api/_search/employees?query=id:" + employee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id")
                .value(hasItem(employee.getId().intValue())))
            .andExpect(jsonPath("$.[*].socialIdentificationNumber").value(
                hasItem(DEFAULT_SOCIAL_IDENTIFICATION_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].plannedDailyHours")
                .value(hasItem(DEFAULT_PLANNED_DAILY_HOURS)));
    }
}
