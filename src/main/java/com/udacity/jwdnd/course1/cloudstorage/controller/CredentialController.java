package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CredentialController {
    private final CredentialService credentialService;

    public CredentialController(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    @PostMapping("/home/credential-add")
    public String addCredential(@ModelAttribute Credential credential, Model model, RedirectAttributes redirectAttributes) {
        if (credential.getCredentialId() != null) {
            credentialService.updateCredential(credential);
        } else {
            credentialService.createNewCredential(credential);
        }

        model.addAttribute("listCredentials", credentialService.getAllCredentials());
        redirectAttributes.addFlashAttribute("successMessage", true);

        return "redirect:/result";
    }

    @GetMapping("/home/credential-delete/{credentialId}")
    public String deleteCredential(@PathVariable Integer credentialId, Model model, RedirectAttributes redirectAttributes) {
        credentialService.deleteCredential(credentialId);
        model.addAttribute("listCredentials", this.credentialService.getAllCredentials());
        redirectAttributes.addFlashAttribute("successMessage", true);

        return "redirect:/result";
    }
}
