/**
 *
 */

package org.clockin.web.rest.dto;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.clockin.domain.Clockin;

/**
 * @author Miguel Angelo Caldas Gallindo
 */
public class WorkDayDTO {

    private LocalDate date;
    private List<Clockin> clockins;

    private Long workDone;
    private Long workPlanned;
    private String justification;

    public WorkDayDTO(LocalDate date) {
        this.date = date;
        this.clockins = new ArrayList<>();
    }

    public boolean getIsBeforeToday() {
        LocalDate now = LocalDate.now();

        return date.isBefore(now);
    }

    public boolean getIsToday() {
        LocalDate now = LocalDate.now();

        return date.isEqual(now);
    }

    public boolean getIsClockinMissing() {
        return clockins.size() % 2 != 0;
    }

    public boolean getIsDayMissing() {
        return workPlanned != null && workDone != null && workPlanned > 0
            && workPlanned == (workDone * -1);
    }

    public boolean getIsMissingBreak() {
        if (clockins.size() % 2 == 0) {
            long work = 0;
            boolean interval = false;
            for (int i = 0; i < clockins.size() - 1; i++) {

                Clockin start = clockins.get(i);
                Clockin end = clockins.get(i + 1);

                long minutes = Duration.between(start.getTime(), end.getTime())
                    .toMinutes();
                if (i % 2 == 0) {
                    work += minutes;
                    if (minutes >= 360) {
                        return true;
                    }
                }
                else if (!interval) {
                    interval = minutes >= 60;
                }
            }
            if (work >= 480 && !interval) {
                return true;
            }
        }
        return false;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<Clockin> getClockinValues() {
        return clockins;
    }

    public void setClockinValues(List<Clockin> clockinValues) {
        this.clockins = clockinValues;
    }

    public String getJustification() {
        return justification;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }

    public Long getWorkPlanned() {
        return workPlanned;
    }

    public void setWorkPlanned(long workPlanned) {
        this.workPlanned = workPlanned;
    }

    public void setWorkDone(long workDone) {
        this.workDone = workDone;
    }

    public Long getWorkDone() {
        return workDone;
    }
}
