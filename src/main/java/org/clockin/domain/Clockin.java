package org.clockin.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import org.clockin.domain.enumeration.RegistryType;

/**
 * A Clockin.
 */
@Entity
@Table(name = "clockin")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "clockin")
public class Clockin implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "sequential_register_number")
    private String sequentialRegisterNumber;

    @Column(name = "date_time")
    private ZonedDateTime dateTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "registry_type")
    private RegistryType registryType;

    @ManyToOne
    private Employee employee;

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

    public ZonedDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(ZonedDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public RegistryType getRegistryType() {
        return registryType;
    }

    public void setRegistryType(RegistryType registryType) {
        this.registryType = registryType;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
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
        if(clockin.id == null || id == null) {
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
        return "Clockin{" +
            "id=" + id +
            ", sequentialRegisterNumber='" + sequentialRegisterNumber + "'" +
            ", dateTime='" + dateTime + "'" +
            ", registryType='" + registryType + "'" +
            '}';
    }
}
