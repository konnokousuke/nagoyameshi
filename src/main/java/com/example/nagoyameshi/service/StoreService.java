package com.example.nagoyameshi.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.nagoyameshi.entity.Store;
import com.example.nagoyameshi.entity.StoreDayOffs;
import com.example.nagoyameshi.form.StoreEditForm;
import com.example.nagoyameshi.form.StoreRegisterForm;
import com.example.nagoyameshi.repository.StoreRepository;

@Service
public class StoreService {
	private final StoreRepository storeRepository;
	
	public StoreService(StoreRepository storeRepository) {
		this.storeRepository = storeRepository;
	}
	
	@Transactional
	public void create(StoreRegisterForm storeRegisterForm) {
		Store store = new Store();
		MultipartFile imageFile = storeRegisterForm.getImageFile();
		
		if (!imageFile.isEmpty()) {
			String imageName = imageFile.getOriginalFilename();
			String hashedImageName = generateNewFileName(imageName);
			Path filePath = Paths.get("src/main/resources/static/storage/" + hashedImageName);
			copyImageFile(imageFile, filePath);
			store.setImageFilename(hashedImageName);
		}
		
		store.setStoreName(storeRegisterForm.getName());
		store.setDescription(storeRegisterForm.getDescription());
		store.setPrice(storeRegisterForm.getPrice().intValue()); // Long型からint型へキャスト
		store.setPostalCode(storeRegisterForm.getPostalCode());
		store.setAddress(storeRegisterForm.getAddress());
		store.setPhoneNumber(storeRegisterForm.getPhoneNumber());
		store.setOpeningHours(storeRegisterForm.getOpeningHours());
		store.setClosedDays(storeRegisterForm.getClosedDays());
		
	    store.setClosingTime(storeRegisterForm.getClosingTime()); // フォームにclosingTimeフィールドがある前提
	    
	 // 定休日のセット
	    Set<StoreDayOffs> storeDayOffs = storeRegisterForm.getDayOffs().stream()
	            .map(dayOfWeek -> {
	                StoreDayOffs dayOff = new StoreDayOffs();
	                dayOff.setDayOfWeek(dayOfWeek);
	                dayOff.setStore(store); // Storeエンティティに関連付ける
	                return dayOff;
	            }).collect(Collectors.toSet());

	    store.setStoreDayOffs(storeDayOffs); // StoreのStoreDayOffsフィールドにセット
		
		storeRepository.save(store);
	}
	
	@Transactional
	public void update(StoreEditForm storeEditForm) {
		Store store = storeRepository.getReferenceById(storeEditForm.getStoreId());
		MultipartFile imageFile = storeEditForm.getImageFile();
		
		if (!imageFile.isEmpty()) {
			String imageName = imageFile.getOriginalFilename();
			String hashedImageName = generateNewFileName(imageName);
			Path filePath = Paths.get("src/main/resources/static/storage/" + hashedImageName);
			copyImageFile(imageFile, filePath);
			store.setImageFilename(hashedImageName);
		}
		
		store.setStoreName(storeEditForm.getName());
		store.setDescription(storeEditForm.getDescription());
		store.setPrice(storeEditForm.getPrice().intValue()); // Long型からint型へキャスト
		store.setPostalCode(storeEditForm.getPostalCode());
		store.setAddress(storeEditForm.getAddress());
		store.setPhoneNumber(storeEditForm.getPhoneNumber());
		store.setOpeningHours(storeEditForm.getOpeningHours());
		store.setClosedDays(storeEditForm.getClosedDays());
		
		store.setClosingTime(storeEditForm.getClosingTime()); // フォームにclosingTimeフィールドがある前提
		
		// 定休日の更新
	    Set<StoreDayOffs> storeDayOffs = storeEditForm.getDayOffs().stream()
	            .map(dayOfWeek -> {
	                StoreDayOffs dayOff = new StoreDayOffs();
	                dayOff.setDayOfWeek(dayOfWeek);
	                dayOff.setStore(store); // Storeエンティティに関連付ける
	                return dayOff;
	            }).collect(Collectors.toSet());

	    store.setStoreDayOffs(storeDayOffs); // StoreのStoreDayOffsフィールドにセット
	    
		storeRepository.save(store);
	}
	
	// UUIDを使って生成したファイル名を返す
	public String generateNewFileName(String fileName) {
		String[] fileNames = fileName.split("\\\\.");
		for (int i = 0; i < fileNames.length - 1; i++) {
			fileNames[i] = UUID.randomUUID().toString();
		}
		String hashedFileName = String.join(".", fileNames);
		return hashedFileName;
	}
	
	// 画像ファイルを指定したファイルにコピーする
	public void copyImageFile(MultipartFile imageFile, Path filePath) {
		try {
			Files.copy(imageFile.getInputStream(), filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
