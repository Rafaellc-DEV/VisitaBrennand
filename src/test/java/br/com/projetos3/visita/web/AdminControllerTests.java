package br.com.projetos3.visita.web;

import br.com.projetos3.visita.entity.Regra;
import br.com.projetos3.visita.service.RegraService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

// Importações estáticas
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RegraService regraService; // O AdminController depende do RegraService

    @Test
    @WithMockUser(roles = "ADMIN") // Simula um admin logado
    void testarSalvarRegrasValidas() throws Exception {
        // 1. Testa se o admin consegue salvar e se o ID=1 é forçado

        // 2. Executa a requisição POST com dados válidos
        mvc.perform(post("/admin/regras") // Rota de salvamento
                        .param("titulo", "Novas Regras de Visitação")
                        .param("conteudo", "<p>Não pode entrar de bicicleta.</p>")
                        .with(csrf()) // Adiciona o token de segurança
                )
                // 3. Verifica a Resposta
                .andExpect(status().is3xxRedirection()) // Espera um redirecionamento
                .andExpect(redirectedUrl("/admin/regras")) // Para a própria página de regras
                .andExpect(flash().attributeExists("success")); // Com a msg de sucesso

        // 4. Verifica se o RegraService foi chamado para SALVAR
        // Usamos o ArgumentCaptor para "capturar" o objeto que foi passado para o save()
        ArgumentCaptor<Regra> regraCaptor = ArgumentCaptor.forClass(Regra.class);
        verify(regraService).salvar(regraCaptor.capture());

        // 5. Verifica se o objeto salvo tem os dados corretos
        Regra regraSalva = regraCaptor.getValue();

        // Verifica o Requisito 2: O ID deve ser sempre 1L (long)
        // A sua RegraService.java força isso
        assertThat(regraSalva.getId()).isEqualTo(1L);

        // Verifica o Requisito 1: O conteúdo foi salvo
        assertThat(regraSalva.getTitulo()).isEqualTo("Novas Regras de Visitação");
        assertThat(regraSalva.getConteudo()).isEqualTo("<p>Não pode entrar de bicicleta.</p>");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testarSalvarRegrasInvalidas_TituloEmBranco() throws Exception {
        // 1. Testa o salvamento de regras inválidas (título em branco)
        // A entidade Regra define o título como @NotBlank

        // 2. Executa a requisição POST com título vazio
        mvc.perform(post("/admin/regras")
                        .param("titulo", "") // Título vazio (INVÁLIDO)
                        .param("conteudo", "<p>Conteúdo ok.</p>")
                        .with(csrf())
                )
                // 3. Verifica a Resposta
                .andExpect(status().isOk()) // NÃO deve redirecionar (status 200)
                .andExpect(view().name("admin/regras")) // Deve mostrar a página de regras novamente
                .andExpect(model().attributeExists("error")) // Deve conter a msg de erro
                .andExpect(model().hasErrors()); // Confirma que houve um erro de validação

        // 4. Verifica se o RegraService NUNCA foi chamado
        verify(regraService, never()).salvar(any(Regra.class));
    }
}