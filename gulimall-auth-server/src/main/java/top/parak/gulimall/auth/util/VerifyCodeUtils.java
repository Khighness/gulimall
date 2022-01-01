package top.parak.gulimall.auth.util;

import java.util.Random;

/**
 * @author KHighness
 * @since 2021-12-28
 * @email parakovo@gmail.com
 * @apiNote 验证码工具类
 */
public class VerifyCodeUtils {

    private static final String UPPER_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final String LOWER_LETTERS = "abcdefghijklmnopqrstuvwxyz";

    private static final String All_NUMBERS = "0123456789";

    /**
     * 生成默认类型的验证码
     *
     * @return 6位数字型的字符串验证码
     */
    public static String generateCode() {
        Random random = new Random(System.currentTimeMillis());
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int x = random.nextInt(10);
            builder.append(All_NUMBERS.charAt(x));
        }
        return builder.toString();
    }

    /**
     * 生成指定位数和指定类型的验证码
     *
     * @param n 验证码位数
     * @param VerifyCodeType 验证码类型
     * @return 指定位数和指定类型的字符串验证码
     */
    public static String generateCode(int n, VerifyCodeType VerifyCodeType) {
        Random random = new Random(System.currentTimeMillis());
        StringBuilder builder = new StringBuilder();
        if (VerifyCodeType == VerifyCodeUtils.VerifyCodeType.DIGIT) {
            for (int i = 0; i < n; i++) {
                int x = random.nextInt(10);
                builder.append(All_NUMBERS.charAt(x));
            }
        } else if (VerifyCodeType == VerifyCodeUtils.VerifyCodeType.LOWER) {
            for (int i = 0; i < n; i++) {
                int x = random.nextInt(26);
                builder.append(LOWER_LETTERS.charAt(x));
            }
        } else if (VerifyCodeType == VerifyCodeUtils.VerifyCodeType.UPPER) {
            for (int i = 0; i < n; i++) {
                int x = random.nextInt(26);
                builder.append(UPPER_LETTERS.charAt(x));
            }
        } else {
            String s = LOWER_LETTERS + UPPER_LETTERS + All_NUMBERS;
            for (int i = 0; i < n; i++) {
                int x = random.nextInt(62);
                builder.append(s.charAt(x));
            }
        }
        return builder.toString();
    }

    public enum VerifyCodeType {
        /* 数字型 */
        DIGIT,
        /* 小写子母型 */
        LOWER,
        /* 大写字母型 */
        UPPER,
        /* 混合型 */
        HYBRID
    }

}
