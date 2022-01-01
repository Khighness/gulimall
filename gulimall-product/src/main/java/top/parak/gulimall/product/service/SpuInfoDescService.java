package top.parak.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.parak.gulimall.common.utils.PageUtils;
import top.parak.gulimall.product.entity.SpuInfoDescEntity;

import java.util.Map;

/**
 * spu信息介绍
 *
 * @author KHighness
 * @since 2021-09-25
 * @email parakovo@gmail.com
 */
public interface SpuInfoDescService extends IService<SpuInfoDescEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存SPU的描述信息
     * @param spuInfoDescEntity SPU描述实体
     */
    void saveSpuInfoDescript(SpuInfoDescEntity spuInfoDescEntity);

}

