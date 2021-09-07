package project.kiosk.kiosk.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import project.kiosk.kiosk.service.ItemService;
import project.kiosk.kiosk.service.MemberService;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/manager")
public class ShopController {

    private final ItemService itemService;
    private final MemberService memberService;


}
