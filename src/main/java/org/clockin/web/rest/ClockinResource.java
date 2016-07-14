package org.clockin.web.rest;

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
import org.clockin.repository.UserRepository;
import org.clockin.security.SecurityUtils;
import org.clockin.service.ClockinService;
import org.clockin.service.EmployeeService;
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

import com.codahale.metrics.annotation.Timed;

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
     * GET /workdays/ /workdays/{year}/{month} -> get all workdays.
     * @throws ParseException
     */
    @RequestMapping(value = { "/workdays/{year}/{month}" },
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

        LocalDateTime start = LocalDateTime.of(startDate, LocalTime.MIN);

        LocalDateTime end = LocalDateTime.of(endDate, LocalTime.MAX);

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
    @RequestMapping(value = { "/workdays-calendar/{year}/{month}" },
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

        LocalDateTime start = LocalDateTime.of(startDate, LocalTime.MIN);

        LocalDateTime end = LocalDateTime.of(endDate, LocalTime.MAX);

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

    /**
     * POST  /clockins : Create a new clockin.
     * @throws JSONException 
     */
    @RequestMapping(value = "/clockins/create",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void createClockin(@RequestBody String employeesParam)
        throws URISyntaxException, JSONException {

        Employee employee = null;
        JSONArray employeesArray = new JSONArray(employeesParam);
        for (int i = 0; i < employeesArray.length(); i++) {
            JSONObject employeeObject = employeesArray.getJSONObject(i);

            String pis = employeeObject.getString("pis");

            employee = checkEmployee(employee, pis);

            JSONArray workdaysArray = employeeObject.getJSONArray("workdays");

            for (int j = 0; j < workdaysArray.length(); j++) {
                JSONObject workdayObject = workdaysArray.getJSONObject(j);

                LocalDate date = LocalDate
                    .parse(workdayObject.getString("date"));

                JSONArray clockinsArray = workdayObject
                    .getJSONArray("clockins");

                for (int k = 0; k < clockinsArray.length(); k++) {
                    JSONObject clockinObject = clockinsArray.getJSONObject(k);
                    LocalTime time = LocalTime
                        .parse(clockinObject.getString("time"));
                    Long id = clockinObject.getLong("id");

                    addClockin(employee, date, time, id);
                }
            }
        }

    }

    private Employee checkEmployee(Employee employee, String pis) {
        if (employee == null
            || !employee.getSocialIdentificationNumber().equals(pis)) {
            employee = employeeService.findBySocialIdentificationNumber(pis);
            if (employee == null
                || employee.getSocialIdentificationNumber() == null
                || !employee.getSocialIdentificationNumber().equals(pis)) {
                employee = new Employee();
                employee.setSocialIdentificationNumber(pis);
                employee = employeeService.save(employee);
            }
        }
        return employee;
    }

    private void addClockin(Employee employee, LocalDate date, LocalTime time,
        Long id) {
        Clockin clockin = clockinService.findOne(id);
        if (clockin == null || clockin.getId() == null
            || !clockin.getId().equals(id)) {
            LocalDateTime dateTime = LocalDateTime.of(date, time);
            clockin = new Clockin();
            clockin.setId(id);
            clockin.setEmployee(employee);
            clockin.setDateTime(dateTime);
            clockinService.save(clockin);
        }
    }

}