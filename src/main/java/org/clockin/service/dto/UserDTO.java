package org.clockin.service.dto;

import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.clockin.config.Constants;
import org.clockin.domain.Authority;
import org.clockin.domain.User;
import org.clockin.service.EmployeeService;
import org.hibernate.validator.constraints.Email;

/**
 * A DTO representing a user, with his authorities.
 */
public class UserDTO {

    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1,
        max = 100)
    private String login;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Email
    @Size(min = 5,
        max = 100)
    private String email;

    private boolean activated = false;

    @Size(min = 2,
        max = 5)
    private String langKey;

    private Set<String> authorities;

    private boolean employee = false;

    public UserDTO() {
    }

    public UserDTO(User user) {
        this(user.getLogin(), user.getFirstName(), user.getLastName(),
            user.getEmail(), user.getActivated(), user.getLangKey(),
            user.getAuthorities().stream().map(Authority::getName)
                .collect(Collectors.toSet()));
    }

    public UserDTO(String login, String firstName, String lastName,
        String email, boolean activated, String langKey,
        Set<String> authorities) {

        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.activated = activated;
        this.langKey = langKey;
        this.authorities = authorities;
    }

    public String getLogin() {
        return login;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public boolean isActivated() {
        return activated;
    }

    public String getLangKey() {
        return langKey;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public void setEmployee(boolean employee) {
        this.employee = employee;
    }

    public boolean isEmployee() {
        return employee;
    }

    @Override
    public String toString() {
        return "UserDTO{" + "login='" + login + '\'' + ", firstName='"
            + firstName + '\'' + ", lastName='" + lastName + '\'' + ", email='"
            + email + '\'' + ", activated=" + activated + ", langKey='"
            + langKey + '\'' + ", authorities=" + authorities + ", employee="
            + employee + "}";
    }
}
