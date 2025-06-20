package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;  // 주문 회원

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)   // cascade = CascadeType.ALL : 부모 엔티티의 작업이 자식 엔티티에 자동으로 전파
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;  // 배송 정보

    private LocalDateTime orderDate;    // 주문 시간

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;    // 주문 상대 (ORDER, CANCEL)

    //==연관관계 메서드==//
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }
    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

//    public static void main(String[] args) {
//        Order order = new Order();
//        Member member = new Member();
//
//        order.setMember(member);
//        member.getOrders().add(order);
//    }
}
// N+1
// select o From order o ; => select * from order : 1개의 쿼리로 Order 100개 조회됨.(1query),  Order마다 추가로 Member 조회 쿼리 발생 (100 query)
// 연관된 엔티티 함께 조회 하고 싶을때 fetch join 또는 엔티티 그래프 기능 사용