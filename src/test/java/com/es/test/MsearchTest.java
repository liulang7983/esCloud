package com.es.test;

import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * @author ming.li
 * @date 2022/12/15 14:35
 */
public class MsearchTest {

    public static RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("127.0.0.1", 9200)));
    public static String ES_DB = "es_db";

    /**
     * 原始的一条语句多个聚合
     */
    @Test
    public void test1() throws IOException {
        SearchRequest request = new SearchRequest(ES_DB);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.aggregation(AggregationBuilders.terms("ageagg").field("age").size(10));
        builder.aggregation(AggregationBuilders.terms("nameagg").field("name").size(10));
        request.source(builder);
        SearchResponse search = client.search(request, RequestOptions.DEFAULT);
        Map<String, Aggregation> asMap = search.getAggregations().asMap();
        String[] aggStr = new String[]{"ageagg", "nameagg"};
        for (String s : aggStr) {
            System.out.println(s);
            Terms terms = (Terms) asMap.get(s);
            Iterator<? extends Terms.Bucket> iterator = terms.getBuckets().iterator();
            while (iterator.hasNext()) {
                Terms.Bucket next = iterator.next();
                String keyAsString = next.getKeyAsString();
                long docCount = next.getDocCount();
                System.out.println("key:" + keyAsString);
                System.out.println("docCount聚合的这个key的条数：" + docCount);
                System.out.println();
            }
        }


    }

    /**
     * 使用msearch多语句同时聚合(类似于多线程)
     */
    @Test
    public void test2() throws IOException {
        SearchRequest ageRequest = new SearchRequest(ES_DB);
        SearchRequest nameRequest = new SearchRequest(ES_DB);
        SearchSourceBuilder ageBuilder = new SearchSourceBuilder();
        SearchSourceBuilder nameBuilder = new SearchSourceBuilder();
        TermsAggregationBuilder ageagg = AggregationBuilders.terms("ageagg").field("age");
        TermsAggregationBuilder nameagg = AggregationBuilders.terms("nameagg").field("name");
        ageBuilder.size(0);
        nameBuilder.size(0);
        ageBuilder.aggregation(ageagg);
        nameBuilder.aggregation(nameagg);
        ageRequest.source(ageBuilder);
        nameRequest.source(nameBuilder);
        MultiSearchRequest multiSearchRequest = new MultiSearchRequest();
        multiSearchRequest.add(ageRequest);
        multiSearchRequest.add(nameRequest);
        MultiSearchResponse msearch = client.msearch(multiSearchRequest, RequestOptions.DEFAULT);
        MultiSearchResponse.Item[] responses = msearch.getResponses();
        for (int i = 0; i < responses.length; i++) {
            MultiSearchResponse.Item item = responses[i];
            Aggregations aggregations = item.getResponse().getAggregations();
            Terms age = (Terms) aggregations.get("ageagg");
            if (age != null) {
                Iterator<? extends Terms.Bucket> iterator = age.getBuckets().iterator();
                while (iterator.hasNext()) {
                    Terms.Bucket next = iterator.next();
                    String keyAsString = next.getKeyAsString();
                    long docCount = next.getDocCount();
                    System.out.println("ageagg-key:" + keyAsString);
                    System.out.println("ageagg-docCount聚合的这个key的条数：" + docCount);
                    System.out.println();
                }
                continue;
            }
            Terms name = (Terms) aggregations.get("nameagg");
            if (name != null) {
                Iterator<? extends Terms.Bucket> iterator = name.getBuckets().iterator();
                while (iterator.hasNext()) {
                    Terms.Bucket next = iterator.next();
                    String keyAsString = next.getKeyAsString();
                    long docCount = next.getDocCount();
                    System.out.println("nameagg-key:" + keyAsString);
                    System.out.println("nameagg-docCount聚合的这个key的条数：" + docCount);
                    System.out.println();
                }
                continue;
            }
        }
    }

    @Test
    public void test3() throws Exception {
        SearchRequest request = new SearchRequest(ES_DB);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.aggregation(AggregationBuilders.terms("ageagg").field("age").size(10));
        builder.aggregation(AggregationBuilders.terms("nameagg").field("name").size(10));
        request.source(builder);
        SearchResponse search = client.search(request, RequestOptions.DEFAULT);
        Map<String, Aggregation> asMap = search.getAggregations().asMap();
        String[] aggStr = new String[]{"ageagg", "nameagg"};
        for (String s : aggStr) {
            Terms terms = (Terms) asMap.get(s);
            Iterator<? extends Terms.Bucket> iterator = terms.getBuckets().iterator();
            while (iterator.hasNext()) {
                Terms.Bucket next = iterator.next();
                long docCount = next.getDocCount();
                String keyAsString = next.getKeyAsString();
                System.out.println(s + "的值为:" + keyAsString + ",条数:" + docCount);
            }
        }
    }

    @Test
    public void test4() throws Exception {
        MultiSearchRequest multiSearchRequest = new MultiSearchRequest();
        //添加年龄聚合
        SearchRequest ageRequest = new SearchRequest();
        SearchSourceBuilder ageBuilder = new SearchSourceBuilder();
        ageBuilder.aggregation(AggregationBuilders.terms("ageagg").field("age")).size(0);
        ageRequest.source(ageBuilder);
        multiSearchRequest.add(ageRequest);
        //添加名称聚合
        SearchRequest nameRequest = new SearchRequest();
        SearchSourceBuilder nameBuilder = new SearchSourceBuilder();
        nameBuilder.aggregation(AggregationBuilders.terms("nameagg").field("name")).size(0);
        nameRequest.source(nameBuilder);
        multiSearchRequest.add(nameRequest);
        MultiSearchResponse msearch = client.msearch(multiSearchRequest, RequestOptions.DEFAULT);
        MultiSearchResponse.Item[] responses = msearch.getResponses();
        for (MultiSearchResponse.Item item : responses) {
            Aggregations aggregations = item.getResponse().getAggregations();
            Terms age = (Terms) aggregations.get("ageagg");
            if (age != null) {
                Iterator<? extends Terms.Bucket> ageIterator = age.getBuckets().iterator();
                while (ageIterator.hasNext()) {
                    Terms.Bucket next = ageIterator.next();
                    long docCount = next.getDocCount();
                    String keyAsString = next.getKeyAsString();
                    System.out.println("ageagg,名字:" + keyAsString + ",条数:" + docCount);
                }
                System.out.println();
            }
            Terms name = (Terms) aggregations.get("nameagg");
            if (name != null) {
                Iterator<? extends Terms.Bucket> nameIterator = name.getBuckets().iterator();
                while (nameIterator.hasNext()) {
                    Terms.Bucket next = nameIterator.next();
                    long docCount = next.getDocCount();
                    String keyAsString = next.getKeyAsString();
                    System.out.println("nameagg,名字:" + keyAsString + ",条数:" + docCount);
                }
                System.out.println();
            }

        }
    }
}
