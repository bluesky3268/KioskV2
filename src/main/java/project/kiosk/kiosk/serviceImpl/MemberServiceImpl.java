package project.kiosk.kiosk.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import project.kiosk.kiosk.dto.responseDto.MemberListResponseDto;
import project.kiosk.kiosk.service.FileService;
import project.kiosk.kiosk.util.FileStore;
import project.kiosk.kiosk.dto.MemberJoinDTO;
import project.kiosk.kiosk.dto.MemberLoginDTO;
import project.kiosk.kiosk.dto.MemberUpdateDTO;
import project.kiosk.kiosk.entity.Member;
import project.kiosk.kiosk.entity.Role;
import project.kiosk.kiosk.entity.UploadFile;
import project.kiosk.kiosk.repository.MemberRepository;
import project.kiosk.kiosk.service.MemberService;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileStore fileStore;
    private final FileService fileService;

    @Override
    public Long joinInit(MemberJoinDTO memberJoinDTO) {
        LocalDateTime regDate = LocalDateTime.now();
        String encodedPwd = passwordEncoder.encode(memberJoinDTO.getPassword());

        Member member = null;
        Role role = null;
        if (memberJoinDTO.getRole().equals("supervisor")) {
            role = Role.SUPERVISOR;
        }else{
            role = Role.MANAGER;
        }
        member = new Member(memberJoinDTO.getId(), encodedPwd, regDate, role);

        log.info("joinMember : {}", memberJoinDTO.getId());

        Member savedMember = memberRepository.save(member);

        log.info("savedMember : {}", savedMember.getId());

        return savedMember.getNo();
    }

    @Override
    public Long join(MemberJoinDTO memberJoin, MultipartFile multipartFile) {
        if (memberJoin == null || multipartFile.isEmpty()) {
            return null;
        }

        if (idCheck(memberJoin.getId()) && pwdCheck(memberJoin.getPassword(), memberJoin.getPasswordConfirm())) {

            LocalDateTime regDate = LocalDateTime.now();
            String encodedPwd = passwordEncoder.encode(memberJoin.getPassword());

            UploadFile uploadFile = null;
            try {
                uploadFile = fileStore.saveFile(multipartFile);
                log.info("uploadFile 변환 성공 : {}, {}", uploadFile.getOriginalName(), uploadFile.getSaveName());
                fileService.addFile(uploadFile);
                log.info("uploadFile 저장 성공");

            } catch (IOException e) {
                e.printStackTrace();
            }

            Member member = null;
            Role role = null;
            if(memberJoin.getRole().equals("supervisor")){
                role = Role.SUPERVISOR;
            }else {
                role = Role.MANAGER;
            }

            member = new Member(memberJoin.getId(),
                    encodedPwd,
                    memberJoin.getLocation(),
                    regDate,
                    role,
                    uploadFile);


            Member savedMember = memberRepository.save(member);
            log.info("등록 성공");
            return savedMember.getNo();
        }
        return null;
    }

    @Override
    public boolean idDuplicateCheck(String id) {
        Member findMember = memberRepository.findMemberById(id);
        if (findMember == null){
            // DB에 저장된 값이 없으면 사용 가능
            return true;
        }
//        DB에 저장된 값이 있으면 아이디 중복
        return false;

    }
    
    @Override
    public String login(MemberLoginDTO memberLoginDTO) {
        log.info("loginParameter : {}, {}", memberLoginDTO.getLoginId(), memberLoginDTO.getLoginPwd());
        Member findMember = memberRepository.findMemberById(memberLoginDTO.getLoginId());


        log.info("findMember : {}, {}", findMember.getId(), findMember.getRole());
        if (findMember != null) {
            log.info(memberLoginDTO.getLoginPwd());
            log.info(findMember.getPassword());
            if (passwordEncoder.matches(memberLoginDTO.getLoginPwd(), findMember.getPassword())) {
                return findMember.getId();
            }else{
                log.info("올바르지 않은 비밀번호");
            }
        }
        log.info("올바르지 않은 아이디");
        return null;

    }

    @Override
    public String logout(HttpSession session) {
//        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
            return "true";
        }else{
            log.info("로그인이 먼저 필요");
            return "false";
        }
    }

    @Override
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    @Override
    public Page<Member> memberPaging(Pageable pageable) {
        pageable = Pageable.ofSize(9);
        Page<Member> managers = memberRepository.findAllByRoleLike(Role.MANAGER, pageable);
        log.info("pageSize : {}", pageable.getPageSize());
        return managers;
    }

    @Override
    public List<Member> findMemberByRole(Role role) {

        log.info("role parameter : {}", role);
        List<Member> result = memberRepository.findMembersByRoleLike(role);

//        List<MemberListResponseDto> list = new ArrayList<>();

//        for (Member member : result) {
//            list.add(new MemberListResponseDto(member));
//        }

        if (!result.isEmpty()) {
            return result;
        } else {
            throw new NullPointerException("데이터 베이스에 저장된 값이 하나도 없습니다.");
        }

    }

    @Override
    public Member findMemberById(String id) {
        Member findMember = memberRepository.findMemberById(id);
        return findMember;
    }

    @Override
    public Member findMemberByMemberNo(Long no) {
        return memberRepository.findMemberByNo(no);
    }

    private boolean pwdCheck(String password, String passwordConfirm) {
        return true;
    }

    private boolean idCheck(String loginId) {
        return true;
    }

    @Override
    public Member updateMember(Long memberNo, MemberUpdateDTO updateMember, MultipartFile multipartFile) {
        log.info("updateMember pwd : {}", updateMember.getPassword());

        Member member = memberRepository.findMemberByNo(memberNo);

        String password = "";
        if (updateMember.getPassword() == null || updateMember.getPassword() == "") {
            password = member.getPassword();
        } else {
            String encodedPwd = passwordEncoder.encode(updateMember.getPassword());
            password = encodedPwd;
        }
        log.info("password : {}", password);

        if (!multipartFile.isEmpty()) {
            try {
                UploadFile newImg = fileStore.saveFile(multipartFile);
                fileService.addFile(newImg);
                member.setThumbImg(newImg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Role role = null;
        if (updateMember.getRole().equals("none")) {
            role = member.getRole();
        }else if (updateMember.getRole().equals("supervisor")) {
            role = Role.SUPERVISOR;
        } else {
            role = Role.MANAGER;
        }

        String location = null;
        if (updateMember.getLocation().equals("") || updateMember.getLocation() == null) {
            location = member.getLocation();
        }

        member.setPassword(password);
        member.setLocation(location);
        member.setRole(role);

        return member;
    }

    @Override
    public void deleteMember(Long memberNo) {
        memberRepository.deleteMemberByNo(memberNo);
    }

    @Override
    public Resource downloadImage(String filename) throws MalformedURLException {
        UrlResource urlResource = new UrlResource("file:" + fileStore.getFullPath(filename));
        return urlResource;
    }
}
