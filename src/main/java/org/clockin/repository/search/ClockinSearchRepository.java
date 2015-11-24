package org.clockin.repository.search;

import org.clockin.domain.Clockin;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Clockin entity.
 */
public interface ClockinSearchRepository extends ElasticsearchRepository<Clockin, Long> {
}
