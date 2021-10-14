package top.parak.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.parak.gulimall.common.utils.PageUtils;
import top.parak.gulimall.ware.entity.WareOrderTaskDetailEntity;

import java.util.Map;

/**
 * 详细库存工作单
 *
 * @author KHighness
 * @email parakovo@gmail.com
 * @date 2021-02-25 11:26:12
 */
public interface WareOrderTaskDetailService extends IService<WareOrderTaskDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

