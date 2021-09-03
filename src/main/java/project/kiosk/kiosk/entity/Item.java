package project.kiosk.kiosk.entity;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@NoArgsConstructor
public class Item {

    @Id
    private Long id;
    private String itemName;
    private Integer price;
    @OneToOne
    private UploadFile img;
    private boolean isSoldOut;
    private Integer memberId;

    public Item(String itemName, Integer price, UploadFile img, boolean isSoldOut, Integer memberId) {
        this.itemName = itemName;
        this.price = price;
        this.img = img;
        this.isSoldOut = isSoldOut;
        this.memberId = memberId;
    }
}
