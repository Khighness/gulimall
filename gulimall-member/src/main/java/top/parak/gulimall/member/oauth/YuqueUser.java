package top.parak.gulimall.member.oauth;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author KHighness
 * @since 2021-12-30
 * @email parakovo@gmail.com
 * @apiNote 语雀用户建模
 * <pre>
 * curl -H "X-Auth-Token: ${token}" https://www.yuque.com/api/v2/user
 * <br>
 * {
 * 	"data": {
 * 		"id": 493248,
 * 		"type": "User",
 * 		"space_id": 0,
 * 		"account_id": 318261,
 * 		"login": "khighness",
 * 		"name": "Khighness",
 * 		"avatar_url": "https://cdn.nlark.com/yuque/0/2021/png/493248/1640598353939-avatar/ed4a1e9a-f36c-47f1-97fe-e7f3bf21722d.png",
 * 		"books_count": 14,
 * 		"public_books_count": 13,
 * 		"followers_count": 3,
 * 		"following_count": 0,
 * 		"public": 1,
 * 		"description": "万古长空，一朝风月",
 * 		"created_at": "2019-09-17T14:14:34.000Z",
 * 		"updated_at": "2021-12-28T08:20:46.000Z",
 * 		"_serializer": "v2.user_detail"
 *        }
 * }
 * </pre>
 */
@Data
@NoArgsConstructor
public class YuqueUser {

    /**
     * ID
     */
    private Long id;

    /**
     * 账号ID
     */
    private String accountId;

    /**
     * 用户名
     */
    private String login;

    /**
     * 姓名
     */
    private String name;

    /**
     * 头像名称
     */
    private String avatarUrl;

    /**
     * 简介
     */
    private String description;

}
