package top.parak.gulimall.order.pay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 支付宝支付
 *
 * @author KHighness
 * @since 2022-01-19
 * @email parakovo@gmail.com
 */
@ConfigurationProperties(prefix = "gulimall.alipay")
@Component
@Data
public class AlipayTemplate {

    /**
     * appId
     */
    private String appId = "2021000119601155";

    /**
     * 商户私钥
     * <p>PKCS8格式 RSA2私钥
     */
    private String merchantPrivateKey = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDE77J0KuahbQFubULhwSLgqVLvFB1kLvBOGIVOGjvd0vUXyv0WbES+TYb+5tIbzz1mBNKj+CCkw8cr+HxtSFVlgXeFmEl7dgMim4OsmSpbWYawzMTbxXTfBo37xDv7PAiTlLbOwqU593cvwQmwtqUdYNPpxhyIS+y//iJx/WyHISGEhHoKt1NUrA63dzHFuxG7ZR478SKX6a1pNZ0oxLbGgPpgvabJxzrNEsQZUMWL/AHlXjo34pynyScFJ3N8QE8Rj4c8F1+CWSVApkjy0V0CrR/Jm/XkPsj0NZxCJghmwjFuVZt4ZEE8/aSQm3JtJPJzk4KGqcHIYYmddE+vqgNlAgMBAAECggEAVT9xHyom9X+rG/L9/Z1ODLQfK1CovGI5MYUZx0pB2e7km20KCzfIthv3MMYX82PNLyktST8yWkBJKjaBHgkutibJ5zGZtXDLQKLS59bRaAOj80pgeMXLpnM+6Nn0IPfGuqOoUm4dea8uj6RfHnihlssAUHSqizFahW1r2a4a2Y7x5Yc53R6oV9leE79JkkBQ90uwThFGmX2UKfARzp0v7Or6v8fOf2Ik/d8suGHJqrKJ7iDZ7uxfZ8L/HoDWyptSXFfVHbaHT6Z/9KOVrX01+7V0sXl+DdTLwoRZUA1ahUMuRJ3CGTQ3kD+AzddBLaHpYy1G8M+no8vnDY0mzORPAQKBgQDy8nJxCIuYDVPAJzCuF23QiRNlpjVAivTeInv6xwgDygbe0Un2cOWFFovemZvXUac9CQEND1PUSFDs716WNh8rVgcaAkjG96+EPdgP9XOdD4t4pvC7fbmf+Fdd9UIvJ5Q2Ata5TDJXZGmASzapav/EPLR98u8TudFLxONI/JGCpQKBgQDPhGg4INtFHV5DawDYwz9R27Xq2bS644s5RpjA3hP0q6aEdzxVYlwrlV8ax7jh82/vNEACr080xGhhxuE1UQLkQt9E1GrTuBBQ2+9Bokwz+J+BjSZNAUFCOJI33ycpeRkRoTAUfY1Ku1cs7lI7V7fo1miIVbF9TjagUe/D6b5hwQKBgQCjFjHQwyXCSqAAC87X7w5ewMXupKZ2z1EVlRyY+ebddE8sYF5AOwU4VCxMn91Hq56SkGrOM43YoTsB8qYSO/6sfrGBuirUtgxyv3k9LU4RDxBXS+mqUvm682Dix/NH6E/L+hfeh9H7bIKOBu+IxzUurHmVWvE20rQ3ItJYeoPLqQKBgQDF2aPT/9+PIdUByj9ga6cvUJ89bpvVo6TV0u1I1XOJTy7QFS686a6fDydBzTIEm6kZE2QEK0kFMk2jC48k2jw7jVZ5tVy0x0UnEsomdarZ0i2hwwXFXXDuxikChAw+imrQWDITFzFjEwKmoAJe+d8qa2H2ul0i7zM932EOFQmjAQKBgQCaRKq+bRlE0nYnpwGBn2xGl9KZZf/mY6Yid9Wwe+f8v2MhHCD7WEw8FqNU0qQmcPTkWgd+CvEdjK/WabnrNjraMoSIzkmgUsEsfH7lnMiVcnlDd21a22P6EeMdjUhoYB5Lij5GmzlHcCIgyYp8G0B7y/33YFLL9pChFOIw4xAiOA==";

    /**
     * 支付宝公钥
     * @see <a href="https://openhome.alipay.com/platform/keyManage.htm">查看对应APPID下的支付宝公钥</a>
     */
    private String alipayPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAss4+//xjrAdzaY+0ugTpaAqnnFv1JP4TwiaN1TUOCv438P6BvjiY45Vyk+UZG/Oa+rYiKSiLzLihF3f3/HkRAuoNUv55NrtmaEtiezWKioCTc9Om70tcmzekRIHVnv/hZ+XuR2qiyYQai8yO5/QVG9w6fJ9yFX8PSpsGj13z3fK0DoRURUFW/iHULPAnXVRs5RJEXpruGtkDvxikJvmaWp7yOK/nY6Cx4YmZquSoFocnbpnwtuZMpEd9wAFFPkTozV9sJO1ptJYwMCCQKriNw0hNrQkmSdcwl8lA5fkCeApxMBcXz7V7E4lXm6bGJgqwnt36LSkzpXFxo4x34iUZwQIDAQAB";

    /**
     * 异步通知URL
     * <p>需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
     * <p>支付宝会悄悄的给我们发送一个请求，告诉我们支付成功的信息
     */
    private String notifyUrl = "http://member.gulimall.com/memberOrder.html";

    /**
     * 同步通知URL
     * <p>需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
     * <p>支付成功，直接跳转
     */
    private String returnUrl = "http://member.gulimall.com/memberOrder.html";

    /**
     * 签名方式
     */
    private String signType = "RSA2";

    /**
     * 字符编码格式
     */
    private String charset = "utf-8";

    /**
     * 自动关单时间
     */
    private String timeout = "15m";

    /**
     * 支付宝网关
     * @see <a href="https://openapi.alipaydev.com/gateway.do">支付宝网关</a>
     */
    private  String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    public  String pay(PayVo payVo) throws AlipayApiException {
        // 1、根据支付宝的配置生成一个支付客户端
        AlipayClient alipayClient = new DefaultAlipayClient(
                gatewayUrl, appId, merchantPrivateKey, "json", charset, alipayPublicKey, signType
        );

        // 2、创建一个支付请求 //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(returnUrl);
        alipayRequest.setNotifyUrl(notifyUrl);

        // 商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = payVo.getOut_trade_no();
        // 付款金额，必填
        String total_amount = payVo.getTotal_amount();
        // 订单名称，必填
        String subject = payVo.getSubject();
        // 商品描述，可空
        String body = payVo.getBody();

        // 30分钟内不付款就会自动关单
        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                + "\"total_amount\":\""+ total_amount +"\","
                + "\"subject\":\""+ subject +"\","
                + "\"body\":\""+ body +"\","
                + "\"timeout_express\":\"" + timeout + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        String result = alipayClient.pageExecute(alipayRequest).getBody();

        // 会收到支付宝的响应，响应的是一个页面，只要浏览器显示这个页面，就会自动来到支付宝的收银台页面
        return result;
    }

}
