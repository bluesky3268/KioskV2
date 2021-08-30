package project.kiosk.kiosk.service;

import project.kiosk.kiosk.dto.MemberJoinDTO;
import project.kiosk.kiosk.dto.MemberLoginDTO;
import project.kiosk.kiosk.dto.MemberUpdateDTO;
import project.kiosk.kiosk.entity.Member;

public interface MemberService {

    void join(MemberJoinDTO memberJoinDTO);

    Member login(MemberLoginDTO memberLoginDTO);

    void logout();

    Member update(MemberUpdateDTO memberUpdateDTO);

}
