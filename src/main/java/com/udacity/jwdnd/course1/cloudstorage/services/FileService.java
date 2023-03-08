package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@Service
public class FileService {
    private final FileMapper fileMapper;
    private final UserService userService;

    public FileService(FileMapper fileMapper, UserService userService) {
        this.fileMapper = fileMapper;
        this.userService = userService;
    }

    public int createNewFile(MultipartFile fileUpload) throws IOException, SQLException {
        return fileMapper.insert(
                new File(
                        null,
                        fileUpload.getOriginalFilename(),
                        fileUpload.getContentType(),
                        String.valueOf(fileUpload.getSize()),
                        userService.getCurrentUserId(),
                        fileUpload.getInputStream()
                )
        );
    }

    public List<File> getAllFiles() {
        return fileMapper.getAllFiles(userService.getCurrentUserId());
    }


}
