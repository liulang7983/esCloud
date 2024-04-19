package com.esTest;

import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.PutIndexTemplateRequest;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.common.settings.Settings;
import org.junit.Test;

import java.io.IOException;

/**
 * @Author ming.li
 * @Date 2024/4/19 16:48
 * @Version 1.0
 */
public class replicasTest {
    public static RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("127.0.0.1", 9200, "http")));
    public static String ES_DB="es_db";
    public static String ESB="esb";
    //修改模版分片数量  --暂时报错
    @Test
    public void test1() {
        try {
            PutIndexTemplateRequest request = new PutIndexTemplateRequest(ESB);
            request.settings(Settings.builder().put(IndexMetaData.SETTING_NUMBER_OF_REPLICAS,0));
            client.indices().putTemplate(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //设置索引的副本分片数量
    @Test
    public void test2() {
        try {
            UpdateSettingsRequest request = new UpdateSettingsRequest(ES_DB);
            request.settings(Settings.builder().put(IndexMetaData.SETTING_NUMBER_OF_REPLICAS,0));
            client.indices().putSettings(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
