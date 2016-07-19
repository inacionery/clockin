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

        Optional<User> user = userRepository
            .findOneByLogin(SecurityUtils.getCurrentUserLogin());
        Employee employee = employeeService.findByUser(user.get());

        List<WorkDayDTO> workDays = new ArrayList<>();

        if (employee != null) {
            List<Workday> workdays = workdayService
                .findByEmployeeAndDateBetweenOrderByDate(employee, startDate,
                    endDate);

            int workdayCount = 0;
            long balance = 0;
            for (int i = 1; i <= startDate.lengthOfMonth(); i++) {
                LocalDate curDate = LocalDate.of(year, month, i);

                WorkDayDTO workDayDTO = new WorkDayDTO(curDate);

                for (int j = workdayCount; j < workdays.size(); j++) {
                    Workday workday = workdays.get(j);

                    if (!workday.getDate().isEqual(curDate)) {
                        workdayCount = j;
                        break;
                    }

                    workDayDTO.setWorkDone(workday.getWorkDone());
                    workDayDTO.setWorkPlanned(workday.getWorkPlanned());

                    balance += workday.getWorkDone();
                    workDayDTO.setBalance(balance);

                    for (Clockin clockin : clockinService
                        .findByWorkday(workday)) {
                        workDayDTO.addClockinValues(clockin);
                    }
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

        Optional<User> user = userRepository
            .findOneByLogin(SecurityUtils.getCurrentUserLogin());
        Employee employee = employeeService.findByUser(user.get());

        List<WorkDayDTO> workDays = new ArrayList<>();

        if (employee != null) {

            int diff = startDate.getDayOfWeek().getValue() == 7 ? 0
                : startDate.getDayOfWeek().getValue();
            LocalDate previousDate = endDate.minusMonths(1);
            for (int i = 0; i < diff; i++) {
                LocalDate curDate = previousDate.minusDays(diff - i - 1);
                WorkDayDTO workDayDTO = new WorkDayDTO(curDate);
                workDays.add(workDayDTO);
            }

            if (employee != null) {
                List<Workday> workdays = workdayService
                    .findByEmployeeAndDateBetweenOrderByDate(employee,
                        startDate, endDate);

                int workdayCount = 0;
                long balance = 0;
                for (int i = 1; i <= startDate.lengthOfMonth(); i++) {
                    LocalDate curDate = LocalDate.of(year, month, i);

                    WorkDayDTO workDayDTO = new WorkDayDTO(curDate);

                    for (int j = workdayCount; j < workdays.size(); j++) {
                        Workday workday = workdays.get(j);

                        if (!workday.getDate().isEqual(curDate)) {
                            workdayCount = j;
                            break;
                        }

                        workDayDTO.setWorkDone(workday.getWorkDone());
                        workDayDTO.setWorkPlanned(workday.getWorkPlanned());

                        balance += workday.getWorkDone();
                        workDayDTO.setBalance(balance);

                        for (Clockin clockin : clockinService
                            .findByWorkday(workday)) {
                            workDayDTO.addClockinValues(clockin);
                        }
                    }

                    workDays.add(workDayDTO);
                }
            }

            diff = 42 - workDays.size();
            LocalDate nextStartDate = startDate.plusMonths(1).withDayOfMonth(1);
            for (int i = 0; i < diff; i++) {
                LocalDate curDate = nextStartDate.plusDays(i);
                WorkDayDTO workDayDTO = new WorkDayDTO(curDate);
                workDays.add(workDayDTO);
            }

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
    public void createClockin(@RequestBody String employeeParam)
        throws URISyntaxException, JSONException {

        JSONObject employeeObject = new JSONObject(employeeParam);

        String pis = employeeObject.getString("pis");

        Employee employee = getOrCreateEmployee(pis);

        JSONArray workdaysArray = employeeObject.getJSONArray("workdays");

        for (int j = 0; j < workdaysArray.length(); j++) {
            JSONObject workdayObject = workdaysArray.getJSONObject(j);

            LocalDate date = LocalDate.parse(workdayObject.getString("date"));

            Long workDone = workdayObject.getLong("workDone");

            Long workPlanned = workdayObject.getLong("workPlanned");

            Workday workday = createOrUpdateWorkDay(employee, date, workDone,
                workPlanned);

            JSONArray clockinsArray = workdayObject.getJSONArray("clockins");

            for (int k = 0; k < clockinsArray.length(); k++) {
                JSONObject clockinObject = clockinsArray.getJSONObject(k);
                LocalTime time = LocalTime
                    .parse(clockinObject.getString("time"));
                Long id = clockinObject.getLong("id");

                createOrUpdateClockin(workday, LocalDateTime.of(date, time),
                    id);
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
        Long workDone, Long workPlanned) {

        Workday workday = workdayService.findByEmployeeAndDate(employee, date);

        if (workday == null) {
            workday = new Workday();
            workday.setEmployee(employee);
            workday.setDate(date);
        }

        if (!workDone.equals(workday.getWorkDone())) {
            workday.setWorkDone(workDone);
        }

        if (!workPlanned.equals(workday.getWorkPlanned())) {
            workday.setWorkDone(workPlanned);
        }

        return workdayService.save(workday);
    }

    private void createOrUpdateClockin(Workday workday, LocalDateTime time,
        Long id) {

        Clockin clockin = clockinService
            .findBySequentialRegisterNumber(String.valueOf(id));

        if (clockin == null) {
            clockin = new Clockin();
            clockin.setSequentialRegisterNumber(String.valueOf(id));
        }

        if (!workday.equals(clockin.getWorkday())) {
            clockin.setWorkday(workday);
        }

        if (!time.equals(clockin.getTime())) {
            clockin.setTime(time);
        }

        clockinService.save(clockin);
    }

}