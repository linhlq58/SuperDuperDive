package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

@Controller
public class HomeController {
    private final FileService fileService;

    public HomeController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/home")
    public String homeView(Model model) {
        model.addAttribute("listFiles", this.fileService.getAllFiles());
        return "home";
    }

    @PostMapping("/home/file-upload")
    public String handleFileUpload(@RequestParam("fileUpload") MultipartFile fileUpload, Model model) throws IOException, SQLException {
        String uploadError = null;

        if (fileService.isFileNameExist(fileUpload.getOriginalFilename())) {
            uploadError = "Can not upload a file with same name.";
        }

        if (uploadError != null) {
            model.addAttribute("uploadError", uploadError);
        } else {
            int fileIdAdded = fileService.createNewFile(fileUpload);

            if (fileIdAdded > 0) {
                model.addAttribute("uploadSuccess", true);
            }
        }

        model.addAttribute("listFiles", this.fileService.getAllFiles());

        return "redirect:/home";
    }

    @GetMapping("/home/file-delete/{fileId}")
    public String deleteFile(@PathVariable Integer fileId, Model model) {
        fileService.deleteFile(fileId);
        model.addAttribute("listFiles", this.fileService.getAllFiles());

        return "redirect:/home";
    }

    @GetMapping("/home/file-download/{fileId}")
    public ResponseEntity<InputStream> downloadFile(@PathVariable Integer fileId, Model model) {
        File file = fileService.getFileById(fileId);

        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType(file.getContentType()));
        header.set

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                .body(file.getFileData());
    }
}
