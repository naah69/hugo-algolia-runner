package com.naah69.infrastructure.utils;

import com.algolia.search.DefaultSearchClient;
import com.algolia.search.SearchClient;
import com.algolia.search.SearchIndex;
import com.algolia.search.models.indexing.BatchIndexingResponse;
import com.algolia.search.models.indexing.BatchResponse;
import com.naah69.infrastructure.model.Algolia;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * AlgoliaUtils
 *
 * @author naah
 * @date 2018-09-01 下午11:54
 * @desc
 */
@Slf4j
public class AlgoliaUtils {

    private SearchClient client;
    private SearchIndex<Algolia> index;
    private String appId;
    private String key;

    public AlgoliaUtils(String appId, String key, String indexName) {
        this.appId = appId;
        this.key = key;
        client = DefaultSearchClient.create(appId, key);
        this.index = client.initIndex(indexName, Algolia.class);
    }

    /**
     * 更新 Algolia
     *
     * @param list Algolia列表
     * @return 结果
     */
    @SneakyThrows
    public boolean updateAlgolia(List<Algolia> list) {
        try {
            BatchIndexingResponse result = index.saveObjects(list);
            if (result != null) {

                BatchResponse response = result.getResponses().get(0);
                log.info("[ALGOLIA] algolia objectIDs: {}", response.getObjectIDs());
                log.info("[ALGOLIA] algolia TaskID: {}", response.getTaskID());
//                System.out.println("IndexName: " + response.getIndexName());
                return true;
            } else {
                return false;
            }
        } finally {
            client.close();
        }
    }


}
