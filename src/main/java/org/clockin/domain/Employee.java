package org.clockin.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Employee.
 */
@Entity
@Table(name = "employee")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "employee")
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "social_identification_number")
    private String socialIdentificationNumber;

    @Column(name = "planned_daily_hours")
    private Integer plannedDailyHours;

    @OneToMany(mappedBy = "employee")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
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
