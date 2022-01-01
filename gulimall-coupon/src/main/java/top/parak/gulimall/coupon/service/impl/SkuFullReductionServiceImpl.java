package top.parak.gulimall.coupon.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.parak.gulimall.common.to.MemberPrice;
import top.parak.gulimall.common.to.SkuReductionTo;
import top.parak.gulimall.common.utils.PageUtils;
import top.parak.gulimall.common.utils.Query;

import top.parak.gulimall.coupon.dao.SkuFullReductionDao;
import top.parak.gulimall.coupon.entity.MemberPriceEntity;
import top.parak.gulimall.coupon.entity.SkuFullReductionEntity;
import top.parak.gulimall.coupon.entity.SkuLadderEntity;
import top.parak.gulimall.coupon.service.MemberPriceService;
import top.parak.gulimall.coupon.service.SkuFullReductionService;
import top.parak.gulimall.coupon.service.SkuLadderService;

/**
 * 商品满减信息
 *
 * @author KHighness
 * @since 2021-10-16
 * @email parakovo@gmail.com
 */
@Service("skuFullReductionService")
public class SkuFullReductionServiceImpl extends ServiceImpl<SkuFullReductionDao, SkuFullReductionEntity> implements SkuFullReductionService {

    @Autowired
    private SkuLadderService skuLadderService;

    @Autowired
    private MemberPriceService memberPriceService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuFullReductionEntity> page = this.page(
                new Query<SkuFullReductionEntity>().getPage(params),
                new QueryWrapper<SkuFullReductionEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSkuReduction(SkuReductionTo skuReductionTo) {
        // 1. 保存阶梯价格
        SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
        skuLadderEntity.setSkuId(skuReductionTo.getSKuId());
        skuLadderEntity.setFullCount(skuReductionTo.getFullCount());  // 满几件
        skuLadderEntity.setDiscount(skuReductionTo.getDiscount());    // 打几折
        skuLadderEntity.setAddOther(skuReductionTo.getCountStatus()); // 是否参与其他优优惠
        // skuLadderEntity.setPrice();                                // 计算价格
        if (skuReductionTo.getFullCount() > 0) {
            skuLadderService.save(skuLadderEntity);
        }

        // 2. 保存满减信息
        SkuFullReductionEntity skuFullReductionEntity = new SkuFullReductionEntity();
        BeanUtils.copyProperties(skuReductionTo, skuFullReductionEntity);
        if (skuFullReductionEntity.getFullPrice().compareTo(BigDecimal.ZERO) > 0) {
            this.save(skuFullReductionEntity);
        }

        // 3. 会员价格
        List<MemberPrice> memberPrice = skuReductionTo.getMemberPrice();
        List<MemberPriceEntity> memberPriceEntities = memberPrice.stream().map(price -> {
            MemberPriceEntity memberPriceEntity = new MemberPriceEntity();
            memberPriceEntity.setSkuId(skuReductionTo.getSKuId());
            memberPriceEntity.setMemberLevelId(price.getId());
            memberPriceEntity.setMemberLevelName(price.getName());
            memberPriceEntity.setMemberPrice(price.getPrice());
            memberPriceEntity.setAddOther(1);
            return memberPriceEntity;
        }).filter(price -> price.getMemberPrice().compareTo(BigDecimal.ZERO) > 0
        ).collect(Collectors.toList());
        memberPriceService.saveBatch(memberPriceEntities);
    }

}
