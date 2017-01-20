/**
 *
 */

package org.clockin.web.rest.dto;

import java.util.LinkedHashMap;
import java.util.Map;

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
    private Map<Object, Object> occurrence = new LinkedHashMap<>();

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

    public Map<Object, Object> getOccurrence() {
        return occurrence;
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

    public void putOccurrence(Object key, Object value) {
        this.occurrence.put(key, value);
    }

    @Override
    public String toString() {
        return "EmployeeDTO [employee=" + employee + ", hour=" + hour
            + ", occurrence=" + occurrence + ", hourCumulative="
            + hourCumulative + ", check=" + check + "]";
    }
}
