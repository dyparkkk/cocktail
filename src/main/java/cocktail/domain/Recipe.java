package cocktail.domain;

import cocktail.global.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Recipe extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private BigDecimal dosu;

    @OneToMany(mappedBy = "Tag")
    private List<Tag> tags  = new ArrayList<>();

    // 값타입 컬렉션 사용
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "orders", joinColumns = @JoinColumn(name = "recipe_id"))
    private List<Order> orders = new ArrayList<>();

    public Recipe(String name, BigDecimal dosu, List<Order> orders) {
        this.name = name;
        this.dosu = dosu;
        this.orders.addAll(orders);
        sortOrders();
    }

    private void sortOrders() {
        Collections.sort(orders);
    }

    @ManyToOne
    @JoinColumn(name = "users_id")
    private User user;
}
