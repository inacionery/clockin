package org.clockin.repository;

import org.clockin.domain.Email;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Email entity.
 */
@SuppressWarnings("unused")
public interface EmailRepository extends JpaRepository<Email, Long> {

}
