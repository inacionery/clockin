package org.clockin.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Clockin.
 */
@Entity
@Table(name = "clockin")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Clockin implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "sequential_register_number",
        unique = true)
    private String sequentialRegisterNumber;

    @Column(name = "time")
    private LocalDateTime time;

    @Column(name = "justification")
    private String justification;

    @ManyToOne
    private Workday workday;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSequentialRegisterNumber() {
        return sequentialRegisterNumber;
    }

    public void setSequentialRegisterNumber(String sequentialRegisterNumber) {
        this.sequentialRegisterNumber = sequentialRegisterNumber;
    }

    public LocalTime getTime() {
        return time != null ? time.toLocalTime() : null;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getJustification() {
        return justification;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }

    public Workday getWorkday() {
        return workday;
    }

    public void setWorkday(Workday workday) {
        this.workday = workday;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Clockin clockin = (Clockin) o;
        if (clockin.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, clockin.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Clockin{" + "id=" + id + ", sequentialRegisterNumber='"
            + sequentialRegisterNumber + "'" + ", time='" + time + "'"
            + ", justification='" + justification + "'" + '}';
    }
}
