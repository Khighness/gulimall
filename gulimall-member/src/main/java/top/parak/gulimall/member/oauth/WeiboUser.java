package top.parak.gulimall.member.oauth;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author KHighness
 * @since 2021-12-30
 * @email parakovo@gmail.com
 * @apiNote 微博信息建模
 */
@Data
@NoArgsConstructor
public class WeiboUser {

    /**
     * UID
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String screenName;

    /**
     * 友好显示昵称
     */
    private String name;

    /**
     * 所在省级ID
     */
    private Integer province;

    /**
     * 所在城市ID
     */
    private Integer city;

    /**
     * 所在地
     */
    private String location;

    /**
     * 个人描述
     */
    private String description;

    /**
     * 博客地址
     */
    private String url;

    /**
     * 头像地址
     */
    private String profileImageUrl;

    /**
     * 性别
     */
    private String gender;

}
