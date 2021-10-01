package project.kiosk.kiosk.serviceImpl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.transaction.annotation.Transactional;
import project.kiosk.kiosk.dto.ItemAddDTO;
import project.kiosk.kiosk.dto.ItemUpdateDTO;
import project.kiosk.kiosk.dto.MemberJoinDTO;
import project.kiosk.kiosk.dto.responseDto.ItemResponseDto;
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
        Long memberNo = memberService.joinInit(member1);
        System.out.println("ID : " + memberNo);

        ItemAddDTO itemAddDTO1 = new ItemAddDTO("itemA", 10000, "false", memberNo);
        itemService.addItem(itemAddDTO1, memberNo);

        ItemAddDTO itemAddDTO2 = new ItemAddDTO("itemB", 20000, "false", memberNo);
        itemService.addItem(itemAddDTO2, memberNo);

        ItemAddDTO itemAddDTO3 = new ItemAddDTO("itemC", 30000, "false", memberNo);
        itemService.addItem(itemAddDTO3, memberNo);
    }

    @Test
    void 판매자별_상품조회() {
        // given
        Member findMember = memberService.findMemberById("member1");

        // when
        List<Item> items = itemService.findItems(findMember.getNo());

        // then
        assertThat(items.size()).isEqualTo(3);
        assertThat(items.get(0).getItemName()).isEqualTo("itemA");
        assertThat(items.get(0).getPrice()).isEqualTo(10000);
        assertThat(items.get(1).getItemName()).isEqualTo("itemB");
        assertThat(items.get(1).getPrice()).isEqualTo(20000);
    }

    @Test
    void 상품_수정() {
        MockMultipartHttpServletRequest multipartfile = new MockMultipartHttpServletRequest();

        // given
        Item findItem = itemService.findItemByItemName("itemA");
        Long itemNo = findItem.getNo();

        ItemUpdateDTO update = new ItemUpdateDTO(50000, "true");

        // when
//        Long no = itemService.editItem(itemNo, update);

        // then
        assertThat(findItem.getPrice()).isEqualTo(50000);
        assertThat(findItem.isSoldOut()).isTrue();

    }

    @Test
    void 상품삭제() {
        // given
        Item findItem = itemService.findItemByItemName("itemA");

        // when
        itemService.deleteItem(findItem.getNo());

        // then
        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> itemService.findItem(findItem.getNo()));

    }


}
