package cocktail.infra;

import cocktail.domain.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class QuerydslTest {

    @Autowired
    EntityManager em;

    @Test
    void JPQL_세팅확인() {
        User user = saveMember();

        List resultList = em.createQuery("select u from User u where u.username =: username")
                .setParameter("username", "name")
                .getResultList();

        assertThat(resultList).containsExactly(user);
    }

    @Test
    void querydsl_세팅확인() {
        User user = saveMember();

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QUser qUser = new QUser("u");

        List<User> result = queryFactory
                .selectFrom(qUser)
                .where(qUser.username.eq("name"))
                .fetch();

        assertThat(result).containsExactly(user);
    }

    private User saveMember() {
        User user = User.builder()
                        .username("name")
                        .pw("pw")
                        .build();
        em.persist(user);
        return user;
    }
}
