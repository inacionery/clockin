package org.clockin.repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.clockin.domain.Authority;
import org.clockin.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for the User entity.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findOneByActivationKey(String activationKey);

    List<User> findAllByActivatedIsFalseAndCreatedDateBefore(
        ZonedDateTime dateTime);

    List<User> findAllByActivatedIsTrue();

    @Query("select distinct user from User user inner join fetch user.authorities as authorities where authorities = :authority")
    List<User> findAllByAuthority(@Param("authority") Authority authority);

    Optional<User> findOneByResetKey(String resetKey);

    Optional<User> findOneByEmail(String email);

    Optional<User> findOneByLogin(String login);

    Optional<User> findOneById(Long userId);

    @Query(
        value = "select distinct user from User user left join fetch user.authorities",
        countQuery = "select count(user) from User user")
    Page<User> findAllWithAuthorities(Pageable pageable);

    @Override
    void delete(User t);

}
