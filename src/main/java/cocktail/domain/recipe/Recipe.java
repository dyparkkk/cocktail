package cocktail.domain.recipe;

import cocktail.domain.User;
import cocktail.global.BaseTimeEntity;
import cocktail.infra.recipe.ListToStringConverter;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
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

    private BigDecimal star;
    private int voter;
    private int viewCnt;

    private String glass;
    private Integer soft; // 0-9
    private Integer sweet; // 0-9

    @Enumerated(EnumType.STRING)
    private Official official;

    @OneToMany(mappedBy = "recipe", orphanRemoval = true)
    private List<Tag> tags  = new ArrayList<>();

    @OneToMany(mappedBy = "recipe", orphanRemoval = true)
    private List<Ingredient> ingredients  = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 값타입 컬렉션 사용
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "orders", joinColumns = @JoinColumn(name = "recipe_id"))
    private List<Order> orders = new ArrayList<>();

    private String garnish;

    @Convert(converter = ListToStringConverter.class)
    private List<String> imageUrls;

    @Builder
    public Recipe(String name, BigDecimal dosu, Brewing brewing, Base base, String garnish, String glass, Integer soft, Integer sweet, List<Order> orders, List<String> imageUrls) {
        if(orders != null){
            this.orders.addAll(orders);
            sortOrders();
        }
        if(imageUrls != null){
            this.imageUrls = imageUrls;
        }
        this.name = name;
        this.dosu = dosu;
        this.brewing = brewing;
        this.base = base;
        this.garnish = garnish;
        this.glass = glass;
        this.soft = soft;
        this.sweet = sweet;
        this.star = BigDecimal.ZERO;
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

    public void update(String name, BigDecimal dosu, Brewing brewing, Base base, String garnish, String glass, Integer soft, Integer sweet, List<Order> orders, List<String> imageUrls){
        if(orders != null){
            this.orders.clear();
            this.orders.addAll(orders);
            sortOrders();
        }
        this.imageUrls = imageUrls;
        this.name = name;
        this.dosu = dosu;
        this.brewing = brewing;
        this.base = base;
        this.garnish = garnish;
        this.glass = glass;
        this.soft = soft;
        this.sweet = sweet;
    }

    public void updateStar(BigDecimal rating) {
        if(rating == null) {
            throw new IllegalArgumentException("rating is null");
        }
        star = star.multiply(BigDecimal.valueOf(voter)).add(rating)
                .divide(BigDecimal.valueOf(voter+1))
                .setScale(2, RoundingMode.HALF_UP);
        voter++;
    }

    private void sortOrders() {
        Collections.sort(orders);
    }

}
