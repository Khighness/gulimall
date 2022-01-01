package top.parak.gulimall.search.service.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.parak.gulimall.common.to.es.SkuEsModel;
import top.parak.gulimall.search.config.GulimallElasticSearchConfig;
import top.parak.gulimall.search.constant.EsConstant;
import top.parak.gulimall.search.service.ProductSaveService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author KHighness
 * @since 2021-11-10
 * @email parakovo@gmail.com
 */
@Slf4j
@Service
public class ProductSaveServiceImpl implements ProductSaveService {

    @Autowired
    private RestHighLevelClient client;

    @Override
    public boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException {
        // 创建索引，建立映射关系

        // 在es中批量保存数据
        BulkRequest bulkRequest = new BulkRequest();
        for (SkuEsModel skuEsModel : skuEsModels) {
            IndexRequest indexRequest = new IndexRequest(EsConstant.PRODUCT_INDEX);
            indexRequest.id(skuEsModel.getSkuId().toString());
            String s = JSON.toJSONString(skuEsModel);
            indexRequest.source(s, XContentType.JSON);

            bulkRequest.add(indexRequest);
        }

        BulkResponse bulkResponse = client.bulk(bulkRequest, GulimallElasticSearchConfig.COMMON_OPTIONS);

        // 错误处理
        boolean hasFailures = bulkResponse.hasFailures();
        if (hasFailures) {
            List<String> errors = Arrays.stream(bulkResponse.getItems()).map(BulkItemResponse::getId)
                    .collect(Collectors.toList());
            log.error("商品上架错误：{}", errors);
        }

        return !hasFailures;
    }

}
