package org.clockin.web.rest;

import com.codahale.metrics.annotation.Timed;

import java.net.URISyntaxException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.clockin.domain.Clockin;
import org.clockin.domain.Employee;
import org.clockin.domain.User;
import org.clockin.domain.Workday;
import org.clockin.repository.UserRepository;
import org.clockin.security.SecurityUtils;
import org.clockin.service.ClockinService;
import org.clockin.service.EmployeeService;
import org.clockin.service.WorkdayService;
import org.clockin.web.rest.dto.MonthDTO;
import org.clockin.web.rest.dto.WorkDayDTO;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    private WorkdayService workdayService;

    @Inject
    private UserRepository userRepository;

    /**
     * GET /clockin/{year}/{semester} -> get all workdays by year and semester.
     * @throws ParseException
     */
    @RequestMapping(value = { "/clockin/{year}/{semester}" },
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<MonthDTO> getWorkDaysByYearSemester(
        @PathVariable(value = "year") Optional<Integer> yearParam,
        @PathVariable(value = "semester") Optional<Integer> semesterParam)
        throws URISyntaxException, ParseException {

        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int semester = now.getMonth().getValue() > 6 ? 1 : 0;

        if (yearParam.isPresent()) {
            year = yearParam.get();
        }
        if (semesterParam.isPresent()) {
            semester = semesterParam.get();
        }

        int[] months;
        if (semester == 0) {
            months = new int[] { 1, 2, 3, 4, 5, 6 };
        }
        else {
            months = new int[] { 7, 8, 9, 10, 11, 12 };
        }

        List<MonthDTO> monthList = new ArrayList<>();
        Optional<User> user = userRepository
            .findOneByLogin(SecurityUtils.getCurrentUserLogin());
        Employee employee = employeeService.findByUser(user.get());

        if (employee != null) {
            for (int i = 0; i < months.length; i++) {
                int month = months[i];

                LocalDate startDate = LocalDate.of(year, month, 1);
                LocalDate endDate = LocalDate.of(year, month,
                    startDate.lengthOfMonth());

                MonthDTO monthDTO = new MonthDTO(startDate);

                int diff = startDate.getDayOfWeek().getValue() == 7 ? 0
                    : startDate.getDayOfWeek().getValue();
                LocalDate previousDate = endDate.minusMonths(1);
                for (int l = 0; l < diff; l++) {
                    LocalDate curDate = previousDate.minusDays(diff - l - 1);
                    WorkDayDTO workDayDTO = new WorkDayDTO(curDate);
                    monthDTO.addWorkDay(workDayDTO);
                }

                List<Workday> workdays = workdayService
                    .findByEmployeeAndDateBetweenOrderByDate(employee,
                        startDate, endDate);

                int workdayCount = 0;
                long hours = 0;
                for (int k = 1; k <= startDate.lengthOfMonth(); k++) {
                    LocalDate curDate = LocalDate.of(year, month, k);

                    WorkDayDTO workDayDTO = new WorkDayDTO(curDate);

                    for (int j = workdayCount; j < workdays.size(); j++) {
                        Workday workday = workdays.get(j);

                        if (!workday.getDate().isEqual(curDate)) {
                            workdayCount = j;
                            break;
                        }

                        List<Clockin> clockins = clockinService
                            .findByWorkday(workday);

                        workDayDTO.setClockinValues(clockins);

                        if (curDate.isAfter(now) || curDate.isEqual(now)) {
                            break;
                        }

                        workDayDTO.setWorkPlanned(workday.getWorkPlanned());
                        workDayDTO.setJustification(workday.getJustification());

                        if (clockins.size() % 2 == 0) {
                            workDayDTO.setWorkDone(workday.getWorkDone());
                            hours += workday.getWorkDone();
                            monthDTO.setHours(hours);
                        }
                    }
                    monthDTO.addWorkDay(workDayDTO);
                }

                diff = 42 - monthDTO.getWorkDays().size();
                LocalDate nextStartDate = startDate.plusMonths(1)
                    .withDayOfMonth(1);
                for (int l = 0; l < diff; l++) {
                    LocalDate curDate = nextStartDate.plusDays(l);
                    WorkDayDTO workDayDTO = new WorkDayDTO(curDate);
                    monthDTO.addWorkDay(workDayDTO);
                }

                monthList.add(monthDTO);
            }
        }

        return monthList;
    }

    /**
     * POST  /clockins : Create a new clockin.
     * @throws JSONException 
     */
    @RequestMapping(value = "/clockins/create",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void createClockin(@RequestBody String employeeParam)
        throws URISyntaxException, JSONException {
        JSONObject employeeObject = new JSONObject(employeeParam);

        log.debug("Request to create Clockin : {}", employeeObject);

        String pis = employeeObject.getString("pis");

        Employee employee = getOrCreateEmployee(pis);

        JSONArray workdaysArray = employeeObject.getJSONArray("workdays");

        for (int j = 0; j < workdaysArray.length(); j++) {
            JSONObject workdayObject = workdaysArray.getJSONObject(j);

            LocalDate date = LocalDate.parse(workdayObject.getString("date"));

            Long workDone = workdayObject.getLong("workDone");

            Long workPlanned = workdayObject.getLong("workPlanned");

            String workDayJustification = workdayObject
                .getString("justification");

            Workday workday = createOrUpdateWorkDay(employee, date, workDone,
                workPlanned, workDayJustification);

            JSONArray clockinsArray = workdayObject.getJSONArray("clockins");

            for (int k = 0; k < clockinsArray.length(); k++) {
                JSONObject clockinObject = clockinsArray.getJSONObject(k);
                LocalTime time = LocalTime
                    .parse(clockinObject.getString("time"));
                Long id = clockinObject.getLong("id");
                String clockinJustification = clockinObject
                    .getString("justification");

                createOrUpdateClockin(workday, LocalDateTime.of(date, time), id,
                    clockinJustification);
            }
        }

    }

    private Employee getOrCreateEmployee(String pis) {

        Employee employee = employeeService
            .findBySocialIdentificationNumber(pis);

        if (employee == null) {
            employee = new Employee();
            employee.setSocialIdentificationNumber(pis);
            employee = employeeService.save(employee);
        }

        return employee;
    }

    private Workday createOrUpdateWorkDay(Employee employee, LocalDate date,
        Long workDone, Long workPlanned, String justification) {

        Workday workday = workdayService.findByEmployeeAndDate(employee, date);

        if (workday == null) {
            workday = new Workday();
            workday.setEmployee(employee);
            workday.setDate(date);
        }

        if (workDone != null && (workday.getWorkDone() == null
            || !workDone.equals(workday.getWorkDone()))) {
            workday.setWorkDone(workDone);
        }

        if (workPlanned != null && (workday.getWorkPlanned() == null
            || !workPlanned.equals(workday.getWorkPlanned()))) {
            workday.setWorkPlanned(workPlanned);
        }

        if (justification != null && (workday.getJustification() == null
            || !justification.equals(workday.getJustification()))) {
            workday.setJustification(justification);
        }

        return workdayService.save(workday);
    }

    private void createOrUpdateClockin(Workday workday, LocalDateTime time,
        Long id, String justification) {

        Clockin clockin = clockinService
            .findBySequentialRegisterNumber(String.valueOf(id));

        if (clockin == null) {
            clockin = new Clockin();
            clockin.setSequentialRegisterNumber(String.valueOf(id));
        }

        if (workday != null && (clockin.getWorkday() == null
            || !workday.equals(clockin.getWorkday()))) {
            clockin.setWorkday(workday);
        }

        if (time != null
            && (clockin.getTime() == null || !time.equals(clockin.getTime()))) {
            clockin.setTime(time);
        }

        if (justification != null && (clockin.getJustification() == null
            || !justification.equals(clockin.getJustification()))) {
            clockin.setJustification(justification);
        }

        clockinService.save(clockin);
    }

}