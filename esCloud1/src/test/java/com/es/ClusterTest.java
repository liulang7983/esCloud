package com.es;

import net.minidev.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.admin.indices.settings.get.GetSettingsRequest;
import org.elasticsearch.action.admin.indices.settings.get.GetSettingsResponse;
import org.elasticsearch.client.*;
import org.elasticsearch.client.enrich.StatsRequest;
import org.elasticsearch.client.enrich.StatsResponse;
import org.elasticsearch.client.indices.GetMappingsRequest;
import org.elasticsearch.client.indices.GetMappingsResponse;
import org.elasticsearch.cluster.health.ClusterIndexHealth;
import org.elasticsearch.cluster.metadata.AliasMetaData;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author ming.li
 * @date 2022/11/24 9:50
 */
public class ClusterTest {
    public static RestHighLevelClient client=new RestHighLevelClient(RestClient.builder(new HttpHost("127.0.0.1",9200)));
    @Test
    public void status ()throws IOException {{
        Response response = client.getLowLevelClient().performRequest(new Request("GET", "/_cat/indices"));
        // 3、数据处理
        HttpEntity entity = response.getEntity();
        System.out.println("打印:entity:"+entity);
        String responseStr = EntityUtils.toString(entity, StandardCharsets.UTF_8);
        System.out.println(responseStr);
        // 4、数据分解
        String[] indexInfoArr = responseStr.split("\n");
        for (String indexInfo : indexInfoArr) {
            // 4.1、索引信息输出
            String[] infoArr = indexInfo.split("\\s+");
            String status = infoArr[0];
            String open = infoArr[1];
            String name = infoArr[2];
            String id = infoArr[3];
            String mainShardNum = infoArr[4];
            String viceShardNum = infoArr[5];
            String docNum = infoArr[6];
            String deletedDocNum = infoArr[7];
            String allShardSize = infoArr[8];
            String mainShardSize = infoArr[9];
            System.out.println("》》》》》》》》索引信息》》》》》》》》");
            System.out.println("名称：" + name);
            System.out.println("id：" + id);
            System.out.println("状态：" + status);
            System.out.println("是否开放：" + open);
            System.out.println("主分片数量：" + mainShardNum);
            System.out.println("副本分片数量：" + viceShardNum);
            System.out.println("Lucene文档数量：" + docNum);
            System.out.println("被删除文档数量：" + deletedDocNum);
            System.out.println("所有分片大小：" + allShardSize);
            System.out.println("主分片大小：" + mainShardSize);
        }
        // 6、关闭ES客户端对象
        }
    }

    @Test
    public void mapping ()throws IOException {{
        Response response = client.getLowLevelClient().performRequest(new Request("GET", "/_cat/indices"));
        // 3、数据处理
        HttpEntity entity = response.getEntity();
        System.out.println("打印:entity:"+entity);
        String responseStr = EntityUtils.toString(entity, StandardCharsets.UTF_8);
        System.out.println(responseStr);
        // 4、数据分解
        String[] indexInfoArr = responseStr.split("\n");
        for (String indexInfo : indexInfoArr) {
            // 4.1、索引信息输出
            String[] infoArr = indexInfo.split("\\s+");
            String name = infoArr[2];
            System.out.println("index名字:"+name);
            //构建请求
            GetMappingsRequest request = new GetMappingsRequest().indices(name);
            //使用RestHighLevelClient发起请求
            GetMappingsResponse mapping = client.indices().getMapping(request, RequestOptions.DEFAULT);
            Map<String, MappingMetaData> mappings = mapping.mappings();
            Iterator<String> iterator = mappings.keySet().iterator();
            System.out.println(iterator.hasNext());
            while (iterator.hasNext()){
                String next = iterator.next();
                System.out.println(next+":"+mappings.get(next));
                MappingMetaData mappingMetaData = mappings.get(next);
                Map<String, Object> sourceAsMap = mappingMetaData.getSourceAsMap();
                Object properties = sourceAsMap.get("properties");
                System.out.println(properties);
            }

            System.out.println("==================");
        }
        // 6、关闭ES客户端对象
    }
    }

    @Test
    public  void index() {
        try {
            GetAliasesRequest request = new GetAliasesRequest();
            GetAliasesResponse getAliasesResponse =  client.indices().getAlias(request,RequestOptions.DEFAULT);
            Map<String, Set<AliasMetaData>> map = getAliasesResponse.getAliases();
            Set<String> indices = map.keySet();
            for (String key : indices) {
                System.out.println(key);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void setting ()throws IOException {
        GetSettingsRequest getSettings=new GetSettingsRequest().indices("job_idx_shard_temp");
        GetSettingsResponse getSettingsResponse=client.indices().getSettings(getSettings, RequestOptions.DEFAULT);

        String numberOfShardsString = getSettingsResponse.getSetting("job_idx_shard_temp", "index.number_of_shards");
        System.out.println(numberOfShardsString);

    }

}
