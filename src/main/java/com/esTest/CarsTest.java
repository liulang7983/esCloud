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
import org.elasticsearch.search.aggregations.metrics.*;
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
    public static RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("127.0.0.1", 9201), new HttpHost("127.0.0.1", 9202), new HttpHost("127.0.0.1", 9203)));
    private static String CARS = "cars";

    /*
    按照颜色对车辆分类，并按照数量排序正序和倒序
     */
    @Test
    public void test1() throws Exception {
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
    public void test2() throws Exception {
        SearchRequest searchRequest = new SearchRequest(CARS);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.matchAllQuery());
        //true从小到大,false从大到小
        TermsAggregationBuilder field = AggregationBuilders.terms("group_by_color").field("color").
                order(BucketOrder.aggregation("avg_by_price", true)).
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
            ParsedAvg avgByPrice = (ParsedAvg) asMap.get("avg_by_price");
            System.out.println(avgByPrice.getName() + ":" + avgByPrice.getValue());
            System.out.println();
        }
    }

    /*
  按照颜色对车辆分类，并算出此颜色平均单价，按照平均价格排序
   */
    @Test
    public void test3() throws Exception {
        SearchRequest searchRequest = new SearchRequest(CARS);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.matchAllQuery());
        //true从小到大,false从大到小
        TermsAggregationBuilder field = AggregationBuilders.terms("group_by_color").field("color").
                subAggregation(AggregationBuilders.terms("group_by_brand").field("brand").
                        subAggregation(AggregationBuilders.avg("avg_by_price").field("price")).order(BucketOrder.aggregation("avg_by_price", false)));
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
                System.out.println("docCount1:" + docCount1 + ",keyAsString1,:" + keyAsString1);
                Aggregations aggregations2 = bucket1.getAggregations();
                Map<String, Aggregation> asMap = aggregations2.getAsMap();
                ParsedAvg avgByPrice = (ParsedAvg) asMap.get("avg_by_price");
                System.out.println(avgByPrice.getName() + ":" + avgByPrice.getValue());
            }
            System.out.println();
        }
    }

    /*
    按照颜色分类，算颜色平均价，同时颜色按照款式算平均价
  */
    @Test
    public void test4() throws Exception {
        SearchRequest searchRequest = new SearchRequest(CARS);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.matchAllQuery());
        //avg_by_price_color和group_by_brand在意义上是同级别的，所以不能区别对待
        TermsAggregationBuilder field = AggregationBuilders.terms("group_by_color").field("color").
                order(BucketOrder.aggregation("avg_by_price_color", true)).
                subAggregation(AggregationBuilders.avg("avg_by_price_color").field("price")).
                subAggregation(AggregationBuilders.terms("group_by_brand").field("brand").
                        order(BucketOrder.aggregation("avg_by_price_brand", false)).
                        subAggregation(AggregationBuilders.avg("avg_by_price_brand").field("price")));
        builder.aggregation(field);
        searchRequest.source(builder);
        SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
        Aggregations aggregations = search.getAggregations();
        Terms groupByColor1 = aggregations.get("group_by_color");
        List<? extends Terms.Bucket> buckets = groupByColor1.getBuckets();
        for (int i = 0; i < buckets.size(); i++) {
            Terms.Bucket bucket = buckets.get(i);
            long docCount = bucket.getDocCount();
            String keyAsString = bucket.getKeyAsString();
            System.out.println(docCount);
            System.out.println(keyAsString);
            Aggregations aggregations1 = bucket.getAggregations();
            ParsedAvg avgByPriceColor = aggregations1.get("avg_by_price_color");
            System.out.println(avgByPriceColor.getName()+":"+avgByPriceColor.getValue());
            Terms groupByBrand1 = aggregations1.get("group_by_brand");
            List<? extends Terms.Bucket> buckets1 = groupByBrand1.getBuckets();
            for (int j = 0; j < buckets1.size(); j++) {
                Terms.Bucket bucket1 = buckets1.get(j);
                long docCount1 = bucket1.getDocCount();
                String keyAsString1 = bucket1.getKeyAsString();
                System.out.println("docCount1:" + docCount1 + ",keyAsString1,:" + keyAsString1);
                Aggregations aggregations2 = bucket1.getAggregations();
                ParsedAvg avgByPriceBrand = aggregations2.get("avg_by_price_brand");
                System.out.println(avgByPriceBrand.getName()+":"+avgByPriceBrand.getValue());
            }
            System.out.println();
        }
    }

    /*
    按照颜色分类，算颜色平均价，同时颜色按照款式算平均价(同test4,且代码更整洁几个Aggregation层次更明显)
  */
    @Test
    public void test5() throws Exception {
        SearchRequest searchRequest = new SearchRequest(CARS);
        // 创建搜索源构建器
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        // 创建按颜色分组的聚合
        TermsAggregationBuilder groupByColor = AggregationBuilders.terms("group_by_color")
                .field("color");
        // 设置按颜色分组的排序规则，按 avg_by_price_color 升序排序
        groupByColor.order(BucketOrder.aggregation("avg_by_price_color", true));

        // 创建按颜色分组内的平均价格聚合
        AvgAggregationBuilder avgByPriceColor = AggregationBuilders.avg("avg_by_price_color")
                .field("price");

        // 创建按品牌分组的聚合
        TermsAggregationBuilder groupByBrand = AggregationBuilders.terms("group_by_brand")
                .field("brand");
        // 设置按品牌分组的排序规则，按 avg_by_price_brand 降序排序
        groupByBrand.order(BucketOrder.aggregation("avg_by_price_brand", false));

        // 创建按品牌分组内的平均价格聚合
        AvgAggregationBuilder avgByPriceBrand = AggregationBuilders.avg("avg_by_price_brand")
                .field("price");

        // 将按品牌分组的聚合添加到按颜色分组的聚合中
        groupByBrand.subAggregation(avgByPriceBrand);

        // 将按颜色分组内的平均价格聚合和按品牌分组的聚合添加到按颜色分组的聚合中
        groupByColor.subAggregation(avgByPriceColor);
        groupByColor.subAggregation(groupByBrand);

        // 将按颜色分组的聚合添加到搜索源构建器中
        searchSourceBuilder.aggregation(groupByColor);

        // 设置搜索源构建器到搜索请求中
        searchRequest.source(searchSourceBuilder);

        // 执行搜索请求
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        // 处理搜索响应
        System.out.println(searchResponse);
        Aggregations aggregations = searchResponse.getAggregations();
        Terms groupByColor1 = aggregations.get("group_by_color");
        List<? extends Terms.Bucket> buckets = groupByColor1.getBuckets();
        for (int i = 0; i < buckets.size(); i++) {
            Terms.Bucket bucket = buckets.get(i);
            long docCount = bucket.getDocCount();
            String keyAsString = bucket.getKeyAsString();
            System.out.println(docCount);
            System.out.println(keyAsString);
            Aggregations aggregations1 = bucket.getAggregations();
            ParsedAvg avgByPriceColor1 = aggregations1.get("avg_by_price_color");
            System.out.println(avgByPriceColor1.getName()+":"+avgByPriceColor1.getValue());
            Terms groupByBrand1 = aggregations1.get("group_by_brand");
            List<? extends Terms.Bucket> buckets1 = groupByBrand1.getBuckets();
            for (int j = 0; j < buckets1.size(); j++) {
                Terms.Bucket bucket1 = buckets1.get(j);
                long docCount1 = bucket1.getDocCount();
                String keyAsString1 = bucket1.getKeyAsString();
                System.out.println("docCount1:" + docCount1 + ",keyAsString1,:" + keyAsString1);
                Aggregations aggregations2 = bucket1.getAggregations();
                ParsedAvg avgByPriceBrand1 = aggregations2.get("avg_by_price_brand");
                System.out.println(avgByPriceBrand1.getName()+":"+avgByPriceBrand1.getValue());
            }
            System.out.println();
        }

    }

    /*
    按照颜色分类，算出最高，最低，平均价
  */
    @Test
    public void test6() throws Exception {
        SearchRequest searchRequest = new SearchRequest(CARS);
        // 创建搜索源构建器
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        // 创建按颜色分组的聚合
        TermsAggregationBuilder groupByColor = AggregationBuilders.terms("group_by_color")
                .field("color");
        // 创建按颜色分组内的平均价格聚合
        AvgAggregationBuilder avgByPriceColor = AggregationBuilders.avg("avg_price")
                .field("price");

        // 创建按按颜色求单价最大值，最小值，平均值
        MaxAggregationBuilder maxAggregationBuilder = AggregationBuilders.max("max_price").field("price");
        MinAggregationBuilder minAggregationBuilder = AggregationBuilders.min("min_price").field("price");

        // 创建按颜色分组内单价的总价聚合
        SumAggregationBuilder sumAggregationBuilder = AggregationBuilders.sum("sum_price").field("price");

        // 将按颜色分组内单价最大值，最小值，平均值添加到按颜色分组的聚合中
        groupByColor.subAggregation(avgByPriceColor);
        groupByColor.subAggregation(maxAggregationBuilder);
        groupByColor.subAggregation(minAggregationBuilder);
        groupByColor.subAggregation(sumAggregationBuilder);
        // 将按颜色分组的聚合添加到搜索源构建器中
        searchSourceBuilder.aggregation(groupByColor);

        // 设置搜索源构建器到搜索请求中
        searchRequest.source(searchSourceBuilder);

        // 执行搜索请求
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        // 处理搜索响应
        System.out.println(searchResponse);
        Aggregations aggregations = searchResponse.getAggregations();
        Terms groupByColor1 = aggregations.get("group_by_color");
        List<? extends Terms.Bucket> buckets = groupByColor1.getBuckets();
        for (int i = 0; i < buckets.size(); i++) {
            Terms.Bucket bucket = buckets.get(i);
            long docCount = bucket.getDocCount();
            String keyAsString = bucket.getKeyAsString();
            System.out.println(docCount);
            System.out.println(keyAsString);
            Aggregations aggregations1 = bucket.getAggregations();
            ParsedAvg avgPrice = aggregations1.get("avg_price");
            System.out.println(avgPrice.getName()+":"+avgPrice.getValue());
            ParsedMax maxPrice = aggregations1.get("max_price");
            System.out.println(maxPrice.getName()+":"+maxPrice.getValue());
            ParsedMin minPrice = aggregations1.get("min_price");
            System.out.println(minPrice.getName()+":"+minPrice.getValue());
            ParsedSum sumPrice = aggregations1.get("sum_price");
            System.out.println(sumPrice.getName()+":"+sumPrice.getValue());
            System.out.println();
        }

    }
}
