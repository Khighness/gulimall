package top.parak.gulimall.ware.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.parak.gulimall.common.utils.PageUtils;
import top.parak.gulimall.common.utils.Query;

import top.parak.gulimall.ware.dao.WareInfoDao;
import top.parak.gulimall.ware.entity.WareInfoEntity;
import top.parak.gulimall.ware.service.WareInfoService;

/**
 * 仓库信息
 *
 * @author KHighness
 * @email parakovo@gmail.com
 * @date 2021-02-25 11:26:12
 */
@Service("wareInfoService")
public class WareInfoServiceImpl extends ServiceImpl<WareInfoDao, WareInfoEntity> implements WareInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WareInfoEntity> page = this.page(
                new Query<WareInfoEntity>().getPage(params),
                new QueryWrapper<WareInfoEntity>()
        );

        return new PageUtils(page);
    }

}
