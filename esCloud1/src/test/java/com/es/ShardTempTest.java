package com.es;

import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.cluster.health.ClusterIndexHealth;
import org.elasticsearch.common.recycler.Recycler;
import org.elasticsearch.common.settings.Settings;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author ming.li
 * @date 2022/11/24 8:47
 */
public class ShardTempTest {
    public static RestHighLevelClient client=new RestHighLevelClient(RestClient.builder(new HttpHost("127.0.0.1",9200)));
    public static String SHARD="job_idx_shard_temp";

    @Test
    public void settings() throws IOException {
        UpdateSettingsRequest settings = new UpdateSettingsRequest().settings(Settings.builder().put("index.number_of_replicas", 0));
        settings.indices(SHARD);
        AcknowledgedResponse acknowledgedResponse = client.indices().putSettings(settings, RequestOptions.DEFAULT);
        System.out.println(acknowledgedResponse.isAcknowledged());
    }


}
