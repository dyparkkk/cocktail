package cocktail.domain.user;


import cocktail.domain.recipe.Recipe;
import cocktail.global.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
public class User extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "users_id")
    private Long id;

    private String username; // userId

    private String pw;

    @Column(name = "user_nickname")
    private String nickname;

    @Enumerated(EnumType.STRING)
    private Role roles;

    private String title;

    private String profileImgUrl;

    private int myRecipeCnt;

    private String provider;    // oauth2를 이용할 경우 어떤 플랫폼을 이용하는지
    private String providerId;  // oauth2를 이용할 경우 아이디값

    @OneToMany(mappedBy = "user")
    private List<Recipe> recipes = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private Set<Bookmark> bookmarks = new HashSet<>();

    @OneToMany(mappedBy = "fromUser")
    private List<Follow> followings = new ArrayList<>();

    @OneToMany(mappedBy = "toUser")
    private List<Follow> followers = new ArrayList<>();

    @Builder
    public User(String username, String pw, String nickname,String title, String profileImgUrl,Role roles) {
        this.username = username;
        this.pw = pw;
        this.nickname = nickname;
        this.title = title;
        this.profileImgUrl = profileImgUrl;
        this.roles = roles;
        myRecipeCnt = 0;
    }

    /* 회원정보 수정을 위한 set method*/
    public  User update (String username,String nickname) {
        this.username = username;
        this.nickname = nickname;
       return  this;
    }

    public void update(String pw, String nickname, String title, String profileImgUrl){
        this.pw = pw;
        this.nickname =nickname;
        this.title = title;
        this.profileImgUrl = profileImgUrl;
    }

    public String getRolekey(){
        return this.roles.getKey();
    }

    public void addMyRecipeCnt() {
        myRecipeCnt++;
    }

    public int getFollowingNum() {
        return followings.size();
    }
    public int getFollowerNum() {
        return followers.size();
    }
    public int getMyRecipeNum() {
        return recipes.size();
    }

}
