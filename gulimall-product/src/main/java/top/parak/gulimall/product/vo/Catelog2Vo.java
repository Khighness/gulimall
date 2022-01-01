package top.parak.gulimall.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author KHighness
 * @since 2021-12-10
 * @email parakovo@gmail.com
 * @apiNote 二级分类VO
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Catelog2Vo {
    /**
     * 一级父分类ID
     */
    private String catalog1Id;

    /**
     * 三级分类VO列表
     */
    private List<Catelog3Vo> catalog3List;

    /**
     * 二级分类ID
     */
    private String id;

    /**
     * 二级分类名称
     */
    private String name;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Catelog3Vo {
        /**
         * 父分类，二级分类id
         */
        private String catalog2Id;

        private String id;

        private String name;

    }
}
