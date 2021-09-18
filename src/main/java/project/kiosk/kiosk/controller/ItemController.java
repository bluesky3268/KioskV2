package project.kiosk.kiosk.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import project.kiosk.kiosk.dto.ItemAddDTO;
import project.kiosk.kiosk.dto.ItemUpdateDTO;
import project.kiosk.kiosk.entity.Item;
import project.kiosk.kiosk.service.ItemService;
import project.kiosk.kiosk.service.MemberService;

import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class ItemController {

    private final ItemService itemService;
    private final MemberService memberService;

    @GetMapping("/items")
    public String itemAddForm() {
        return "admin/itemAddForm";
    }

    @PostMapping("/items")
    public String itemAdd(@RequestBody @Validated ItemAddDTO itemAdd, BindingResult bindingResult, HttpSession session) {

        if (!bindingResult.hasErrors()) {
            String memberId = String.valueOf(session.getAttribute("loggedIn"));
            itemService.addItem(itemAdd, memberId);
        }else{
            log.info("bindingError : {}", bindingResult.getAllErrors());
            return "redirect:/items";
        }

        return "redirect:/items/{memberNo}";
    }


    @GetMapping("/items/{memberNo}")
    public String itemList(@PathVariable("memberNo") Long no, Model model, @PageableDefault(size = 5, sort = "no", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<Item> itemList = itemService.findByMemberNoWithPage(no, pageable);

        model.addAttribute("items", itemList);

        return "admin/itemList";
    }

    @GetMapping("/items/{memberNo}/{itemNo}")
    public String itemDetail(@PathVariable Long memberNo, @PathVariable Long itemNo, Model model) {

        Item item = itemService.findItemDetail(itemNo);

        model.addAttribute("item", item);

        return "admin/itemDetail";
    }

    @PatchMapping("/items/{memberNo}/{itemNo}")
    public String editItem(@PathVariable Long memberNo, @PathVariable Long itemNo, @RequestBody @Validated ItemUpdateDTO itemUpdate, BindingResult bindingResult, Model model) {

        if (!bindingResult.hasErrors()) {
            itemService.editItem(itemNo, itemUpdate);
        }else{
            log.info("bindResult : {}", bindingResult.getAllErrors());
        }
        return "redirect:/items/{memberNo}/{itemNo}";

    }

    @DeleteMapping("/items/{memberNo}/{itemNo}")
    public String deleteItem(@PathVariable Long memberNo, @PathVariable Long itemNo) {

        itemService.deleteItem(itemNo);

        return "redirect:/items/{memberNo}/{itemNo}";
    }

}
