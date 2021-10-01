package project.kiosk.kiosk.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;
import project.kiosk.kiosk.dto.ItemAddDTO;
import project.kiosk.kiosk.dto.ItemUpdateDTO;
import project.kiosk.kiosk.dto.responseDto.ItemResponseDto;
import project.kiosk.kiosk.entity.Item;
import project.kiosk.kiosk.entity.Member;
import project.kiosk.kiosk.service.FileService;
import project.kiosk.kiosk.service.ItemService;
import project.kiosk.kiosk.service.MemberService;
import project.kiosk.kiosk.util.FileStore;

import javax.servlet.http.HttpSession;
import java.net.MalformedURLException;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ItemApiController {

    private final ItemService itemService;
    private final MemberService memberService;
    private final FileStore fileStore;


    @GetMapping("/item/{itemNo}")
    public ItemResponseDto itemDetail(@PathVariable Long itemNo, Model model) {

        ItemResponseDto responseItem = itemService.findItem(itemNo);

        return responseItem;
    }

    @PatchMapping("/item/{itemNo}")
    public Long editItem(@PathVariable Long itemNo, @RequestPart(value="key") @Validated ItemUpdateDTO itemUpdate, BindingResult bindingResult, @RequestPart(value="img") MultipartFile img, Model model) {

        if (!bindingResult.hasErrors()) {
            itemService.editItem(itemNo, itemUpdate, img);
        }else{
            return 0L;
        }

        return itemNo;
    }

    @DeleteMapping("/item/{itemNo}")
    public Long deleteItem(@PathVariable Long itemNo) {
        itemService.deleteItem(itemNo);
        return itemNo;
    }

    @GetMapping("/itemImages/{filename}")
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
        Resource urlResource = itemService.downloadImage(filename);
        log.info("urlResource : {}", urlResource);

        return urlResource;
    }

    @GetMapping("/upload/{itemNo}")
    public ResponseEntity<Resource> downloadAttach(@PathVariable Long itemNo) throws MalformedURLException {
        ItemResponseDto item = itemService.findItem(itemNo);
        String saveName = item.getThumbImg().getSaveName();
        String originalName = item.getThumbImg().getOriginalName();

        UrlResource urlResource = new UrlResource("file:" + fileStore.getFullPath(saveName));

        log.info("uploadFileName = {}", originalName);

        String encodedUploadFileName = UriUtils.encode(originalName, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(urlResource);
    }


}
