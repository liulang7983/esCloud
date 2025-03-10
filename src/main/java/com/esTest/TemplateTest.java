package com.esTest;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.script.mustache.SearchTemplateRequest;
import org.elasticsearch.script.mustache.SearchTemplateResponse;
import org.junit.Test;

import java.io.IOException;

/**
 * @Author ming.li
 * @Date 2024/7/5 15:13
 * @Version 1.0
 */
public class TemplateTest {
    public static RestHighLevelClient client=new RestHighLevelClient(RestClient.builder(new HttpHost("127.0.0.1",9201)));
    @Test
    public void test1() throws IOException {
        SearchRequest searchRequest=new SearchRequest("esb");
        SearchTemplateRequest templateRequest = new SearchTemplateRequest(searchRequest);
        SearchTemplateResponse template = client.searchTemplate(templateRequest, RequestOptions.DEFAULT);
        template.getResponse();
    }
}
