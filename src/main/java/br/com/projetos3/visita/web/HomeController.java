package br.com.projetos3.visita.web;

import br.com.projetos3.visita.entity.*;
import br.com.projetos3.visita.service.*;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private final AvisoService avisoService;
    private final FeedbackService feedbackService;
    private final RegraService regraService;
    private final ComoChegarService comoChegarService;
    private final HorarioService horarioService; // Nova injeção

    public HomeController(AvisoService a, FeedbackService f, RegraService r,
                          ComoChegarService c, HorarioService h){
        this.avisoService = a;
        this.feedbackService = f;
        this.regraService = r;
        this.comoChegarService = c;
        this.horarioService = h;
    }

    private void loadCalendarData(Model model, String baseDateStr, Locale locale) {
        LocalDate baseDate;
        try {
            baseDate = LocalDate.parse(baseDateStr);
        } catch (DateTimeParseException | NullPointerException e) {
            baseDate = LocalDate.now();
        }

        LocalDate hoje = LocalDate.now();
        LocalDate startDate = baseDate.minusDays(3);
        LocalDate endDate = baseDate.plusDays(3);
        Set<LocalDate> avisoDates = avisoService.findDatesWithAvisos(startDate, endDate);

        List<CalendarDayDTO> calendarDays = new ArrayList<>();
        for (int i = -3; i <= 3; i++) {
            LocalDate currentDay = baseDate.plusDays(i);
            CalendarDayDTO dto = new CalendarDayDTO();
            dto.setDate(currentDay);
            dto.setToday(currentDay.isEqual(hoje));
            dto.setBaseDate(currentDay.isEqual(baseDate));
            dto.setClosed(currentDay.getDayOfWeek() == DayOfWeek.MONDAY);
            dto.setHasAviso(avisoDates.contains(currentDay));
            calendarDays.add(dto);
        }

        DateTimeFormatter formatadorMes = DateTimeFormatter.ofPattern("MMMM yyyy", locale);
        String nomeMes = baseDate.format(formatadorMes);
        nomeMes = nomeMes.substring(0, 1).toUpperCase() + nomeMes.substring(1);

        model.addAttribute("avisos", avisoService.porData(baseDate));
        model.addAttribute("hoje", hoje);
        model.addAttribute("baseDate", baseDate);
        model.addAttribute("calendarDays", calendarDays);
        model.addAttribute("nomeMes", nomeMes);
    }

    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(value="ok", required=false) String ok,
                        @RequestParam(value="erro", required=false) String erro,
                        @RequestParam(value="date", required=false) String baseDateStr,
                        Locale locale) {

        loadCalendarData(model, baseDateStr, locale);
        model.addAttribute("regra", regraService.getRegra());
        model.addAttribute("comoChegar", comoChegarService.getComoChegar());

        // NOVO: Carrega os horários dinâmicos
        model.addAttribute("horario", horarioService.getHorario());

        if (!model.containsAttribute("feedback")) {
            model.addAttribute("feedback", new Feedback());
        }
        model.addAttribute("ok", ok);
        model.addAttribute("erro", erro);

        return "home/index";
    }

    @GetMapping("/api/calendar")
    public String getCalendarFragment(Model model,
                                      @RequestParam("date") String date,
                                      Locale locale) {
        loadCalendarData(model, date, locale);
        return "home/index :: calendar-fragment";
    }

    @PostMapping("/feedback")
    public String enviarFeedback(@Valid @ModelAttribute("feedback") Feedback feedback,
                                 BindingResult br,
                                 RedirectAttributes ra) {
        if (br.hasErrors()) {
            String erros = br.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.joining(", "));

            ra.addFlashAttribute("erro", "Erro ao enviar: " + erros);
            ra.addFlashAttribute("feedback", feedback);
            ra.addFlashAttribute("org.springframework.validation.BindingResult.feedback", br);
            return "redirect:/#feedback-form";
        }
        feedbackService.salvar(feedback);
        ra.addFlashAttribute("ok","home.feedback.ok");
        return "redirect:/#feedback-form";
    }

    @GetMapping("/api/avisos")
    @ResponseBody
    public List<Aviso> getAvisosPorData(
            @RequestParam("data") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return avisoService.porData(data);
    }

    @GetMapping("/login")
    public String login() {
        return "admin/login";
    }
}