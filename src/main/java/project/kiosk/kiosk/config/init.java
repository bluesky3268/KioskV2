package project.kiosk.kiosk.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import project.kiosk.kiosk.dto.ItemAddDTO;
import project.kiosk.kiosk.dto.MemberJoinDTO;
import project.kiosk.kiosk.dto.responseDto.MemberListResponseDto;
import project.kiosk.kiosk.entity.Member;
import project.kiosk.kiosk.entity.Role;
import project.kiosk.kiosk.service.ItemService;
import project.kiosk.kiosk.service.MemberService;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class init {

    private final MemberService memberService;
    private final ItemService itemService;

    // 서버 실행 시 supervisor가 하나도 없으면 root 생성
    @PostConstruct
    public void init(){
        log.info("PostConstructor init() ");
        List<Member> supervisorList = new ArrayList<>();
        try {
            supervisorList = memberService.findMemberByRole(Role.SUPERVISOR);
        } catch (NullPointerException e) {
            MemberJoinDTO member = new MemberJoinDTO("ROOT", "1234", "1234", "supervisor");
            MemberJoinDTO member2 = new MemberJoinDTO("SAMPLE", "1234", "1234", "manager");
            log.info("joinInit()호출");
            Long memberNo = memberService.joinInit(member);
            Long member2No = memberService.joinInit(member2);

            Member sample = memberService.findMemberById("SAMPLE");
            log.info("sample : {}", sample);
            ItemAddDTO itemAddDTO = new ItemAddDTO(sample.getId(), "item", 10000, "false");
            itemService.addItemInit(itemAddDTO, member2No);
            }

        }


}
