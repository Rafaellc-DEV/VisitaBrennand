package br.com.projetos3.visita.web;

import br.com.projetos3.visita.entity.Horario;
import br.com.projetos3.visita.service.HorarioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminHorarioControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private HorarioService horarioService; // Mockamos o serviço que lida com Horários

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Deve carregar a página de edição de horários corretamente")
    void testarCarregarPaginaHorarios() throws Exception {
        // Configura o mock para retornar um objeto vazio ou padrão quando solicitado
        when(horarioService.getHorario()).thenReturn(new Horario());

        mvc.perform(get("/admin/horarios"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/horarios"))
                .andExpect(model().attributeExists("horario"))
                .andExpect(model().attribute("pageTitle", "Editar Horários"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Deve salvar horários válidos com sucesso")
    void testarSalvarHorariosValidos() throws Exception {
        // Dados válidos para envio
        String tituloSemana = "Segunda a Sexta";
        String horarioSemana = "08:00 às 17:00"; // Formato válido com HH:mm
        String tituloFimDeSemana = "Finais de Semana";
        String horarioFimDeSemana = "09:00 às 12:00"; // Formato válido com HH:mm

        mvc.perform(post("/admin/horarios")
                        .param("tituloSemana", tituloSemana)
                        .param("horarioSemana", horarioSemana)
                        .param("tituloFimDeSemana", tituloFimDeSemana)
                        .param("horarioFimDeSemana", horarioFimDeSemana)
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection()) // Deve redirecionar após sucesso
                .andExpect(redirectedUrl("/admin/horarios"))
                .andExpect(flash().attributeExists("success"));

        // Captura o objeto salvo para verificar os valores
        ArgumentCaptor<Horario> horarioCaptor = ArgumentCaptor.forClass(Horario.class);
        verify(horarioService).salvar(horarioCaptor.capture());

        Horario salvo = horarioCaptor.getValue();
        assertThat(salvo.getTituloSemana()).isEqualTo(tituloSemana);
        assertThat(salvo.getHorarioSemana()).isEqualTo(horarioSemana);
        assertThat(salvo.getTituloFimDeSemana()).isEqualTo(tituloFimDeSemana);
        assertThat(salvo.getHorarioFimDeSemana()).isEqualTo(horarioFimDeSemana);
        // Verifica se o ID é mantido como 1L (Singleton)
        assertThat(salvo.getId()).isEqualTo(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Não deve salvar horários com formato de hora inválido")
    void testarSalvarHorariosInvalidos() throws Exception {
        // O regex na entidade exige dois dígitos, dois pontos, dois dígitos (ex: 09:00)
        String horarioInvalido = "Aberto o dia todo";

        mvc.perform(post("/admin/horarios")
                        .param("tituloSemana", "Dias Úteis")
                        .param("horarioSemana", horarioInvalido) // INVÁLIDO
                        .param("tituloFimDeSemana", "Fim de Semana")
                        .param("horarioFimDeSemana", "08:00 às 12:00")
                        .with(csrf())
                )
                .andExpect(status().isOk()) // Retorna 200 (não redireciona) pois houve erro
                .andExpect(view().name("admin/horarios"))
                .andExpect(model().hasErrors()) // Verifica se o Spring Validation pegou o erro
                .andExpect(model().attributeHasFieldErrors("horario", "horarioSemana"));

        // Garante que o serviço de salvar NUNCA foi chamado
        verify(horarioService, never()).salvar(any(Horario.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Não deve salvar se campos obrigatórios estiverem vazios")
    void testarSalvarCamposVazios() throws Exception {
        mvc.perform(post("/admin/horarios")
                        .param("tituloSemana", "") // Vazio
                        .param("horarioSemana", "") // Vazio
                        .param("tituloFimDeSemana", "") // Vazio
                        .param("horarioFimDeSemana", "") // Vazio
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(view().name("admin/horarios"))
                .andExpect(model().hasErrors());

        verify(horarioService, never()).salvar(any(Horario.class));
    }

    @Test
    @DisplayName("Deve negar acesso a usuários não autenticados")
    void testarAcessoNegadoSemLogin() throws Exception {
        mvc.perform(get("/admin/horarios"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }
}