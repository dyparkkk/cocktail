package cocktail.infra;

import cocktail.domain.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class QuerydslTest {

    @Autowired
    EntityManager em;

    @Test
    void querydsl_세팅확인() {
        User user = new User("name", "pw");
        em.persist(user);

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QUser Quser = new QUser("u");

        List<User> result = queryFactory
                .selectFrom(Quser)
                .fetch();

        assertThat(result).containsExactly(user);
    }
}
