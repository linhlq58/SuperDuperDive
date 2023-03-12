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

    public Boolean isFileNameExist(String fileName) {
        return fileMapper.getFile(fileName) != null;
    }

    public int createNewFile(MultipartFile fileUpload) throws IOException, SQLException {
        return fileMapper.insert(
                new File(
                        null,
                        fileUpload.getOriginalFilename(),
                        fileUpload.getContentType(),
                        String.valueOf(fileUpload.getSize()),
                        userService.getCurrentUserId(),
                        fileUpload.getBytes()
                )
        );
    }

    public List<File> getAllFiles() {
        return fileMapper.getAllFiles(userService.getCurrentUserId());
    }

    public File getFileById(Integer fileId) {
        return fileMapper.getFileById(fileId);
    }

    public void deleteFile(Integer fileId) {
        fileMapper.deleteFile(fileId);
    }
}
