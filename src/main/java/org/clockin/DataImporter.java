package org.clockin;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.clockin.domain.Clockin;
import org.clockin.domain.Employee;
import org.clockin.domain.User;
import org.clockin.service.ClockinService;
import org.clockin.service.EmployeeService;
import org.clockin.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DataImporter {

    @Inject
    private UserService userService;

    @Inject
    private EmployeeService employeeService;

    @Inject
    private ClockinService clockinService;

    public void importData() {

        importUsersAndEmployees();
    }

    private void importUsersAndEmployees() {
        List<Employee> employees = null;

        try (Stream<String> stream = Files.lines(Paths.get(
            DataImporter.class.getResource("/all-employees.csv").toURI()))) {
            employees = stream.map(line -> createEmployee(line))
                .collect(Collectors.toList());

            for (Employee employee : employees) {
                URI uri = DataImporter.class.getResource("/all-clockins.csv")
                    .toURI();
                Files.lines(Paths.get(uri))
                    .filter(line -> line
                        .endsWith(employee.getSocialIdentificationNumber()))
                    .forEach(line -> createClockin(employee, line));
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }

    }

    private void createClockin(Employee employee, String line) {

        Clockin clockin = new Clockin();

        clockin.setSequentialRegisterNumber(line.substring(0, 9));

        String dateString = line.substring(10, 18);

        LocalDate date = LocalDate.parse(dateString,
            DateTimeFormatter.ofPattern("ddMMyyyy"));

        String timeString = line.substring(18, 22);
        LocalTime time = LocalTime.parse(timeString,
            DateTimeFormatter.ofPattern("HHmm"));

        LocalDateTime dateTime = date.atTime(time);

        clockin.setDateTime(dateTime);

        clockin.setEmployee(employee);

        clockinService.save(clockin);
    }

    private Employee createEmployee(String line) {

        Employee employee = new Employee();

        String[] data = line.split("#");

        employee.setPlannedDailyHours(8);
        employee.setSocialIdentificationNumber(data[0]);

        User user = createUser(data);

        employee.setUser(user);

        employeeService.save(employee);

        return employee;
    }

    private User createUser(String[] data) {

        String email = data[3];
        String password = "12345";
        String firstName = data[1];
        String lastName = data[2];
        String langKey = "pt-BR";
        User user = userService.createUserInformation(email, password,
            firstName, lastName, email, langKey);

        return user;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public EmployeeService getEmployeeService() {
        return employeeService;
    }

    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    public ClockinService getClockinService() {
        return clockinService;
    }

    public void setClockinService(ClockinService clockinService) {
        this.clockinService = clockinService;
    }

}
