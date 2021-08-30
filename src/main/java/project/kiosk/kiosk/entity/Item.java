package project.kiosk.kiosk.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Item {

    @Id
    private Long id;
    private String itmeName;
    private Integer price;
    private String img;
    private boolean isSoldOut;
    private Integer memberId;
}
