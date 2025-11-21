package br.com.projetos3.visita.web;

import br.com.projetos3.visita.entity.ComoChegar;
import br.com.projetos3.visita.entity.Feedback;
import br.com.projetos3.visita.entity.Horario;
import br.com.projetos3.visita.entity.Regra;
import br.com.projetos3.visita.repository.FeedbackRepository;
import br.com.projetos3.visita.service.ComoChegarService;
import br.com.projetos3.visita.service.HorarioService;
import br.com.projetos3.visita.service.RegraService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final RegraService regraService;
    private final ComoChegarService comoChegarService;
    private final HorarioService horarioService;
    private final FeedbackRepository feedbackRepository;

    public AdminController(RegraService regraService,
                           ComoChegarService comoChegarService,
                           HorarioService horarioService,
                           FeedbackRepository feedbackRepository) {
        this.regraService = regraService;
        this.comoChegarService = comoChegarService;
        this.horarioService = horarioService;
        this.feedbackRepository = feedbackRepository;
    }

    @ModelAttribute("currentUri")
    public String getCurrentUri(HttpServletRequest request) {
        return request.getRequestURI();
    }

    @GetMapping
    public String adminHome(Model model) {
        // Agora carregamos os dados do gráfico também na Home
        carregarDadosDeRelatorio(model);
        return "admin/index";
    }

    // --- MÉTODOS EXISTENTES (Regras, Como Chegar, Horários) ---
    @GetMapping("/regras")
    public String editarRegras(Model model) {
        if (!model.containsAttribute("regra")) {
            model.addAttribute("regra", regraService.getRegra());
        }
        model.addAttribute("pageTitle", "Editar Regras");
        return "admin/regras";
    }

    @PostMapping("/regras")
    public String salvarRegras(@Valid @ModelAttribute("regra") Regra regra,
                               BindingResult br, RedirectAttributes ra, Model model) {
        if (br.hasErrors()) {
            model.addAttribute("pageTitle", "Editar Regras");
            model.addAttribute("error", "Erro ao salvar: Verifique os campos.");
            return "admin/regras";
        }
        regraService.salvar(regra);
        ra.addFlashAttribute("success", "Regras salvas com sucesso!");
        return "redirect:/admin/regras";
    }

    @GetMapping("/como-chegar")
    public String editarComoChegar(Model model) {
        if (!model.containsAttribute("comoChegar")) {
            model.addAttribute("comoChegar", comoChegarService.getComoChegar());
        }
        model.addAttribute("pageTitle", "Editar Como Chegar");
        return "admin/como_chegar";
    }

    @PostMapping("/como-chegar")
    public String salvarComoChegar(@Valid @ModelAttribute("comoChegar") ComoChegar comoChegar,
                                   BindingResult br, RedirectAttributes ra, Model model) {
        if (br.hasErrors()) {
            model.addAttribute("pageTitle", "Editar Como Chegar");
            model.addAttribute("error", "O conteúdo não pode estar vazio.");
            return "admin/como_chegar";
        }
        comoChegarService.salvar(comoChegar);
        ra.addFlashAttribute("success", "Informações salvas com sucesso!");
        return "redirect:/admin/como-chegar";
    }

    @GetMapping("/horarios")
    public String editarHorarios(Model model) {
        if (!model.containsAttribute("horario")) {
            model.addAttribute("horario", horarioService.getHorario());
        }
        model.addAttribute("pageTitle", "Editar Horários");
        return "admin/horarios";
    }

    @PostMapping("/horarios")
    public String salvarHorarios(@Valid @ModelAttribute("horario") Horario horario,
                                 BindingResult br, RedirectAttributes ra, Model model) {
        if (br.hasErrors()) {
            model.addAttribute("pageTitle", "Editar Horários");
            model.addAttribute("error", "Erro ao salvar. Verifique se os horários estão no formato HH:mm.");
            return "admin/horarios";
        }
        horarioService.salvar(horario);
        ra.addFlashAttribute("success", "Horários atualizados com sucesso!");
        return "redirect:/admin/horarios";
    }

    @GetMapping("/relatorios")
    public String relatorios(Model model) {
        carregarDadosDeRelatorio(model);
        return "admin/relatorios";
    }

    // --- MÉTODO PRIVADO AUXILIAR PARA CALCULAR DADOS ---
    private void carregarDadosDeRelatorio(Model model) {
        List<Feedback> todos = feedbackRepository.findAll();
        long total = todos.size();
        double mediaGeral = todos.stream().mapToInt(Feedback::getNota).average().orElse(0.0);
        long positivos = todos.stream().filter(f -> f.getNota() >= 4).count();
        int satisfacaoPct = total > 0 ? (int) ((positivos * 100) / total) : 0;

        int[] estrelasCount = new int[5];
        for (Feedback f : todos) {
            if (f.getNota() >= 1 && f.getNota() <= 5) {
                estrelasCount[f.getNota() - 1]++;
            }
        }

        List<String> mesesLabels = new ArrayList<>();
        List<Double> mediasMensais = new ArrayList<>();
        LocalDate hoje = LocalDate.now();

        for (int i = 5; i >= 0; i--) {
            LocalDate mesRef = hoje.minusMonths(i);
            String nomeMes = mesRef.getMonth().getDisplayName(TextStyle.SHORT, new Locale("pt", "BR"));
            mesesLabels.add(nomeMes);
            double mediaMes = todos.stream()
                    .filter(f -> f.getCriadoEm().getMonth() == mesRef.getMonth() && f.getCriadoEm().getYear() == mesRef.getYear())
                    .mapToInt(Feedback::getNota)
                    .average()
                    .orElse(0.0);
            mediasMensais.add(mediaMes);
        }

        long countSugestao = todos.stream().filter(f -> "Sugestão".equalsIgnoreCase(f.getTipo())).count();
        long countReclamacao = todos.stream().filter(f -> "Reclamação".equalsIgnoreCase(f.getTipo())).count();

        model.addAttribute("total", total);
        model.addAttribute("media", String.format(Locale.US, "%.1f", mediaGeral));
        model.addAttribute("satisfacao", satisfacaoPct);
        model.addAttribute("estrelasData", estrelasCount);
        model.addAttribute("timelineLabels", mesesLabels);
        model.addAttribute("timelineData", mediasMensais);
        model.addAttribute("tiposData", List.of(countSugestao, countReclamacao));
    }
}