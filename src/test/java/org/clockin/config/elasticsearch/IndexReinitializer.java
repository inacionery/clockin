package org.clockin.config.elasticsearch;

import static java.lang.System.currentTimeMillis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Component
public class IndexReinitializer {

    private Logger logger = LoggerFactory.getLogger(getClass());
    @Inject
    private ElasticsearchTemplate elasticsearchTemplate;

    @PostConstruct
    public void resetIndex() {
        long t = currentTimeMillis();
        elasticsearchTemplate.deleteIndex("_all");
        t = currentTimeMillis() - t;
        logger.debug("ElasticSearch indexes reset in {} ms", t);
    }
}
