package com.ssafy.freezetag.domain.room.service;

import com.ssafy.freezetag.domain.exception.custom.RoomNotFoundException;
import com.ssafy.freezetag.domain.room.entity.Room;
import com.ssafy.freezetag.domain.room.repository.RoomRepository;
import com.ssafy.freezetag.global.s3.S3UploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemoryBoxService {
    private final RoomRepository roomRepository;
    private final S3UploadService s3UploadService;

    public void uploadBeforeMemoryImage(Long roomId, MultipartFile image) throws IOException {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(RoomNotFoundException::new);

        String s3UploadUrl = s3UploadService.saveFile(image);
        room.assignBeforeImage(s3UploadUrl);
    }

    public void uploadAfterMemoryImage(Long roomId, MultipartFile image) throws IOException {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(RoomNotFoundException::new);

        String s3UploadUrl = s3UploadService.saveFile(image);
        room.assignAfterImage(s3UploadUrl);
    }
}
