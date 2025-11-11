package br.com.projetos3.visita.web;

import br.com.projetos3.visita.entity.Regra; // Importar
import br.com.projetos3.visita.service.RegraService; // Importar
import jakarta.validation.Valid; // Importar
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // Importar
import org.springframework.validation.BindingResult; // Importar
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping; // Importar
import org.springframework.web.bind.annotation.ModelAttribute; // Importar
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // Importar

@Controller
@RequestMapping("/admin")
public class AdminController {

    // 1. Injete o RegraService
    private final RegraService regraService;

    // 2. Atualize o construtor
    public AdminController(RegraService regraService) {
        this.regraService = regraService;
    }

    @GetMapping
    public String adminHome() {
        return "admin/index";
    }

    // 3. Adicione o método GET para /admin/regras
    @GetMapping("/regras")
    public String editarRegras(Model model) {
        if (!model.containsAttribute("regra")) {
            model.addAttribute("regra", regraService.getRegra());
        }
        model.addAttribute("pageTitle", "Editar Regras");
        return "admin/regras";
    }

    // Página de relatórios
    @GetMapping("/relatorios")
    public String relatorios() {
        return "admin/relatorios"; // renderiza src/main/resources/templates/admin/relatorios.html
    }

    // 4. Adicione o método POST para /admin/regras
    @PostMapping("/regras")
    public String salvarRegras(@Valid @ModelAttribute("regra") Regra regra,
                               BindingResult br,
                               RedirectAttributes ra,
                               Model model) {
        if (br.hasErrors()) {
            model.addAttribute("pageTitle", "Editar Regras");
            model.addAttribute("error", "Erro ao salvar: Verifique os campos.");
            return "admin/regras";
        }

        regraService.salvar(regra);
        ra.addFlashAttribute("success", "Regras salvas com sucesso!");
        return "redirect:/admin/regras";
    }
}