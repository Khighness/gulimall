package top.parak.gulimall.member.oauth;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author KHighness
 * @since 2021-12-30
 * @email parakovo@gmail.com
 * @apiNote Github用户建模
 * <pre>
 * curl -H "Authorization: token ${access_token}" https://api.github.com/user
 * <br>
 * response:
 * {
 *   "login": "Khighness",
 *   "id": 52003252,
 *   "node_id": "MDQ6VXNlcjUyMDAzMjUy",
 *   "avatar_url": "https://avatars.githubusercontent.com/u/52003252?v=4",
 *   "gravatar_id": "",
 *   "url": "https://api.github.com/users/Khighness",
 *   "html_url": "https://github.com/Khighness",
 *   "followers_url": "https://api.github.com/users/Khighness/followers",
 *   "following_url": "https://api.github.com/users/Khighness/following{/other_user}",
 *   "gists_url": "https://api.github.com/users/Khighness/gists{/gist_id}",
 *   "starred_url": "https://api.github.com/users/Khighness/starred{/owner}{/repo}",
 *   "subscriptions_url": "https://api.github.com/users/Khighness/subscriptions",
 *   "organizations_url": "https://api.github.com/users/Khighness/orgs",
 *   "repos_url": "https://api.github.com/users/Khighness/repos",
 *   "events_url": "https://api.github.com/users/Khighness/events{/privacy}",
 *   "received_events_url": "https://api.github.com/users/Khighness/received_events",
 *   "type": "User",
 *   "site_admin": false,
 *   "name": "Chen Zikang",
 *   "company": "Shopee",
 *   "blog": "https://www.parak.top",
 *   "location": "Hefei",
 *   "email": "parakovo@gmail.com",
 *   "hireable": null,
 *   "bio": "Mid Two.",
 *   "twitter_username": null,
 *   "public_repos": 26,
 *   "public_gists": 0,
 *   "followers": 5,
 *   "following": 0,
 *   "created_at": "2019-06-19T14:28:04Z",
 *   "updated_at": "2021-12-27T09:56:20Z"
 * }
 * </pre>
 */
@Data
@NoArgsConstructor
public class GithubUser {

    /**
     * 用户名
     */
    private String login;

    /**
     * ID
     */
    private Long id;

    /**
     * NodeID
     */
    private String nodeId;

    /**
     * 头像地址
     */
    private String avatarUrl;

    /**
     * 姓名
     */
    private String name;

    /**
     * 公司
     */
    private String company;

    /**
     * 博客
     */
    private String blog;

    /**
     * 所在地
     */
    private String location;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 生物
     */
    private String bio;

}
