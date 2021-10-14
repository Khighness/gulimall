package top.parak.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.parak.gulimall.common.utils.PageUtils;
import top.parak.gulimall.product.entity.SpuInfoDescEntity;

import java.util.Map;

/**
 * spu信息介绍
 *
 * @author KHighness
 * @email parakovo@gmail.com
 * @date 2021-02-24 21:59:22
 */
public interface SpuInfoDescService extends IService<SpuInfoDescEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

