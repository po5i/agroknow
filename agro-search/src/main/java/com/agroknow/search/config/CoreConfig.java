package com.agroknow.search.config;

import com.agroknow.domain.agrif.Agrif;
import com.agroknow.domain.akif.Akif;
import com.agroknow.domain.parser.json.CustomObjectMapper;
import com.agroknow.search.services.SearchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.client.transport.TransportClient;
import static org.elasticsearch.common.settings.ImmutableSettings.settingsBuilder;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.util.Assert;

@Configuration
@ComponentScan({ "com.agroknow.search.services"})
@EnableAsync
@EnableAspectJAutoProxy
public class CoreConfig {

    @Bean
    public ObjectMapper objectMapper() {
        CustomObjectMapper objectMapper = new CustomObjectMapper();
        objectMapper.init();
        return objectMapper;
    }

    @Bean
    public SearchService<Akif> akifSearchService() {
        return new SearchService<Akif>();
    }

    @Bean
    public SearchService<Agrif> agrifSearchService() {
        return new SearchService<Agrif>();
    }

    @Bean
    public TransportClient elasticsearchClient() {
        String clusterName = "agroknow";
        String[] clusterNodes = new String[]{"agro01.keevosh.gr:9300","agro02.keevosh.gr:9300","agro03.keevosh.gr:9300"};

        TransportClient client = new TransportClient(settingsBuilder()
                .put("cluster.name", clusterName)
                .put("client.transport.ping_timeout", "10s")
                //TODO maybe add more advanced settings
                //.put("client.transport.sniff", )
                //.put("client.transport.ignore_cluster_name", )
                //.put("client.transport.nodes_sampler_interval", )
                .build());

        Assert.notEmpty(clusterNodes, "[Assertion failed] clusterNodes setting not set");
        for (String clusterNode : clusterNodes) {
            String hostName = StringUtils.substringBefore(clusterNode, ":");
            String port = StringUtils.substringAfter(clusterNode, ":");
            Assert.hasText(hostName, "[Assertion failed] missing host name in 'clusterNodes'");
            Assert.hasText(port, "[Assertion failed] missing port in 'clusterNodes'");
            client.addTransportAddress(new InetSocketTransportAddress(hostName, Integer.valueOf(port)));
        }
        client.connectedNodes();

        return client;
    }

    @Bean
    public ElasticsearchTemplate elasticsearchTemplate() {
        return new ElasticsearchTemplate(elasticsearchClient());
    }
}
