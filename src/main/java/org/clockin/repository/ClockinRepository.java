package org.clockin.repository;

import org.clockin.domain.Clockin;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Clockin entity.
 */
public interface ClockinRepository extends JpaRepository<Clockin,Long> {

}
