package org.clockin.repository.search;

import org.clockin.domain.Employee;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Employee entity.
 */
public interface EmployeeSearchRepository
    extends ElasticsearchRepository<Employee, Long> {
}
