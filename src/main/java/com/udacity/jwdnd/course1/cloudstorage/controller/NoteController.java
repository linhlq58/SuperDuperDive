package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class NoteController {
    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping("/home/add-note")
    public String addNote(@ModelAttribute Note note, Model model) {
        int rowAdded = noteService.createNewNote(note);

        if (rowAdded > 0) {

        }

        model.addAttribute("listFiles", noteService.getAllNotes());

        return "redirect:/home#nav-notes";
    }
}
