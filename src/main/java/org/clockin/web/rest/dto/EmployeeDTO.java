/**
 *
 */

package org.clockin.web.rest.dto;

import org.clockin.domain.Employee;

/**
 * @author Inácio Nery
 */
public class EmployeeDTO {

    private Employee employee;
    private Long hour;
    private Long hourCumulative;

    public EmployeeDTO(Employee employee, Long hour, Long hourCumulative) {
        this.employee = employee;
        this.hour = hour;
        this.hourCumulative = hourCumulative;
    }

    public Employee getEmployee() {
        return employee;
    }

    public Long getHour() {
        return hour;
    }

    public Long getHourCumulative() {
        return hourCumulative;
    }

}
