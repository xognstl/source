package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    @Embedded   // 값 타입(Value Type)을 엔티티에 내장할때 사용
    private Address address;

    @Enumerated(EnumType.STRING)    // enum 타입을 DB에 매핑할 때 사용, EnumType.ORDINAL 숫자 사용 XX
    private DeliveryStatus status; //ENUM [READY(준비), COMP(배송)]
}
