package project.kiosk.kiosk.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.kiosk.kiosk.dto.FileStore;
import project.kiosk.kiosk.dto.ItemAddDTO;
import project.kiosk.kiosk.dto.ItemUpdateDTO;
import project.kiosk.kiosk.entity.Item;
import project.kiosk.kiosk.entity.Member;
import project.kiosk.kiosk.entity.UploadFile;
import project.kiosk.kiosk.repository.ItemRepository;
import project.kiosk.kiosk.repository.MemberRepository;
import project.kiosk.kiosk.service.ItemService;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final MemberServiceImpl memberService;
    private final FileStore fileStore;
    private final FileServiceImpl fileService;

    @Override
    public Item addItem(ItemAddDTO itemAddDTO, String memberId) {

        Member findMember = memberService.findMemberById(memberId);

        Item item = new Item(itemAddDTO.getItemName(), itemAddDTO.getPrice(), itemAddDTO.isSoldOut(), findMember);

        Item savedItem = itemRepository.save(item);

        return savedItem;
    }




    @Override
    public Item editItem(Long itemNo, ItemUpdateDTO itemUpdate) {
        Item item = itemRepository.findByNo(itemNo);
        UploadFile updateImg = null;

        if (itemUpdate.getImg() != null) {
            MultipartFile img = itemUpdate.getImg();
            try {
                updateImg = fileStore.saveFile(img);
                item.setImg(updateImg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        item.setPrice(itemUpdate.getPrice());
        item.setSoldOut(itemUpdate.isSoldOut());

        return item;
    }

    @Override
    public void deleteItem(Long itemNo) {
        itemRepository.deleteByNo(itemNo);
    }

    @Override
    public Item findItem(Long no) {
        return itemRepository.findByNo(no);
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
    public Item findItemDetail(Long itemNo) {
        return itemRepository.findByNo(itemNo);
    }

    @Override
    public Page<Item> findByMemberNoWithPage(Long no, Pageable pageable) {
        return itemRepository.findByMemberNo(no, pageable);
    }

    @Override
    public Page<Item> findAll(Pageable pageable) {
        return itemRepository.findAll(pageable);
    }

}
