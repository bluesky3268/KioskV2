package project.kiosk.kiosk.service;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import project.kiosk.kiosk.dto.ItemAddDTO;
import project.kiosk.kiosk.dto.ItemUpdateDTO;
import project.kiosk.kiosk.dto.responseDto.ItemResponseDto;
import project.kiosk.kiosk.entity.Item;
import project.kiosk.kiosk.entity.Role;

import java.net.MalformedURLException;
import java.util.List;

public interface ItemService {

    Long addItemInit(ItemAddDTO itemAddDTO, Long memberNo);

    Long addItem(ItemAddDTO itemAddDTO, MultipartFile multipartFile);

    Long editItem(Long no, ItemUpdateDTO itemUpdate, MultipartFile img);

    void deleteItem(Long no);

    List<Item> findItems(Long memberNo);

    Item findItemEntity(Long no);

    ItemResponseDto findItem(Long no);

    Item findItemByItemName(String itemName);

    Page<Item> findByMemberNoWithPage(Long no, Pageable pageable);

    Page<Item> findAll(Pageable pageable);

    Resource downloadImage(String filename) throws MalformedURLException;

}
