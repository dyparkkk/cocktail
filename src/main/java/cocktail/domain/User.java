package cocktail.domain;


import cocktail.domain.recipe.Recipe;
import cocktail.global.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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

    private Role roles;

    private String provider;    // oauth2를 이용할 경우 어떤 플랫폼을 이용하는지
    private String providerId;  // oauth2를 이용할 경우 아이디값

//    @OneToMany(mappedBy = "user")
//    private List<Recipe> recipe = new ArrayList<Recipe>();

    @OneToMany(mappedBy = "user")
    private Set<Bookmark> bookmarks = new HashSet<>();

    @Builder
    public User(String username, String pw, String nickname,Role roles) {
        this.username = username;
        this.pw = pw;
        this.nickname = nickname;
        this.roles = roles;
    }

    /* 회원정보 수정을 위한 set method*/
    public  User update (String username,String nickname) {
        this.username = username;
        this.nickname = nickname;
       return  this;
    }

    public String getRolekey(){
        return this.roles.getKey();
    }

}
