package project.kiosk.kiosk.controller.apiController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;
import project.kiosk.kiosk.dto.MemberJoinDTO;
import project.kiosk.kiosk.dto.MemberUpdateDTO;
import project.kiosk.kiosk.dto.responseDto.ItemResponseDto;
import project.kiosk.kiosk.dto.responseDto.MemberListResponseDto;
import project.kiosk.kiosk.entity.Member;
import project.kiosk.kiosk.entity.Role;
import project.kiosk.kiosk.entity.UploadFile;
import project.kiosk.kiosk.service.MemberService;
import project.kiosk.kiosk.util.FileStore;

import java.net.MalformedURLException;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberApiController {

    private final MemberService memberService;
    private final FileStore fileStore;

    @PostMapping("/duplicateCheck")
    public String idDuplicateCheck(@RequestBody HashMap<String, Object> shop) {
        String checkName = String.valueOf(shop.get("shop"));
        boolean duplicateCheck = memberService.idDuplicateCheck(checkName);
        if (duplicateCheck) {
            // 아이디 사용가능
            log.info("아이디 사용가능");
            return "0";
        } else {
            // 중복된 아이디 있음
            log.info("아이디 중복");
            return "1";
        }
    }

    @PostMapping("/member")
    public Long addMember(@RequestPart(value = "key") @Validated MemberJoinDTO memberJoin, BindingResult bindingResult,
                          @RequestPart(value = "img") MultipartFile img) {

        Long result = 0L;
        if (!bindingResult.hasErrors()) {
            result = memberService.join(memberJoin, img);
        } else {
            log.info("bindingResult : {}", bindingResult.getAllErrors());
        }
        return result;
    }

    @PatchMapping("/member/{no}")
    public Long editMember(@PathVariable("no") Long memberNo, @RequestPart(value = "key") @Validated MemberUpdateDTO updateMember, BindingResult bindingResult,
                           @RequestPart(value = "img") MultipartFile img, Model model) {
        Long no = 0L;
        if (!bindingResult.hasErrors()) {
            Member member = memberService.updateMember(memberNo, updateMember, img);
            no = member.getNo();
        } else {
            log.info("bindingResult : {}", bindingResult.getAllErrors());
        }
        return no;
    }

    @DeleteMapping("/member/{no}")
    public Long deleteMember(@PathVariable("no") Long memberNo) {
        memberService.deleteMember(memberNo);
        return memberNo;
    }



    @GetMapping("/member/role/{role}")
    public List<Member> findMembersByRole(@PathVariable("role") String role, Model model) {
        List<Member> result = new ArrayList<>();
        try {
            if (role.equals("supervisor")) {
                result = memberService.findMemberByRole(Role.SUPERVISOR);
            } else {
                result = memberService.findMemberByRole(Role.MANAGER);

            }
            model.addAttribute("result", result);
            for (Member member : result) {
                log.info("member : {}", member);
            }
        } catch (NullPointerException e) {
            log.info("데이터가 없습니다 : {}, {}",e.getMessage(), e.getStackTrace() );
        }
        return result;

    }

    @GetMapping("/memberImages/{filename}")
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {

        Resource urlResource = memberService.downloadImage(filename);
        log.info("urlResource : {}", urlResource);

        return urlResource;
    }

    @GetMapping("/upload/{memberNo}")
    public ResponseEntity<Resource> downloadAttach(@PathVariable Long memberNo) throws MalformedURLException {
        Member findMember = memberService.findMemberByMemberNo(memberNo);
        String saveName = findMember.getThumbImg().getSaveName();
        String originalName = findMember.getThumbImg().getOriginalName();

        UrlResource urlResource = new UrlResource("file:" + fileStore.getFullPath(saveName));

        log.info("uploadFileName = {}", originalName);

        String encodedUploadFileName = UriUtils.encode(originalName, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(urlResource);
    }

}
