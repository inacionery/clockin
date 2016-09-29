package org.clockin.web.rest;

import com.codahale.metrics.annotation.Timed;

import java.net.URISyntaxException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

import org.clockin.domain.Email;
import org.clockin.domain.Employee;
import org.clockin.domain.User;
import org.clockin.domain.util.EmailConstants;
import org.clockin.repository.UserRepository;
import org.clockin.repository.WorkdayRepository;
import org.clockin.security.SecurityUtils;
import org.clockin.service.EmployeeService;
import org.clockin.service.MailService;
import org.clockin.web.rest.dto.EmployeeDTO;
import org.clockin.web.rest.dto.ReportDTO;
import org.clockin.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
public class ReportResource {

    private final Logger log = LoggerFactory.getLogger(ReportResource.class);

    @Inject
    private EmployeeService employeeService;

    @Inject
    private WorkdayRepository workdayRepository;

    @Inject
    private MailService mailService;

    @Inject
    private UserRepository userRepository;

    /**
     * POST  /employees : Create a new employee.
     *
     * @param employee the employee to create
     * @return the ResponseEntity with status 201 (Created) and with body the new employee, or with status 400 (Bad Request) if the employee has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/report",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ReportDTO> sendEmail(@RequestBody ReportDTO report)
        throws URISyntaxException {
        Email email = report.getEmail();
        log.debug("REST request to send Email : {}", report);

        User userLogin = userRepository
            .findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();

        for (EmployeeDTO employeeDTO : report.getEmployees()) {
            if (!employeeDTO.isCheck()) {
                continue;
            }

            Employee employee = employeeDTO.getEmployee();
            employee = employeeService.findOne(employee.getId());

            Set<User> managers = employee.getManagers();
            String[] bcc = {};
            if (managers != null) {
                bcc = new String[managers.size()];
                int i = 0;
                for (User manager : managers) {
                    bcc[i++] = manager.getEmail();
                }
            }

            User user = employee.getUser();

            String content = replaceValues(email.getContent(), user,
                employeeDTO);
            String subject = replaceValues(email.getSubject(), user,
                employeeDTO);

            boolean sent = mailService.sendEmail(userLogin, user.getEmail(),
                bcc, subject, content, false, true);

            employeeDTO.setSent(sent);

            if (!sent) {
                report.setError(true);
            }
        }
        if (report.isError()) {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert("report",
                    "emailsenterror", "Ocorreu um erro ao enviar os emails"))
                .body(null);
        }
        else {
            return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("report",
                    "Sucesso ao enviar os emails"))
                .body(report);
        }
    }

    private String replaceValues(String text, User user,
        EmployeeDTO employeeDTO) {

        text = text.replace(EmailConstants.EMPLOYEE_FIRST_NAME,
            user.getFirstName());
        text = text.replace(EmailConstants.EMPLOYEE_LAST_NAME,
            user.getLastName());

        int hours = employeeDTO.getHour().intValue() / 60;
        int minutes = employeeDTO.getHour().intValue() % 60;
        if (minutes < 0) {
            minutes = minutes * -1;
        }
        text = text.replace(EmailConstants.EMPLOYEE_MONTH_HOURS,
            String.format("%02d:%02d", hours, minutes));
        hours = employeeDTO.getHourCumulative().intValue() / 60;
        minutes = employeeDTO.getHourCumulative().intValue() % 60;
        if (minutes < 0) {
            minutes = minutes * -1;
        }
        text = text.replace(EmailConstants.EMPLOYEE_TOTAL_HOURS,
            String.format("%02d:%02d", hours, minutes));

        return text;

    }

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