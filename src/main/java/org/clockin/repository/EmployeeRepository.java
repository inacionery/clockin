package org.clockin.repository;

import java.util.List;

import org.clockin.domain.Employee;
import org.clockin.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for the Employee entity.
 */
@SuppressWarnings("unused")
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findByUser(User user);

    Employee findBySocialIdentificationNumber(
        String socialIdentificationNumber);

    Page<Employee> findByHiddenIsFalse(Pageable pageable);

    List<Employee> findByHiddenIsFalse();

    @Query("select distinct employee from Employee employee left join fetch employee.managers")
    List<Employee> findAllWithEagerRelationships();

    @Query("select employee from Employee employee left join fetch employee.managers where employee.id =:id")
    Employee findOneWithEagerRelationships(@Param("id") Long id);

    @Query("select distinct employee from Employee employee inner join fetch employee.managers as managers where managers = :manager")
    List<Employee> findByManager(@Param("manager") User manager);

}
