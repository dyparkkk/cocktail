package cocktail.domain;

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
import java.util.Arrays;
import java.util.List;

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

    private String roles;

    @OneToMany(mappedBy = "user")
    private List<Recipe> post = new ArrayList<Recipe>();

    @Builder
    public User(String username, String pw, String nickname){
        this.username = username;
        this.pw = pw;
        this.nickname = nickname;
        this.roles = "ROLE_USER";
    }

    public List<String> getRoleList() {
        if(roles.length() > 0) {
            return Arrays.asList(roles.split(","));
        }
        return new ArrayList<>();
    }

    /* 회원정보 수정을 위한 set method*/
    public  User update (String username,String nickname) {
        this.username = username;
        this.nickname = nickname;
       return  this;
    }


}
