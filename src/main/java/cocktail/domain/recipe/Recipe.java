package cocktail.domain.recipe;

import cocktail.domain.User;
import cocktail.global.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Recipe extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private BigDecimal dosu;

    @Enumerated(EnumType.STRING)
    private Brewing brewing;

    @Enumerated(EnumType.STRING)
    private Base base;

    private String star;
    private int voter;
    private int viewCnt;

    private String glass;
    private Integer soft; // 0-9
    private Integer sweet; // 0-9

    @Enumerated(EnumType.STRING)
    private Official official;

    @OneToMany(mappedBy = "recipe")
    private List<Tag> tags  = new ArrayList<>();

    @OneToMany(mappedBy = "recipe")
    private List<Ingredient> ingredients  = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 값타입 컬렉션 사용
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "orders", joinColumns = @JoinColumn(name = "recipe_id"))
    private List<Order> orders = new ArrayList<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "garnishes", joinColumns = @JoinColumn(name = "recipe_id"))
    private Set<String> garnishes = new HashSet<>();

    @Builder
    public Recipe(String name, BigDecimal dosu, Brewing brewing, Base base, Set<String> garnishes, String glass, Integer soft, Integer sweet, List<Order> orders) {
        this.name = name;
        this.dosu = dosu;
        this.brewing = brewing;
        this.base = base;
        if(orders != null){
            this.orders.addAll(orders);
            sortOrders();
        }
        if (garnishes != null) {
            this.garnishes.addAll(garnishes);
        }
        this.glass = glass;
        this.soft = soft;
        this.sweet = sweet;
        this.star = "0.00";
        this.voter = 0;
        this.viewCnt = 0;
        this.official = Official.NONE;
    }

    public void setUser(User user) {
        this.user = user;
        user.getRecipe().add(this);
    }

    public void setOfficial(Official official) {
        this.official = official;
    }

    public void update(String name, BigDecimal dosu, Brewing brewing, Base base, Set<String> garnishes, String glass, Integer soft, Integer sweet, List<Order> orders){
        this.name = name;
        this.dosu = dosu;
        this.brewing = brewing;
        this.base = base;

        this.orders.clear();
        if(orders != null){
            this.orders.addAll(orders);
            sortOrders();
        }
        this.garnishes.clear();
        if (garnishes != null) {
            this.garnishes.addAll(garnishes);
        }

        this.glass = glass;
        this.soft = soft;
        this.sweet = sweet;
    }

    private void sortOrders() {
        Collections.sort(orders);
    }


}
