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

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.clockin.domain.Clockin;
import org.clockin.repository.ClockinRepository;
import org.clockin.service.ClockinService;
import org.junit.Before;
import org.mockito.MockitoAnnotations;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Test class for the ClockinResource REST controller.
 *
 * @see ClockinResource
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = ClockinApp.class)
public class ClockinResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter
        .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_SEQUENTIAL_REGISTER_NUMBER = "AAAAA";
    private static final String UPDATED_SEQUENTIAL_REGISTER_NUMBER = "BBBBB";

    private static final LocalDateTime DEFAULT_TIME = LocalDateTime
        .ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final LocalDateTime UPDATED_TIME = LocalDateTime.now()
        .withNano(0);
    private static final String DEFAULT_TIME_STR = dateTimeFormatter
        .format(DEFAULT_TIME);

    private static final String DEFAULT_JUSTIFICATION = "AAAAA";
    private static final String UPDATED_JUSTIFICATION = "BBBBB";

    @Inject
    private ClockinRepository clockinRepository;

    @Inject
    private ClockinService clockinService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restClockinMockMvc;

    private Clockin clockin;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ClockinResource clockinResource = new ClockinResource();
        ReflectionTestUtils.setField(clockinResource, "clockinService",
            clockinService);
        this.restClockinMockMvc = MockMvcBuilders
            .standaloneSetup(clockinResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Clockin createEntity(EntityManager em) {
        Clockin clockin = new Clockin();
        clockin.setSequentialRegisterNumber(DEFAULT_SEQUENTIAL_REGISTER_NUMBER);
        clockin.setTime(DEFAULT_TIME);
        clockin.setJustification(DEFAULT_JUSTIFICATION);
        return clockin;
    }

    @Before
    public void initTest() {
        clockin = createEntity(em);
    }

    //@Test
    //@Transactional
    public void createClockin() throws Exception {
        int databaseSizeBeforeCreate = clockinRepository.findAll().size();

        // Create the Clockin

        restClockinMockMvc
            .perform(post("/api/clockins")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(clockin)))
            .andExpect(status().isCreated());

        // Validate the Clockin in the database
        List<Clockin> clockins = clockinRepository.findAll();
        assertThat(clockins).hasSize(databaseSizeBeforeCreate + 1);
        Clockin testClockin = clockins.get(clockins.size() - 1);
        assertThat(testClockin.getSequentialRegisterNumber())
            .isEqualTo(DEFAULT_SEQUENTIAL_REGISTER_NUMBER);
        assertThat(testClockin.getTime()).isEqualTo(DEFAULT_TIME);
        assertThat(testClockin.getJustification())
            .isEqualTo(DEFAULT_JUSTIFICATION);
    }

    //@Test
    //@Transactional
    public void getAllClockins() throws Exception {
        // Initialize the database
        clockinRepository.saveAndFlush(clockin);

        // Get all the clockins
        restClockinMockMvc.perform(get("/api/clockins?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(
                content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(
                jsonPath("$.[*].id").value(hasItem(clockin.getId().intValue())))
            .andExpect(jsonPath("$.[*].sequentialRegisterNumber")
                .value(hasItem(DEFAULT_SEQUENTIAL_REGISTER_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME_STR)))
            .andExpect(jsonPath("$.[*].justification")
                .value(hasItem(DEFAULT_JUSTIFICATION.toString())));
    }

    //@Test
    //@Transactional
    public void getClockin() throws Exception {
        // Initialize the database
        clockinRepository.saveAndFlush(clockin);

        // Get the clockin
        restClockinMockMvc.perform(get("/api/clockins/{id}", clockin.getId()))
            .andExpect(status().isOk())
            .andExpect(
                content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(clockin.getId().intValue()))
            .andExpect(jsonPath("$.sequentialRegisterNumber")
                .value(DEFAULT_SEQUENTIAL_REGISTER_NUMBER.toString()))
            .andExpect(jsonPath("$.time").value(DEFAULT_TIME_STR))
            .andExpect(jsonPath("$.justification")
                .value(DEFAULT_JUSTIFICATION.toString()));
    }

    //@Test
    //@Transactional
    public void getNonExistingClockin() throws Exception {
        // Get the clockin
        restClockinMockMvc.perform(get("/api/clockins/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    //@Test
    //@Transactional
    public void updateClockin() throws Exception {
        // Initialize the database
        clockinService.save(clockin);

        int databaseSizeBeforeUpdate = clockinRepository.findAll().size();

        // Update the clockin
        Clockin updatedClockin = clockinRepository.findOne(clockin.getId());
        updatedClockin
            .setSequentialRegisterNumber(UPDATED_SEQUENTIAL_REGISTER_NUMBER);
        updatedClockin.setTime(UPDATED_TIME);
        updatedClockin.setJustification(UPDATED_JUSTIFICATION);

        restClockinMockMvc
            .perform(
                put("/api/clockins").contentType(TestUtil.APPLICATION_JSON_UTF8)
                    .content(TestUtil.convertObjectToJsonBytes(updatedClockin)))
            .andExpect(status().isOk());

        // Validate the Clockin in the database
        List<Clockin> clockins = clockinRepository.findAll();
        assertThat(clockins).hasSize(databaseSizeBeforeUpdate);
        Clockin testClockin = clockins.get(clockins.size() - 1);
        assertThat(testClockin.getSequentialRegisterNumber())
            .isEqualTo(UPDATED_SEQUENTIAL_REGISTER_NUMBER);
        assertThat(testClockin.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testClockin.getJustification())
            .isEqualTo(UPDATED_JUSTIFICATION);
    }

    //@Test
    //@Transactional
    public void deleteClockin() throws Exception {
        // Initialize the database
        clockinService.save(clockin);

        int databaseSizeBeforeDelete = clockinRepository.findAll().size();

        // Get the clockin
        restClockinMockMvc
            .perform(delete("/api/clockins/{id}", clockin.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Clockin> clockins = clockinRepository.findAll();
        assertThat(clockins).hasSize(databaseSizeBeforeDelete - 1);
    }
}
