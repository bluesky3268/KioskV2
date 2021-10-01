package project.kiosk.kiosk.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.kiosk.kiosk.dto.responseDto.ItemResponseDto;
import project.kiosk.kiosk.entity.Role;
import project.kiosk.kiosk.util.FileStore;
import project.kiosk.kiosk.dto.ItemAddDTO;
import project.kiosk.kiosk.dto.ItemUpdateDTO;
import project.kiosk.kiosk.entity.Item;
import project.kiosk.kiosk.entity.Member;
import project.kiosk.kiosk.entity.UploadFile;
import project.kiosk.kiosk.repository.ItemRepository;
import project.kiosk.kiosk.service.ItemService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final MemberServiceImpl memberService;
    private final FileStore fileStore;
    private final FileServiceImpl fileService;

    @Override
    public Long addItemInit(ItemAddDTO itemAddDTO, Long memberNo) {
        Member findMember = memberService.findMemberByMemberNo(memberNo);

        boolean soldOut = false;
        if (itemAddDTO.getIsSoldOut().equals("true")) {
            soldOut = true;
        }
        Item item = new Item(itemAddDTO.getItemName(), itemAddDTO.getPrice(), soldOut, findMember);

        Item savedItem = itemRepository.save(item);

        return savedItem.getNo();
    }

    @Override
    public Long addItem(ItemAddDTO itemAddDTO, Long memberNo) {

        Member findMember = memberService.findMemberByMemberNo(memberNo);

        UploadFile uploadFile = null;
        try {
            uploadFile = fileStore.saveFile(itemAddDTO.getImg());
            fileService.addFile(uploadFile);
            log.info("파일등록 성공 : {}", uploadFile.getOriginalName());
        } catch (IOException e) {
            e.printStackTrace();
        }

        boolean soldOut = false;
        if (itemAddDTO.getIsSoldOut().equals("true")) {
            soldOut = true;
        }

        Item item = new Item(itemAddDTO.getItemName(), itemAddDTO.getPrice(), uploadFile, soldOut, findMember);

        Item savedItem = itemRepository.save(item);

        return savedItem.getNo();
    }




    @Override
    public Long editItem(Long itemNo, ItemUpdateDTO itemUpdate, MultipartFile img) {

        Item item = itemRepository.findByNo(itemNo);
        UploadFile updateImg = null;

        if (img != null) {
            try {
                updateImg = fileStore.saveFile(img);
                fileService.addFile(updateImg);
                log.info("파일 수정 성공");
                item.setImg(updateImg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        item.setPrice(itemUpdate.getPrice());
        if (itemUpdate.getIsSoldOut().equals("true")) {
            item.setSoldOut(true);
        }else{
            item.setSoldOut(false);
        }

        return item.getNo();
    }

    @Override
    public void deleteItem(Long itemNo) {
        itemRepository.deleteById(itemNo);
    }

    @Override
    public ItemResponseDto findItem(Long no) {
        Item findItem = itemRepository.findByNo(no);
        ItemResponseDto responseItem = new ItemResponseDto(findItem.getNo(), findItem.getItemName(), findItem.getPrice(), findItem.getImg(), findItem.isSoldOut(), findItem.getMember().getId());
        return responseItem;
    }

    @Override
    public Item findItemByItemName(String itemName) {
        return itemRepository.findByItemName(itemName);
    }

    @Override
    public List<Item> findItems(Long memberNo) {
        return itemRepository.findAllByMemberNo(memberNo);
    }

    @Override
    public Page<Item> findByMemberNoWithPage(Long no, Pageable pageable) {
        return itemRepository.findByMemberNo(no, pageable);
    }

    @Override
    public Page<Item> findAll(Pageable pageable) {
        return itemRepository.findAll(pageable);
    }

    @Override
    public Resource downloadImage(String filename) throws MalformedURLException {
        UrlResource urlResource = new UrlResource("file:" + fileStore.getFullPath(filename));
        return urlResource;
    }

}
