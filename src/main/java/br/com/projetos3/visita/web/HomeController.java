package br.com.projetos3.visita.web;

import br.com.projetos3.visita.entity.*;
import br.com.projetos3.visita.service.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
public class HomeController {

    private final AvisoService avisoService;
    private final FeedbackService feedbackService;
    private final SugestaoService sugestaoService;

    public HomeController(AvisoService a, FeedbackService f, SugestaoService s){
        this.avisoService = a;
        this.feedbackService = f;
        this.sugestaoService = s;
    }

    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(value="ok", required=false) String ok,
                        @RequestParam(value="erro", required=false) String erro) {
        List<Aviso> avisos = avisoService.vigentes();
        model.addAttribute("avisos", avisos);
        model.addAttribute("hoje", LocalDate.now());

        model.addAttribute("feedback", new Feedback());
        model.addAttribute("sugestao", new Sugestao());
        model.addAttribute("ok", ok);
        model.addAttribute("erro", erro);
        return "home/index";
    }

    @PostMapping("/feedback")
    public String enviarFeedback(@Valid @ModelAttribute("feedback") Feedback feedback,
                                 BindingResult br,
                                 RedirectAttributes ra) {
        if (br.hasErrors()) {
            ra.addAttribute("erro","home.feedback.erro");
            return "redirect:/";
        }
        feedbackService.salvar(feedback);
        ra.addAttribute("ok","home.feedback.ok");
        return "redirect:/";
    }

    @PostMapping("/sugestao")
    public String enviarSugestao(@Valid @ModelAttribute("sugestao") Sugestao sugestao,
                                 BindingResult br,
                                 RedirectAttributes ra) {
        if (br.hasErrors()) {
            ra.addAttribute("erro","home.sugestao.erro");
            return "redirect:/";
        }
        sugestaoService.salvar(sugestao);
        ra.addAttribute("ok","home.sugestao.ok");
        return "redirect:/";
    }
}
