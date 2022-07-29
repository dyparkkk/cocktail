package cocktail.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "follow")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name ="from_user_id")
    @ManyToOne
    private User fromUser;

    @JoinColumn(name = "to_user_id")
    @ManyToOne
    private User toUser;

    @ManyToOne
    @JoinColumn(name = "user_users_id")
    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    @Builder
    public Follow(User fromUser, User toUser){
        this.fromUser = fromUser;
        this.toUser = toUser;
        user.getFromUser().add(this);
        user.getToUser().add(this);
    }
}
