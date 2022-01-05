package top.parak.gulimall.cart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import top.parak.gulimall.cart.service.CartService;
import top.parak.gulimall.cart.vo.Cart;
import top.parak.gulimall.cart.vo.CartItem;
import top.parak.gulimall.common.cosntant.GulimallPageConstant;

import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;

/**
 * @author KHighness
 * @since 2022-01-02
 * @email parakovo@gmail.com
 */
@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * 购物车页面
     */
    @GetMapping({"/", "cart.html"})
    public String cartListPage(Model model) throws ExecutionException, InterruptedException {
        Cart cart = cartService.getCart();
        model.addAttribute("cart", cart);

        return "cartList";
    }

    /**
     * 添加商品到购物车
     *
     * <br> 对于{@code RedirectAttributes}
     * <br> {@link RedirectAttributes#addAttribute(String, Object)}
     * 将数据放在session里面可以在页面取出，但是只能取一次。
     * <br> {@link RedirectAttributes#addFlashAttribute(String, Object)}
     * 将数据放在url后面。
     * @see org.springframework.web.servlet.mvc.support.RedirectAttributes
     */
    @GetMapping("/addToCart")
    public String addToCart(@RequestParam("skuId") Long skuId, @RequestParam("num") Integer num,
                            RedirectAttributes attributes) throws ExecutionException, InterruptedException {
        CartItem cartItem = cartService.addToCart(skuId, num);
        attributes.addAttribute("skuId", skuId);

        return "redirect:http://cart.gulimall.com/addToCartSuccess.html";
    }

    /**
     * 添加购物车成功页面
     */
    @GetMapping("/addToCartSuccess.html")
    public String addToCartSuccessPage(@RequestParam("skuId") Long skuId, Model model) {
        CartItem cartItem = cartService.getCartItem(skuId);
        model.addAttribute("item", cartItem);

        return "success";
    }

    /**
     * 勾选购物项
     */
    @GetMapping("/checkItem.html")
    public String checkItem(@RequestParam("skuId") Long skuId, @RequestParam("check") Integer check) {
        cartService.checkItem(skuId, check);

        return GulimallPageConstant.REDIRECT_CART;
    }

    /**
     * 修改购物数量
     */
    @GetMapping("/countItem")
    public String countItem(@RequestParam("skuId") Long skuId, @RequestParam("num") Integer num) {
        cartService.changeItemCount(skuId, num);

        return GulimallPageConstant.REDIRECT_CART;
    }

    /**
     * 删除购物项
     */
    @GetMapping("/deleteItem")
    public String deleteItem(@RequestParam("skuId") Long skuId) {
        cartService.deleteItem(skuId);

        return GulimallPageConstant.REDIRECT_CART;
    }

    /**
     * 结账
     */
    @ResponseBody
    @GetMapping("toTrade")
    public String toTrade() throws ExecutionException, InterruptedException {
        BigDecimal amount = cartService.toTrade();

        return "结算成功，共：￥" + amount.toString();
    }

}

