package org.clockin.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * A Employee.
 */
@Entity
@Table(name = "employee")
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "social_identification_number",
        unique = true)
    private String socialIdentificationNumber;

    @Column(name = "planned_daily_hours")
    private Integer plannedDailyHours;

    @OneToMany(mappedBy = "employee")
    @JsonIgnore
    private Set<Clockin> clockins = new HashSet<>();

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSocialIdentificationNumber() {
        return socialIdentificationNumber;
    }

    public void setSocialIdentificationNumber(
        String socialIdentificationNumber) {
        this.socialIdentificationNumber = socialIdentificationNumber;
    }

    public Integer getPlannedDailyHours() {
        return plannedDailyHours;
    }

    public void setPlannedDailyHours(Integer plannedDailyHours) {
        this.plannedDailyHours = plannedDailyHours;
    }

    public Set<Clockin> getClockins() {
        return clockins;
    }

    public void setClockins(Set<Clockin> clockins) {
        this.clockins = clockins;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Employee employee = (Employee) o;
        if (employee.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, employee.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Employee{" + "id=" + id + ", socialIdentificationNumber='"
            + socialIdentificationNumber + "'" + ", plannedDailyHours='"
            + plannedDailyHours + "'" + '}';
    }
}
