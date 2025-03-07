package com.esTest;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ParsedAvg;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * @Author ming.li
 * @Date 2025/3/7 13:41
 * @Version 1.0
 */
public class CarsTest {
    public static RestHighLevelClient client=new RestHighLevelClient(RestClient.builder(new HttpHost("127.0.0.1",9201),new HttpHost("127.0.0.1",9202),new HttpHost("127.0.0.1",9203)));
    private static String CARS="cars";
    /*
    按照颜色对车辆分类，并按照数量排序正序和倒序
     */
    @Test
    public void test1()throws Exception{
        SearchRequest searchRequest = new SearchRequest(CARS);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.matchAllQuery());
        //true从小到大,false从大到小
        TermsAggregationBuilder field = AggregationBuilders.terms("group_by_color").field("color").order(BucketOrder.count(true));
        builder.aggregation(field);
        searchRequest.source(builder);
        SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
        Aggregations aggregations = search.getAggregations();
        Terms groupByColor = aggregations.get("group_by_color");
        List<? extends Terms.Bucket> buckets = groupByColor.getBuckets();
        for (int i = 0; i < buckets.size(); i++) {
            Terms.Bucket bucket = buckets.get(i);
            long docCount = bucket.getDocCount();
            String keyAsString = bucket.getKeyAsString();
            System.out.println(docCount);
            System.out.println(keyAsString);
        }
    }
    /*
    按照颜色对车辆分类，并算出此颜色平均单价，按照平均价格排序
     */
    @Test
    public void test2()throws Exception{
        SearchRequest searchRequest = new SearchRequest(CARS);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.matchAllQuery());
        //true从小到大,false从大到小
        TermsAggregationBuilder field = AggregationBuilders.terms("group_by_color").field("color").
                order(BucketOrder.aggregation("avg_by_price",true)).
                subAggregation(AggregationBuilders.avg("avg_by_price").field("price"));
        builder.aggregation(field);
        searchRequest.source(builder);
        SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
        Aggregations aggregations = search.getAggregations();
        Terms groupByColor = aggregations.get("group_by_color");
        List<? extends Terms.Bucket> buckets = groupByColor.getBuckets();
        for (int i = 0; i < buckets.size(); i++) {
            Terms.Bucket bucket = buckets.get(i);
            long docCount = bucket.getDocCount();
            String keyAsString = bucket.getKeyAsString();
            System.out.println(docCount);
            System.out.println(keyAsString);
            Aggregations aggregations1 = bucket.getAggregations();
            Map<String, Aggregation> asMap = aggregations1.getAsMap();
            ParsedAvg avgByPrice = (ParsedAvg)asMap.get("avg_by_price");
            System.out.println(avgByPrice.getName()+":"+avgByPrice.getValue());
            System.out.println();
        }
    }

    /*
  按照颜色对车辆分类，并算出此颜色平均单价，按照平均价格排序
   */
    @Test
    public void test3()throws Exception{
        SearchRequest searchRequest = new SearchRequest(CARS);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.matchAllQuery());
        //true从小到大,false从大到小
        TermsAggregationBuilder field = AggregationBuilders.terms("group_by_color").field("color").
                subAggregation(AggregationBuilders.terms("group_by_brand").field("brand").
                        subAggregation(AggregationBuilders.avg("avg_by_price").field("price")).order(BucketOrder.aggregation("avg_by_price",false)));
        builder.aggregation(field);
        searchRequest.source(builder);
        SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
        Aggregations aggregations = search.getAggregations();
        Terms groupByColor = aggregations.get("group_by_color");
        List<? extends Terms.Bucket> buckets = groupByColor.getBuckets();
        for (int i = 0; i < buckets.size(); i++) {
            Terms.Bucket bucket = buckets.get(i);
            long docCount = bucket.getDocCount();
            String keyAsString = bucket.getKeyAsString();
            System.out.println(docCount);
            System.out.println(keyAsString);
            Aggregations aggregations1 = bucket.getAggregations();
            Terms groupByBrand = aggregations1.get("group_by_brand");
            List<? extends Terms.Bucket> buckets1 = groupByBrand.getBuckets();
            for (int j = 0; j < buckets1.size(); j++) {
                Terms.Bucket bucket1 = buckets1.get(j);
                long docCount1 = bucket1.getDocCount();
                String keyAsString1 = bucket1.getKeyAsString();
                System.out.println("docCount1:"+docCount1+",keyAsString1,:"+keyAsString1);
                Aggregations aggregations2 = bucket1.getAggregations();
                Map<String, Aggregation> asMap = aggregations2.getAsMap();
                ParsedAvg avgByPrice = (ParsedAvg)asMap.get("avg_by_price");
                System.out.println(avgByPrice.getName()+":"+avgByPrice.getValue());
            }
            System.out.println();
        }
    }
}
