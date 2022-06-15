package cocktail.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor
public class Order implements Comparable<Order> {
    private int num;
    private String content;

    public Order(int num, String content) {
        this.num = num;
        this.content = content;
    }

    @Override
    public int compareTo(Order o) {
        if (this.num <= o.num ){
            return -1;
        }
        else return 1;
    }
}
