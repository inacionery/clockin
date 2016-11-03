package org.clockin.web.rest;

import com.codahale.metrics.annotation.Timed;

import java.net.URISyntaxException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.clockin.domain.Employee;
import org.clockin.domain.User;
import org.clockin.domain.Workday;
import org.clockin.repository.ClockinRepository;
import org.clockin.repository.UserRepository;
import org.clockin.repository.WorkdayRepository;
import org.clockin.security.AuthoritiesConstants;
import org.clockin.security.SecurityUtils;
import org.clockin.service.EmployeeService;
import org.clockin.web.rest.dto.EmployeeDTO;
import org.clockin.web.rest.dto.ReportDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing Clockin.
 */
@RestController
@RequestMapping("/api")
public class ManagerResource {

    private final Logger log = LoggerFactory.getLogger(ManagerResource.class);

    @Inject
    private EmployeeService employeeService;

    @Inject
    private WorkdayRepository workdayRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private ClockinRepository clockinRepository;

    @RequestMapping(value = { "/manager/{year}/{semester}" },
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ReportDTO> getReportByYearSemester(
        @PathVariable(value = "year") Optional<Integer> yearParam,
        @PathVariable(value = "semester") Optional<Integer> semesterParam)
        throws URISyntaxException, ParseException {

        log.debug("Request to get Manager By Year: {}, Semester : {}",
            yearParam, semesterParam);

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

        List<ReportDTO> reportDTOList = new ArrayList<>();

        for (int month : months) {
            getReportDTO(reportDTOList, LocalDate.of(year, month, 1));
        }

        Optional<User> user = userRepository
            .findOneByLogin(SecurityUtils.getCurrentUserLogin());

        if (!user.isPresent()) {
            return reportDTOList;
        }

        boolean isAdmin = SecurityUtils
            .isCurrentUserInRole(AuthoritiesConstants.ADMIN);

        List<Employee> employees;

        if (isAdmin) {
            employees = employeeService.findByHiddenIsFalse();
        }
        else {
            employees = employeeService.findByManager(user.get());
        }

        for (Employee employee : employees) {

            LocalDate startDate = LocalDate.of(year, months[0], 1);
            LocalDate endDate = LocalDate.of(year, months[5], 1)
                .with(TemporalAdjusters.lastDayOfMonth());

            if (startDate.equals(now) || startDate.isAfter(now)) {
                continue;
            }
            else if (endDate.equals(now) || endDate.isAfter(now)) {
                endDate = now.minusDays(1);
            }

            long hourCumulative = 0;

            for (Object[] work : workdayRepository
                .getSumWorkDoneByEmployeeAndDateBetween(employee, startDate,
                    endDate)) {

                if (work != null) {

                    LocalDate start = LocalDate.of(year, (Integer) work[1], 1);
                    LocalDate end = LocalDate.of(year, (Integer) work[1], 1)
                        .with(TemporalAdjusters.lastDayOfMonth());

                    if (end.equals(now) || end.isAfter(now)) {
                        end = now.minusDays(1);
                    }

                    Long hour = (Long) work[0];

                    int missing = 0;

                    for (Workday workday : workdayRepository
                        .findByEmployeeAndDateBetweenOrderByDate(employee,
                            start, end)) {

                        int countByWorkday = clockinRepository
                            .countByWorkday(workday);

                        if (countByWorkday % 2 != 0 || (countByWorkday == 0
                            && workday.getWorkPlanned() > 0)
                            && workday.getJustification().equals("")) {
                            missing++;
                            hour += workday.getWorkPlanned();
                        }
                    }

                    ReportDTO reportDTO = getReportDTO(reportDTOList, start);

                    hourCumulative += hour;

                    EmployeeDTO employeeDTO = new EmployeeDTO(employee, hour,
                        hourCumulative);

                    for (Object[] occurrences : workdayRepository
                        .findByEmployeeAndDateBetweenGroupByJustification(
                            employee, start, end)) {
                        employeeDTO.putOccurrence(occurrences[0],
                            occurrences[1]);
                    }

                    if (missing > 0) {
                        employeeDTO.putOccurrence("Faltando batidas", missing);
                    }

                    reportDTO.addEmployee(employeeDTO);
                }
            }
        }

        return reportDTOList;
    }

    private ReportDTO getReportDTO(List<ReportDTO> reportDTOList,
        LocalDate date) {
        for (ReportDTO reportDTO : reportDTOList) {
            if (reportDTO.getMonth().equals(date)) {
                return reportDTO;
            }
        }
        ReportDTO reportDTO = new ReportDTO(date);
        reportDTOList.add(reportDTO);
        return reportDTO;
    }
}