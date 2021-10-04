package project.kiosk.kiosk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_no")
    private Long no;

    private String itemName;

    private Integer price;

    @OneToOne(fetch = FetchType.EAGER)
    private UploadFile img;

    private boolean isSoldOut;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no")
    private Member member;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "orders_no")
//    private Order order;

    // 테스트용 생성자
    public Item(String itemName, Integer price, boolean isSoldOut, Member member) {
        this.itemName = itemName;
        this.price = price;
        this.isSoldOut = isSoldOut;
        this.member = member;
    }

    public Item(String itemName, Integer price, UploadFile img, boolean isSoldOut, Member member) {
        this.itemName = itemName;
        this.price = price;
        this.img = img;
        this.isSoldOut = isSoldOut;
        this.member = member;
    }

    public void updatePrice(Integer price) {
        this.price = price;
    }
}
