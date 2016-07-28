package org.clockin.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Workday.
 */
@Entity
@Table(name = "workday")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Workday implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "work_planned")
    private Long workPlanned;

    @Column(name = "work_done")
    private Long workDone;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "justification")
    private String justification;

    @ManyToOne
    private Employee employee;

    @OneToMany(mappedBy = "workday")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Clockin> clockins = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWorkPlanned() {
        return workPlanned;
    }

    public void setWorkPlanned(Long workPlanned) {
        this.workPlanned = workPlanned;
    }

    public Long getWorkDone() {
        return workDone;
    }

    public void setWorkDone(Long workDone) {
        this.workDone = workDone;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getJustification() {
        return justification;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Set<Clockin> getClockins() {
        return clockins;
    }

    public void setClockins(Set<Clockin> clockins) {
        this.clockins = clockins;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Workday workday = (Workday) o;
        if (workday.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, workday.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Workday{" + "id=" + id + ", workPlanned='" + workPlanned + "'"
            + ", workDone='" + workDone + "'" + ", date='" + date + "'"
            + ", justification='" + justification + "'" + '}';
    }
}
