package org.clockin.repository;

import java.util.List;

import org.clockin.domain.Clockin;
import org.clockin.domain.Workday;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Clockin entity.
 */
public interface ClockinRepository extends JpaRepository<Clockin, Long> {

    List<Clockin> findByWorkday(Workday workday);

    Clockin findBySequentialRegisterNumber(String sequentialRegisterNumber);

}
