package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class NoteController {
    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping("/home/note-add")
    public String addNote(@ModelAttribute Note note, Model model, RedirectAttributes redirectAttributes) {
        if (note.getNoteId() != null) {
            noteService.updateNote(note);
        } else {
            noteService.createNewNote(note);
        }

        model.addAttribute("listNotes", noteService.getAllNotes());
        redirectAttributes.addFlashAttribute("successMessage", true);

        return "redirect:/result";
    }

    @GetMapping("/home/note-delete/{noteId}")
    public String deleteNote(@PathVariable Integer noteId, Model model, RedirectAttributes redirectAttributes) {
        noteService.deleteNote(noteId);
        model.addAttribute("listNotes", this.noteService.getAllNotes());
        redirectAttributes.addFlashAttribute("successMessage", true);

        return "redirect:/result";
    }
}
