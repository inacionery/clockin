/**
 *
 */

package org.clockin.web.rest.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Inácio Nery
 */
public class ReportDTO {

    private LocalDate month;
    private List<EmployeeDTO> employees = new ArrayList<>();

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

}
