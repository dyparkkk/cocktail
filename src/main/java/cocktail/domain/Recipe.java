package cocktail.domain;

import cocktail.global.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
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

    @Enumerated(EnumType.STRING)
    private Brewing brewing;

    @Enumerated(EnumType.STRING)
    private Base base;

    @OneToMany(mappedBy = "recipe")
    private List<Tag> tags  = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "users_id")
    private User user;

    // 값타입 컬렉션 사용
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "orders", joinColumns = @JoinColumn(name = "recipe_id"))
    private List<Order> orders = new ArrayList<>();

    @Builder
    public Recipe(String name, BigDecimal dosu, Brewing brewing, Base base, List<Tag> tags, List<Order> orders) {
        this.name = name;
        this.dosu = dosu;
        this.brewing = brewing;
        this.base = base;
        if(orders != null){
            this.orders.addAll(orders);
            sortOrders();
        }
    }

    private void sortOrders() {
        Collections.sort(orders);
    }


}
