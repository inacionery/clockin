/**
 *
 */

package org.clockin.web.rest.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.clockin.domain.Email;

/**
 * @author Inácio Nery
 */
public class ReportDTO {

    private LocalDate month;
    private List<EmployeeDTO> employees = new ArrayList<>();
    private Email email;
    private boolean error;

    public ReportDTO() {
    }

    public ReportDTO(LocalDate month, List<EmployeeDTO> employees,
        Email email) {
        this.month = month;
        this.employees = employees;
        this.email = email;
    }

    public ReportDTO(LocalDate month) {
        this.month = month;
    }

    public void addEmployee(EmployeeDTO employee) {
        this.employees.add(employee);
    }

    public LocalDate getMonth() {
        return month;
    }

    public List<EmployeeDTO> getEmployees() {
        return employees;
    }

    public Email getEmail() {
        return email;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "ReportDTO [month=" + month + ", employees=" + employees
            + ", email=" + email + "]";
    }
}
