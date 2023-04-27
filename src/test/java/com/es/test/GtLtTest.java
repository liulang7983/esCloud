package com.es.test;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.RegexpQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;

/**
 * @author ming.li
 * @date 2023/2/8 8:39
 */
public class GtLtTest {
    public static RestHighLevelClient client=new RestHighLevelClient(RestClient.builder(new HttpHost("127.0.0.1",9200)));
    @Test
    public void test1()throws Exception{
        SearchSourceBuilder builder = new SearchSourceBuilder();
        //年龄小于20的
        //RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("age").lt(20);
        //年龄大于20的
        RangeQueryBuilder rangeQueryBuilder= QueryBuilders.rangeQuery("age").gt(20);
        SearchRequest request = new SearchRequest("test");
        builder.query(rangeQueryBuilder);
        request.source(builder);
        System.out.println(builder.toString());
        SearchResponse search = client.search(request, RequestOptions.DEFAULT);
        long value = search.getHits().getTotalHits().value;
        System.out.println("value:"+value);
    }

    @Test
    public void test2()throws Exception{
        SearchSourceBuilder builder = new SearchSourceBuilder();
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        //年龄在20-31之间
        RangeQueryBuilder rangeQueryBuilder= QueryBuilders.rangeQuery("age").from(20).to(31);
        boolQuery.filter(rangeQueryBuilder);
        RangeQueryBuilder money = QueryBuilders.rangeQuery("money").gte(200);
        boolQuery.must(money);
        SearchRequest request = new SearchRequest("test");
        builder.query(boolQuery);
        request.source(builder);
        System.out.println(builder.toString());

        SearchResponse search = client.search(request, RequestOptions.DEFAULT);
        long value = search.getHits().getTotalHits().value;
        System.out.println("value:"+value);
    }
}
