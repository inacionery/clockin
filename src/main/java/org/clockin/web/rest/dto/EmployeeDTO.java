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
    private boolean check;
    private boolean sent;

    public EmployeeDTO() {
    }

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

    public boolean isCheck() {
        return check;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    @Override
    public String toString() {
        return "EmployeeDTO [employee=" + employee + ", hour=" + hour
            + ", hourCumulative=" + hourCumulative + ", check=" + check + "]";
    }
}
