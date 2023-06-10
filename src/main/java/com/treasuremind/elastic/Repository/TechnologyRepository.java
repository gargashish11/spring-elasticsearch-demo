package com.treasuremind.elastic.Repository;

import com.treasuremind.elastic.pojo.Technology;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface TechnologyRepository extends ElasticsearchRepository<Technology,Integer> {
}
