package top.parak.gulimall.ware.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.parak.gulimall.common.utils.PageUtils;
import top.parak.gulimall.common.utils.Query;

import top.parak.gulimall.ware.dao.PurchaseDao;
import top.parak.gulimall.ware.entity.PurchaseEntity;
import top.parak.gulimall.ware.service.PurchaseService;

/**
 * 采购信息
 *
 * @author KHighness
 * @email parakovo@gmail.com
 * @date 2021-02-25 11:26:12
 */
@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

}