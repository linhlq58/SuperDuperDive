package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

@Controller
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/home/file-upload")
    public String handleFileUpload(@RequestParam("fileUpload") MultipartFile fileUpload, Model model, RedirectAttributes redirectAttributes) throws IOException, SQLException {
        String uploadError = null;

        if (fileService.isFileNameExist(fileUpload.getOriginalFilename())) {
            uploadError = "Can not upload a file with same name.";
        }

        if (uploadError != null) {
            model.addAttribute("uploadError", uploadError);
        } else {
            int rowAdded = fileService.createNewFile(fileUpload);

            if (rowAdded > 0) {
                model.addAttribute("uploadSuccess", true);
                redirectAttributes.addFlashAttribute("successMessage", true);
            }
        }

        model.addAttribute("listFiles", this.fileService.getAllFiles());

        return "redirect:/result";
    }

    @GetMapping("/home/file-delete/{fileId}")
    public String deleteFile(@PathVariable Integer fileId, Model model, RedirectAttributes redirectAttributes) {
        fileService.deleteFile(fileId);
        model.addAttribute("listFiles", this.fileService.getAllFiles());
        redirectAttributes.addFlashAttribute("successMessage", true);

        return "redirect:/result";
    }

    @GetMapping("/home/file-download/{fileId}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Integer fileId, Model model) {
        File file = fileService.getFileById(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                .body(new ByteArrayResource(file.getFileData()));
    }
}
