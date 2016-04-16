package org.clockin.repository.search;

import org.clockin.domain.User;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the User entity.
 */
public interface UserSearchRepository
    extends ElasticsearchRepository<User, Long> {
}
