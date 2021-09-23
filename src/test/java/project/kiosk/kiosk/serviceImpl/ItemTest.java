package project.kiosk.kiosk.serviceImpl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.kiosk.kiosk.dto.ItemAddDTO;
import project.kiosk.kiosk.dto.ItemUpdateDTO;
import project.kiosk.kiosk.dto.MemberJoinDTO;
import project.kiosk.kiosk.entity.Item;
import project.kiosk.kiosk.entity.Member;
import project.kiosk.kiosk.entity.Role;
import project.kiosk.kiosk.service.ItemService;
import project.kiosk.kiosk.service.MemberService;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
public class ItemTest {

    @Autowired
    private ItemService itemService;
    @Autowired
    private MemberService memberService;

    @BeforeEach
    void 상품등록() {
        // 멤버 등록
        MemberJoinDTO member1 = new MemberJoinDTO("member1", "1234", "1234", "manager");
        Member joinMember = memberService.joinInit(member1);
        System.out.println("joinMember.getMemberId() = " + joinMember.getId());

        ItemAddDTO itemAddDTO1 = new ItemAddDTO("itemA", 10000, false, joinMember.getNo());
        itemService.addItem(itemAddDTO1, joinMember.getId());

        ItemAddDTO itemAddDTO2 = new ItemAddDTO("itemB", 20000, false, joinMember.getNo());
        itemService.addItem(itemAddDTO2, joinMember.getId());

        ItemAddDTO itemAddDTO3 = new ItemAddDTO("itemC", 30000, false, joinMember.getNo());
        itemService.addItem(itemAddDTO3, joinMember.getId());
    }

    @Test
    void 판매자별_상품조회() {

        Member findMember = memberService.findMemberById("member1");

        System.out.println("findMember.getNo() = " + findMember.getNo());

        List<Item> items = itemService.findItems(findMember.getNo());

        assertThat(items.size()).isEqualTo(3);
        assertThat(items.get(0).getItemName()).isEqualTo("itemA");
        assertThat(items.get(0).getPrice()).isEqualTo(10000);
        assertThat(items.get(1).getItemName()).isEqualTo("itemB");
        assertThat(items.get(1).getPrice()).isEqualTo(20000);
    }

    @Test
    void 상품_수정() {

        Item itemA = itemService.findItemByItemName("itemA");

        ItemUpdateDTO update = new ItemUpdateDTO(50000, true);

        itemService.editItem(itemA.getNo(), update);

        Item findItem = itemService.findItemByItemName("itemA");

        assertThat(findItem.getPrice()).isEqualTo(50000);
        assertThat(findItem.isSoldOut()).isTrue();

    }

    @Test
    void 상품삭제() {
        Item itemA = itemService.findItemByItemName("itemA");

        itemService.deleteItem(itemA.getNo());

        Item findItem = itemService.findItem(itemA.getNo());

        assertThat(findItem).isNull();
    }


}
