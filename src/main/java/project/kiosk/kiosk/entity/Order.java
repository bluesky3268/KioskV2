package project.kiosk.kiosk.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "orders")
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orders_no")
    private Long no;

    @OneToOne
    private Item item;

    private int price;

    private int quantity;

    private int totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no")
    private Member member;

    private LocalDateTime regDate;

    public Order(Item item, int price, int quantity, int totalPrice, Member member, LocalDateTime regDate) {
        this.item = item;
        this.price = price;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.member = member;
        this.regDate = regDate;
    }

    @Override
    public String toString() {
        return "Order{" +
                "no=" + no +
                ", item=" + item +
                ", price=" + price +
                ", quantity=" + quantity +
                ", totalPrice=" + totalPrice +
                ", member=" + member +
                ", regDate=" + regDate +
                '}';
    }
}
