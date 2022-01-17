package top.parak.gulimall.common.exception;

/**
 * @author KHighness
 * @since 2022-01-14
 * @email parakovo@gmail.com
 */
public class NoStockException extends RuntimeException {

    private Long skuId;

    public NoStockException(Long skuId) {
        super("商品[skuId=" + skuId + "]库存不足");
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }
}
