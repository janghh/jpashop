package jpabook.jpashop.repository.order.simplequery;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {

    private final EntityManager em;

    public List<OrderSimpleQueryDto> findOrderDtos(){
        return em.createQuery(
                "select new jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto(o.id, o.member.name, o.orderDate, o.status, o.delivery.address) " +
                        " from Order o " +
                        " join o.member m " +
                        " join o.delivery d"
                , OrderSimpleQueryDto.class
        ).getResultList();
    }
}
