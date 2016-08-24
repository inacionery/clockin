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
import org.clockin.repository.WorkdayRepository;
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
public class ReportResource {

    private final Logger log = LoggerFactory.getLogger(ReportResource.class);

    @Inject
    private EmployeeService employeeService;

    @Inject
    private WorkdayRepository workdayRepository;

    @RequestMapping(value = { "/report/{year}/{semester}" },
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ReportDTO> getReportByYearSemester(
        @PathVariable(value = "year") Optional<Integer> yearParam,
        @PathVariable(value = "semester") Optional<Integer> semesterParam)
        throws URISyntaxException, ParseException {

        log.debug("Request to get Report By Year: {}, Semester : {}", yearParam,
            semesterParam);

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

        for (Employee employee : employeeService.findByHiddenIsFalse()) {

            LocalDate startDate = LocalDate.of(year, months[0], 1);
            LocalDate endDate = LocalDate.of(year, months[5], 1)
                .with(TemporalAdjusters.lastDayOfMonth());

            long hourCumulative = 0;

            for (Object[] work : workdayRepository
                .getSumWorkDoneByEmployeeAndDateBetween(employee, startDate,
                    endDate)) {

                if (work != null) {

                    ReportDTO reportDTO = getReportDTO(reportDTOList,
                        LocalDate.of(year, (Integer) work[1], 1));

                    Long hour = (Long) work[0];

                    hourCumulative += hour;

                    reportDTO.addEmployee(
                        new EmployeeDTO(employee, hour, hourCumulative));
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