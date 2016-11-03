package org.clockin.service.impl;

import javax.inject.Inject;

import org.clockin.domain.Email;
import org.clockin.repository.EmailRepository;
import org.clockin.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing Email.
 */
@Service
@Transactional
public class EmailServiceImpl implements EmailService {

    private final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Inject
    private EmailRepository emailRepository;

    /**
     * Save a email.
     *
     * @param email the entity to save
     * @return the persisted entity
     */
    public Email save(Email email) {
        log.debug("Request to save Email : {}", email);
        Email result = emailRepository.save(email);
        return result;
    }

    /**
     *  Get all the emails.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Email> findAll(Pageable pageable) {
        log.debug("Request to get all Emails");
        Page<Email> result = emailRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one email by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Email findOne(Long id) {
        log.debug("Request to get Email : {}", id);
        Email email = emailRepository.findOne(id);
        return email;
    }

    /**
     *  Delete the  email by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Email : {}", id);
        emailRepository.delete(id);
    }
}
