package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import org.springframework.http.HttpHeaders;
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
    private FileService fileService;

    public HomeController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/home")
    public String homeView(Model model) {
        model.addAttribute("listFiles", this.fileService.getAllFiles());
        return "home";
    }

    @PostMapping("/file-upload")
    public String handleFileUpload(@RequestParam("fileUpload") MultipartFile fileUpload, Model model) throws IOException, SQLException {
        int fileIdAdded = fileService.createNewFile(fileUpload);

        if (fileIdAdded > 0) {
            model.addAttribute("uploadSuccess", true);
            model.addAttribute("listFiles", this.fileService.getAllFiles());
        }

        return "home";
    }

//    @GetMapping("/file-download")
//    public ResponseEntity<byte[]> downloadFile(@ModelAttribute File file, Model model) {
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "" + "\"")
//                .body(file.getFileData());
//    }
}
