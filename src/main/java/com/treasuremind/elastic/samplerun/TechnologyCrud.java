package com.treasuremind.elastic.samplerun;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.GetRequest;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import com.treasuremind.elastic.pojo.Technology;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TechnologyCrud implements CommandLineRunner {

    private final ElasticsearchClient elasticSearchClient;

    public TechnologyCrud(ElasticsearchClient elasticSearchClient) {
        this.elasticSearchClient = elasticSearchClient;
    }

    @Override
    public void run(String... args) throws Exception {
        createTechnologyRecord();
        retrieveTechnologyRecord();
    }

    private void createTechnologyRecord() throws Exception {
        Technology technology = new Technology();
        technology.setId(100);
        technology.setName("Java");
        technology.setSelfRating("9/10");
        IndexRequest.Builder<Technology> indexReqBuilder = new IndexRequest.Builder<>();
        indexReqBuilder.index("hrms");
        indexReqBuilder.id(technology.getId().toString());
        indexReqBuilder.document(technology);
        IndexRequest<Technology> indexRequest = indexReqBuilder.build();
        IndexResponse response = this.elasticSearchClient.index(indexRequest);
        System.out.println("Indexed with version " + response.version());
    }

    private void retrieveTechnologyRecord() throws IOException {
        GetRequest.Builder getRequestBuilder = new GetRequest.Builder();
		getRequestBuilder.index("hrms");
		getRequestBuilder.id("200");

		GetRequest getRequest = getRequestBuilder.build();

		GetResponse<Technology> getResponse = this.elasticSearchClient.get(getRequest, Technology.class);

		Technology technology = getResponse.source();

		System.out.println(technology.getId());
		System.out.println(technology.getName());
		System.out.println(technology.getSelfRating());
    }
}
