package top.parak.gulimall.search.service;

import top.parak.gulimall.common.to.es.SkuEsModel;

import java.io.IOException;
import java.util.List;

/**
 * @author KHighness
 * @since 2021-11-10
 * @email parakovo@gmail.com
 */
public interface ProductSaveService {

    boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException;

}
