/**
 *
 */

package org.clockin.service.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Inácio Nery
 */
public class MonthDTO {

    private List<WorkDayDTO> workDays;

    private LocalDate date;
    private long hours = 0;

    public MonthDTO(LocalDate date) {
        this.date = date;
        this.workDays = new ArrayList<>();
    }

    public List<WorkDayDTO> getWorkDays() {
        return workDays;
    }

    public void addWorkDay(WorkDayDTO workDayDTO) {
        this.workDays.add(workDayDTO);
    }

    public LocalDate getDate() {
        return date;
    }

    public long getHours() {
        return hours;
    }

    public void addHours(long hours) {
        this.hours += hours;
    }

}
