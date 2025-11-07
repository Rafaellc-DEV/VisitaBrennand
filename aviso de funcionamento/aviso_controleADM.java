package com.exemplo.visitabrennand.controller;

import com.exemplo.visitabrennand.model.Aviso;
import com.exemplo.visitabrennand.service.AvisoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/avisos")
public class AdminAvisoController {
    private final AvisoService avisoService;

    public AdminAvisoController(AvisoService avisoService) {
        this.avisoService = avisoService;
    }

    @GetMapping
    public String listarAvisos(Model model) {
        model.addAttribute("avisos", avisoService.listar());
        return "admin/avisos";
    }

    @PostMapping("/salvar")
    public String salvarAviso(@ModelAttribute Aviso aviso) {
        avisoService.salvar(aviso);
        return "redirect:/admin/avisos";
    }
}
