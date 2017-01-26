/**
 *
 */

package org.clockin.web.rest.dto;

import static java.time.temporal.ChronoUnit.MINUTES;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.clockin.domain.Clockin;

/**
 * @author Miguel Angelo Caldas Gallindo
 * @author Inácio Nery
 */
public class WorkDayDTO {

    private LocalDate date;
    private List<Clockin> clockins;

    private long workDone;
    private long workPlanned;
    private long workMinute;
    private long intervalMinute;
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

    public boolean getIsAfterToday() {
        LocalDate now = LocalDate.now();

        return date.isAfter(now);
    }

    public boolean getIsClockinMissing() {
        return clockins.size() % 2 != 0;
    }

    public long getWorkMinute() {
        if (workMinute == 0) {
            calculateTime();
        }

        return workMinute;
    }

    public long getIntervalMinute() {
        if (intervalMinute == 0) {
            calculateTime();
        }
        return intervalMinute;
    }

    public long getRemainTime() {

        if (workPlanned > 0 && clockins.size() > 0) {
            calculateTime();

            long remainTime = (workPlanned - workMinute
                - MINUTES.between(clockins.get(clockins.size() - 1).getTime(),
                    LocalTime.now(ZoneId.of("GMT-3"))));

            if (remainTime == 0) {
                return remainTime;
            }

            return remainTime * 60;
        }

        return 0;
    }

    private void calculateTime() {
        workMinute = 0;
        intervalMinute = 0;

        for (int i = 0; i < clockins.size() - 1; i++) {
            Clockin start = clockins.get(i);
            Clockin end = clockins.get(i + 1);

            long minutes = Duration.between(start.getTime(), end.getTime())
                .toMinutes();
            if (i % 2 == 0) {
                workMinute += minutes;
            }
            else {
                intervalMinute += minutes;
            }
        }
    }

    public List<Clockin> getPredictions() {
        List<Clockin> predictionClokins = new ArrayList<>();

        if (workPlanned > 0) {
            calculateTime();

            if (clockins.size() == 1) {
                Clockin clockin = clockins.get(0);
                Clockin pd1 = new Clockin();
                pd1.setTime(LocalDateTime.of(date,
                    clockin.getTime().plusMinutes(workPlanned / 2)));
                predictionClokins.add(pd1);
                Clockin pd2 = new Clockin();
                if (workPlanned > 240 && workPlanned <= 360) {
                    pd2.setTime(
                        LocalDateTime.of(date, pd1.getTime().plusMinutes(15)));
                }
                else if (workPlanned > 360) {
                    pd2.setTime(
                        LocalDateTime.of(date, pd1.getTime().plusMinutes(60)));
                }
                predictionClokins.add(pd2);
                Clockin pd3 = new Clockin();
                pd3.setTime(LocalDateTime.of(date,
                    pd2.getTime().plusMinutes(workPlanned / 2)));
                predictionClokins.add(pd3);
            }
            else if (clockins.size() == 2) {
                Clockin clockin = clockins.get(0);
                Clockin pd1 = new Clockin();
                pd1.setTime(LocalDateTime.of(date,
                    clockin.getTime().plusMinutes(workMinute)));
                if (workPlanned > 240 && workPlanned <= 360) {
                    pd1.setTime(
                        LocalDateTime.of(date, pd1.getTime().plusMinutes(15)));
                }
                else if (workPlanned > 360) {
                    pd1.setTime(
                        LocalDateTime.of(date, pd1.getTime().plusMinutes(60)));
                }
                predictionClokins.add(pd1);
                Clockin pd2 = new Clockin();
                pd2.setTime(LocalDateTime.of(date,
                    pd1.getTime().plusMinutes(workPlanned - workMinute)));
                predictionClokins.add(pd2);
            }
            else if (clockins.size() % 2 != 0) {
                Clockin clockin = clockins.get(0);
                Clockin pd1 = new Clockin();
                pd1.setTime(LocalDateTime.of(date,
                    clockin.getTime().plusMinutes(workMinute)));
                pd1.setTime(LocalDateTime.of(date,
                    pd1.getTime().plusMinutes(intervalMinute)));
                if (workPlanned > 240 && workPlanned <= 360
                    && intervalMinute < 15) {
                    pd1.setTime(
                        LocalDateTime.of(date, pd1.getTime().plusMinutes(1)));
                    predictionClokins.add(pd1);
                    Clockin pd2 = new Clockin();
                    pd2.setTime(
                        LocalDateTime.of(date, pd1.getTime().plusMinutes(15)));
                    predictionClokins.add(pd2);
                    Clockin pd3 = new Clockin();
                    pd3.setTime(LocalDateTime.of(date,
                        pd2.getTime().plusMinutes(workPlanned - workMinute)));
                    predictionClokins.add(pd3);
                }
                else if (workPlanned > 360 && intervalMinute < 60) {
                    pd1.setTime(
                        LocalDateTime.of(date, pd1.getTime().plusMinutes(1)));
                    predictionClokins.add(pd1);
                    Clockin pd2 = new Clockin();
                    pd2.setTime(
                        LocalDateTime.of(date, pd1.getTime().plusMinutes(60)));
                    predictionClokins.add(pd2);
                    Clockin pd3 = new Clockin();
                    pd3.setTime(LocalDateTime.of(date,
                        pd2.getTime().plusMinutes(workPlanned - workMinute)));
                    predictionClokins.add(pd3);
                }
                else {
                    pd1.setTime(LocalDateTime.of(date,
                        pd1.getTime().plusMinutes(workPlanned - workMinute)));
                    predictionClokins.add(pd1);
                }
            }
        }

        return predictionClokins;
    }

    public boolean getIsDayMissing() {
        return workPlanned > 0 && workPlanned == (workDone * -1);
    }

    public boolean getIsMissingBreak() {
        if (clockins.size() % 2 == 0 && workPlanned > 0) {
            long work = 0;
            long interval = 0;
            for (int i = 0; i < clockins.size() - 1; i++) {

                Clockin start = clockins.get(i);
                Clockin end = clockins.get(i + 1);

                long minutes = Duration.between(start.getTime(), end.getTime())
                    .toMinutes();
                if (i % 2 == 0) {
                    work += minutes;
                }
                else if (minutes > interval) {
                    interval = minutes;
                }
            }
            if ((work > 240 && work <= 360 && interval < 15)
                || (work > 360 && interval < 60)) {
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
