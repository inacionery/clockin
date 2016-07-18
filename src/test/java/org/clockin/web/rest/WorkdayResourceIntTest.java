package org.clockin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.clockin.ClockinApp;
import org.clockin.domain.Workday;
import org.clockin.repository.WorkdayRepository;
import org.clockin.service.WorkdayService;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test class for the WorkdayResource REST controller.
 *
 * @see WorkdayResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ClockinApp.class)
@WebAppConfiguration
@IntegrationTest
public class WorkdayResourceIntTest {

    private static final Long DEFAULT_WORK_PLANNED = 1L;
    private static final Long UPDATED_WORK_PLANNED = 2L;

    private static final Long DEFAULT_WORK_DONE = 1L;
    private static final Long UPDATED_WORK_DONE = 2L;

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate
        .now(ZoneId.systemDefault());

    @Inject
    private WorkdayRepository workdayRepository;

    @Inject
    private WorkdayService workdayService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restWorkdayMockMvc;

    private Workday workday;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        WorkdayResource workdayResource = new WorkdayResource();
        ReflectionTestUtils.setField(workdayResource, "workdayService",
            workdayService);
        this.restWorkdayMockMvc = MockMvcBuilders
            .standaloneSetup(workdayResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        workday = new Workday();
        workday.setWorkPlanned(DEFAULT_WORK_PLANNED);
        workday.setWorkDone(DEFAULT_WORK_DONE);
        workday.setDate(DEFAULT_DATE);
    }

    //@Test
    @Transactional
    public void createWorkday() throws Exception {
        int databaseSizeBeforeCreate = workdayRepository.findAll().size();

        // Create the Workday

        restWorkdayMockMvc
            .perform(post("/api/workdays")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(workday)))
            .andExpect(status().isCreated());

        // Validate the Workday in the database
        List<Workday> workdays = workdayRepository.findAll();
        assertThat(workdays).hasSize(databaseSizeBeforeCreate + 1);
        Workday testWorkday = workdays.get(workdays.size() - 1);
        assertThat(testWorkday.getWorkPlanned())
            .isEqualTo(DEFAULT_WORK_PLANNED);
        assertThat(testWorkday.getWorkDone()).isEqualTo(DEFAULT_WORK_DONE);
        assertThat(testWorkday.getDate()).isEqualTo(DEFAULT_DATE);
    }

    //@Test
    @Transactional
    public void getAllWorkdays() throws Exception {
        // Initialize the database
        workdayRepository.saveAndFlush(workday);

        // Get all the workdays
        restWorkdayMockMvc.perform(get("/api/workdays?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(
                jsonPath("$.[*].id").value(hasItem(workday.getId().intValue())))
            .andExpect(jsonPath("$.[*].workPlanned")
                .value(hasItem(DEFAULT_WORK_PLANNED.intValue())))
            .andExpect(jsonPath("$.[*].workDone")
                .value(hasItem(DEFAULT_WORK_DONE.intValue())))
            .andExpect(
                jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    //@Test
    @Transactional
    public void getWorkday() throws Exception {
        // Initialize the database
        workdayRepository.saveAndFlush(workday);

        // Get the workday
        restWorkdayMockMvc.perform(get("/api/workdays/{id}", workday.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(workday.getId().intValue()))
            .andExpect(jsonPath("$.workPlanned")
                .value(DEFAULT_WORK_PLANNED.intValue()))
            .andExpect(
                jsonPath("$.workDone").value(DEFAULT_WORK_DONE.intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    //@Test
    @Transactional
    public void getNonExistingWorkday() throws Exception {
        // Get the workday
        restWorkdayMockMvc.perform(get("/api/workdays/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    //@Test
    @Transactional
    public void updateWorkday() throws Exception {
        // Initialize the database
        workdayService.save(workday);

        int databaseSizeBeforeUpdate = workdayRepository.findAll().size();

        // Update the workday
        Workday updatedWorkday = new Workday();
        updatedWorkday.setId(workday.getId());
        updatedWorkday.setWorkPlanned(UPDATED_WORK_PLANNED);
        updatedWorkday.setWorkDone(UPDATED_WORK_DONE);
        updatedWorkday.setDate(UPDATED_DATE);

        restWorkdayMockMvc
            .perform(
                put("/api/workdays").contentType(TestUtil.APPLICATION_JSON_UTF8)
                    .content(TestUtil.convertObjectToJsonBytes(updatedWorkday)))
            .andExpect(status().isOk());

        // Validate the Workday in the database
        List<Workday> workdays = workdayRepository.findAll();
        assertThat(workdays).hasSize(databaseSizeBeforeUpdate);
        Workday testWorkday = workdays.get(workdays.size() - 1);
        assertThat(testWorkday.getWorkPlanned())
            .isEqualTo(UPDATED_WORK_PLANNED);
        assertThat(testWorkday.getWorkDone()).isEqualTo(UPDATED_WORK_DONE);
        assertThat(testWorkday.getDate()).isEqualTo(UPDATED_DATE);
    }

    //@Test
    @Transactional
    public void deleteWorkday() throws Exception {
        // Initialize the database
        workdayService.save(workday);

        int databaseSizeBeforeDelete = workdayRepository.findAll().size();

        // Get the workday
        restWorkdayMockMvc
            .perform(delete("/api/workdays/{id}", workday.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Workday> workdays = workdayRepository.findAll();
        assertThat(workdays).hasSize(databaseSizeBeforeDelete - 1);
    }
}
