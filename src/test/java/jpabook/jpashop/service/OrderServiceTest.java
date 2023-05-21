package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Album;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private EntityManager em;

    @Test
    public void 상품주문() throws Exception {
        // given
        Member member = createMember();

        Item item = createItem();

        // when
        Long orderId = orderService.order(member.getId(), item.getId(), 2);

        // then
        Order getOrder = orderRepository.findOne(orderId);
        Item getItem = itemRepository.findOne(item.getId());

        assertEquals( "상품 주문시 상태는 Order", OrderStatus.ORDER, getOrder.getStatus());
        assertEquals( "주문한 상품 종류 수가 정확해야 한다", 1, getOrder.getOrderItems().size());
        assertEquals( "주문 가격은 가격 * 수량이다", (35000*2), getOrder.getTotalPrice() );
        assertEquals( "아이템 재고 수량 확인", (1000-2), getItem.getStockQuantity() );

    }

    @Test
    public void 주문취소() throws Exception {
        // given
        Member member = createMember();
        Item item = createItem();

        Long orderId1 = orderService.order(member.getId(), item.getId(), 50);
        Long orderId2 = orderService.order(member.getId(), item.getId(), 30);

        // when
        orderService.cancel(orderId1);

        // then
        Order getOrder = orderRepository.findOne(orderId1);
        Item getItem = itemRepository.findOne(item.getId());

        assertEquals("주문 취소 상태 체크", OrderStatus.CANCEL, getOrder.getStatus());
        assertEquals("주문 취소시 아이템 수량 원복 확인", (1000-30), getItem.getStockQuantity());
    }

    @Test(expected = NotEnoughStockException.class)
    public void 상품주문_재고수량초과() throws Exception {
        // given
        Member member = createMember();
        Item item = createItem();

        // when
        Long orderId1 = orderService.order(member.getId(), item.getId(), 400);
        Long orderId2 = orderService.order(member.getId(), item.getId(), 600);

        orderService.cancel(orderId2);

        Long orderId3 = orderService.order(member.getId(), item.getId(), 501);
        Long orderId4 = orderService.order(member.getId(), item.getId(), 100);

        // then
        fail("재고 수량 예외가 발생해야 한다");

    }

    private Item createItem() {
        Item item = new Album("이무진", "신호등", 35000, 1000);
        itemService.saveItem(item);
        return item;
    }

    private Member createMember() {
        Address address = new Address("부산", "강서구 지사동", "123-4567");
        Member member = new Member("장호형", address);
        memberService.join(member);
        return member;
    }

}