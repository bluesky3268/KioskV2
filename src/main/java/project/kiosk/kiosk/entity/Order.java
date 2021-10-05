package project.kiosk.kiosk.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orders_no")
    private Long no;

    @OneToOne()
    private Item item;

    private int price;

    private int quantity;

    private int totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no")
    private Member member;



    public Order(Item item, int price, int quantity, Member member) {
        this.item = item;
        this.price = price;
        this.quantity = quantity;
        this.member = member;
        this.totalPrice = price * quantity;
    }
}
