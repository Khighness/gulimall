package top.parak.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.parak.gulimall.common.utils.PageUtils;
import top.parak.gulimall.product.entity.BrandEntity;

import java.util.List;
import java.util.Map;

/**
 * 品牌
 *
 * @author KHighness
 * @email parakovo@gmail.com
 * @date 2021-09-24 21:59:22
 */
public interface BrandService extends IService<BrandEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 更新品牌信息
     * @param brand 信息
     */
    void updateDetail(BrandEntity brand);

    /**
     * 批量查询品牌信息
     * @param brandIds 品牌ID数组
     * @return 品牌信息列表
     */
    List<BrandEntity> getBrandByIds(List<Long> brandIds);

}

