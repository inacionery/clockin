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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Employee.
 */
@Entity
@Table(name = "employee")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "social_identification_number",
        unique = true)
    private String socialIdentificationNumber;

    @Column(name = "hidden")
    @NotNull
    private Boolean hidden = false;

    @OneToMany(mappedBy = "employee")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Workday> workdays = new HashSet<>();

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "employee_manager",
        joinColumns = @JoinColumn(name = "employees_id",
            referencedColumnName = "ID"),
        inverseJoinColumns = @JoinColumn(name = "managers_id",
            referencedColumnName = "ID"))
    private Set<User> managers = new HashSet<>();

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

    public Boolean isHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public Set<Workday> getWorkdays() {
        return workdays;
    }

    public void setWorkdays(Set<Workday> workdays) {
        this.workdays = workdays;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<User> getManagers() {
        return managers;
    }

    public void setManagers(Set<User> users) {
        this.managers = users;
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
            + socialIdentificationNumber + "'" + ", hidden='" + hidden + "'"
            + '}';
    }
}
