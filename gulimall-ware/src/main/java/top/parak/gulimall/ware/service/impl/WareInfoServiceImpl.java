package top.parak.gulimall.ware.service.impl;

import com.alibaba.fastjson.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Random;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import top.parak.gulimall.common.utils.PageUtils;
import top.parak.gulimall.common.utils.Query;

import top.parak.gulimall.common.utils.R;
import top.parak.gulimall.common.vo.MemberResponseVo;
import top.parak.gulimall.ware.dao.WareInfoDao;
import top.parak.gulimall.ware.entity.WareInfoEntity;
import top.parak.gulimall.ware.feign.MemberFeignService;
import top.parak.gulimall.ware.service.WareInfoService;
import top.parak.gulimall.ware.vo.FareVo;
import top.parak.gulimall.ware.vo.MemberAddressVo;

/**
 * 仓库信息
 *
 * @author KHighness
 * @since 2021-09-25
 * @email parakovo@gmail.com
 */
@Service("wareInfoService")
public class WareInfoServiceImpl extends ServiceImpl<WareInfoDao, WareInfoEntity> implements WareInfoService {

    @Autowired
    private MemberFeignService memberFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<WareInfoEntity> queryWrapper = new QueryWrapper<>();

        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            queryWrapper.eq("id", key)
                    .or().like("name", key)
                    .or().like("address", key);
        }

        IPage<WareInfoEntity> page = this.page(
                new Query<WareInfoEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public FareVo getFare(Long addrId) {
        FareVo fareVo = new FareVo();

        R r = memberFeignService.addrInfo(addrId);
        MemberAddressVo memberAddressVo = r.getData("memberReceiveAddress", new TypeReference<MemberAddressVo>() { });
        fareVo.setAddress(memberAddressVo);

        if (!ObjectUtils.isEmpty(memberAddressVo)) {
            String phone = memberAddressVo.getPhone();
            if (phone == null || phone.length() < 2) {
                phone = new Random().nextInt(100) + "";
            }
            fareVo.setFare(new BigDecimal(phone.substring(phone.length() - 1)));
        } else {
            fareVo.setFare(new BigDecimal("20"));
        }

        return fareVo;
    }

}
