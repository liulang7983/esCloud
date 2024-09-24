package com.esTest;

import com.alibaba.fastjson2.JSONObject;
import com.bean.EsDb;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.Avg;
import org.elasticsearch.search.aggregations.metrics.Max;
import org.elasticsearch.search.aggregations.metrics.Min;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author ming.li
 * @date 2023/11/15 15:39
 */
public class aggTest {
    public static RestHighLevelClient client=new RestHighLevelClient(RestClient.builder(new HttpHost("127.0.0.1",9200)));
    private static String LM="lm";

    //求age字段所有出现的值，类似于distinct
    @Test
    public void test1() throws IOException {
        SearchRequest request = new SearchRequest(LM);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.matchAllQuery());
        builder.aggregation(AggregationBuilders.terms("age_agg").field("age"));
        request.source(builder);
        SearchResponse search = client.search(request,RequestOptions.DEFAULT);
        Aggregations aggregations = search.getAggregations();
        Terms ageAgg = aggregations.get("age_agg");
        List<? extends Terms.Bucket> buckets = ageAgg.getBuckets();
        for (int i = 0; i <buckets.size() ; i++) {
            Terms.Bucket bucket = buckets.get(i);
            String keyAsString = bucket.getKeyAsString();
            long docCount = bucket.getDocCount();
            System.out.println("值为:"+keyAsString+"的值出现:"+docCount+"次");

        }
    }

    //求age字段所有出现的值，类似于distinct,并取出这几个分组money字段的平均，最大，最小值
    @Test
    public void test2() throws IOException {
        SearchRequest request = new SearchRequest(LM);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.matchAllQuery());
        builder.aggregation(AggregationBuilders.terms("age_agg").field("age")
        .subAggregation(AggregationBuilders.avg("money_agg").field("money"))
        .subAggregation(AggregationBuilders.max("money_max").field("money"))
        .subAggregation(AggregationBuilders.min("money_min").field("money")));
        request.source(builder);
        SearchResponse search = client.search(request,RequestOptions.DEFAULT);
        Aggregations aggregations = search.getAggregations();
        Terms ageAgg = aggregations.get("age_agg");
        List<? extends Terms.Bucket> buckets = ageAgg.getBuckets();
        for (int i = 0; i <buckets.size() ; i++) {
            Terms.Bucket bucket = buckets.get(i);
            String keyAsString = bucket.getKeyAsString();
            long docCount = bucket.getDocCount();
            Aggregations aggregations1 = bucket.getAggregations();
            Avg money_agg = aggregations1.get("money_agg");
            double value = money_agg.getValue();
            Max money_max=aggregations1.get("money_max");
            double value1 = money_max.getValue();
            Min money_min=aggregations1.get("money_min");
            double value2 = money_min.getValue();
            System.out.println("age值为:"+keyAsString+"的值出现:"+docCount+"次,他的money的平均值为:"+value+",最大值为:"+value1+","+"最小值为:"+value2);

        }
    }

}
