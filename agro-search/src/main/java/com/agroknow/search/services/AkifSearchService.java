package com.agroknow.search.services;

import com.agroknow.domain.Akif;
import com.agroknow.search.domain.AgroSearchRequest;
import com.agroknow.search.domain.AgroSearchResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetRequest;
import org.elasticsearch.action.get.MultiGetRequestBuilder;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.facet.FacetBuilder;
import org.elasticsearch.search.facet.FacetBuilders;
import org.elasticsearch.search.facet.Facets;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author aggelos
 */
@Component
public class AkifSearchService {

    @Autowired
    private TransportClient esClient;

    @Autowired
    private ObjectMapper objectMapper;

    public AgroSearchResponse<Akif> search(AgroSearchRequest req) throws IOException {
        AgroSearchResponse<Akif> res = new AgroSearchResponse<Akif>();

        // create the SearchRequestBuilder
        SearchRequestBuilder searchReqBuilder = esClient.prepareSearch("akif").setTypes("akif");

        // setup query
        QueryBuilder queryBuilder;
        Map<String, String> searchFieldsMap = req.getSearchFields();
        Set<String> searchFields = req.getSearchFields().keySet();
        if(searchFields.size() == 1) {
            String searchField = searchFields.iterator().next();
            queryBuilder = QueryBuilders.matchQuery(searchField, searchFieldsMap.get(searchField));
        } else {
            queryBuilder = QueryBuilders.boolQuery();
            BoolQueryBuilder bqb = (BoolQueryBuilder) queryBuilder;
            for(String searchField : searchFields) {
                bqb.must(QueryBuilders.matchQuery(searchField, searchFieldsMap.get(searchField)));
            }
        }
        searchReqBuilder.setQuery(queryBuilder);

        // setup sorting
        String sortByField = req.getSortByField();
        if (!StringUtils.isEmpty(sortByField)) {
            searchReqBuilder.addSort(sortByField, SortOrder.valueOf(req.getSortOrder()));
        }

        // setup paging
        searchReqBuilder.setFrom( (req.getPage()-1) * req.getPageSize() );
        searchReqBuilder.setSize(req.getPageSize());

        // setup facets
        List<String> facetFields = req.getFacetFields();
        for (String facetField : facetFields) {
            searchReqBuilder.addFacet(FacetBuilders.termsFacet(facetField).field(facetField).size(req.getFacetSize()));
        }

        // execute the request and get the response
        SearchResponse response = searchReqBuilder.execute().actionGet();

        // parse the response into AgroSearchResponse<Akif>
        Iterator<SearchHit> hitsIter = response.getHits().iterator();
        SearchHit hit;
        while (hitsIter.hasNext()) {
            hit = hitsIter.next();

            String source = hit.getSourceAsString();
            Akif doc = objectMapper.reader(Akif.class).readValue(source);
            res.addResult(doc);
        }

        // add facets to res
        Facets facets = response.getFacets();
        if(facets != null) {
            res.setupFacetsFromES(facets);
        }

        // add query (meta)data to res
        res.setSortByField(req.getSortByField());
        res.setSortOrder(req.getSortOrder());
        res.setPage(req.getPage());
        res.setPageSize(req.getPageSize());
        res.setTotal(response.getHits().getTotalHits());
        res.setTime(response.getTookInMillis());

        // ..and
        return res;
    }

    /**
     * Fetch the akif objects of the given ids. When no ids argument an empty
     * response is returned with no results.
     *
     * @param ids
     * @return
     * @throws IOException
     */
    public AgroSearchResponse<Akif> fetch(String[] ids) throws IOException {
        AgroSearchResponse<Akif> res = new AgroSearchResponse<Akif>();
        if (ArrayUtils.isEmpty(ids)) { // return empty res when no ids
            return res;
        }

        // create the MultiGetRequest for akif ids given
        MultiGetRequestBuilder mGetBuilder = esClient.prepareMultiGet();
        MultiGetRequest.Item item;
        for (String id : ids) {
            item = new MultiGetRequest.Item("akif", "akif", id);
            mGetBuilder.add(item);
        }

        // execute the request and get the response
        MultiGetResponse response = mGetBuilder.execute().actionGet();

        // parse the response into AgroSearchResponse<Akif>
        MultiGetItemResponse itemResponse;
        Iterator<MultiGetItemResponse> responseIter = response.iterator();
        while (responseIter.hasNext()) {
            itemResponse = responseIter.next();
            if (itemResponse.isFailed()) {
                continue;
            }
            String source = itemResponse.getResponse().getSourceAsString();
            Akif doc = objectMapper.reader(Akif.class).readValue(source);
            res.increaseTotal();
            res.addResult(doc);
        }

        // ..and
        return res;
    }
}
