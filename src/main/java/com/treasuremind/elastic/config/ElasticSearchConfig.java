package com.treasuremind.elastic.config;

import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchClients;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;

@Configuration
@EnableElasticsearchRepositories
public class ElasticSearchConfig extends ElasticsearchConfiguration {

    private SSLContext sslContext;

    public ElasticSearchConfig(Environment env) throws IOException {
        try {
            String keyStorePath = env.getProperty("spring.elasticsearch.truststore");
            String keyStorePassword = env.getProperty("spring.elasticsearch.truststore-password");
            File trustStoreLocationFile = new ClassPathResource(keyStorePath).getFile();
            SSLContextBuilder sslContextBuilder =
                    SSLContexts
                            .custom()
                            .loadTrustMaterial
                                    (trustStoreLocationFile, keyStorePassword.toCharArray());
            this.sslContext = sslContextBuilder.build();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo("localhost:9200")
                .usingSsl(this.sslContext)
                .withBasicAuth("elastic", "123456")
                .withClientConfigurer(ElasticsearchClients
                        .ElasticsearchRestClientConfigurationCallback
                        .from(restClientBuilder -> {
                            // configure the Elasticsearch RestClient
                            return restClientBuilder;
                        }))
                .build();
    }
}