package br.com.projetos3.visita.web;

import br.com.projetos3.visita.entity.ComoChegar;
import br.com.projetos3.visita.service.ComoChegarService;
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
public class AdminComoChegarControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ComoChegarService comoChegarService;

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Deve carregar a página de edição 'Como Chegar' corretamente")
    void testarCarregarPaginaComoChegar() throws Exception {
        // Mock do serviço retornando um objeto vazio ou preenchido
        when(comoChegarService.getComoChegar()).thenReturn(new ComoChegar());

        mvc.perform(get("/admin/como-chegar"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/como_chegar"))
                .andExpect(model().attributeExists("comoChegar"))
                .andExpect(model().attribute("pageTitle", "Editar Como Chegar"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Deve salvar informações de 'Como Chegar' válidas com sucesso")
    void testarSalvarComoChegarValido() throws Exception {
        // Executa o POST com todos os campos obrigatórios preenchidos
        mvc.perform(post("/admin/como-chegar")
                        // Bloco Barco
                        .param("barcoDescricao", "Saída do Marco Zero")
                        .param("barcoPreco", "R$ 10,00")
                        .param("barcoTempo", "15 min")
                        .param("barcoBike", "Aceita bicicleta")
                        // Bloco Carro
                        .param("carroDescricao", "Via Brasília Teimosa")
                        .param("carroPasso1", "Seguir pela orla")
                        .param("carroPasso2", "Entrar na rua do porto")
                        .param("carroPasso3", "Estacionar no final")
                        // Bloco Catamarã
                        .param("catamaraDescricao", "Passeio pelo rio")
                        .param("catamaraLink", "https://catamara.com.br")
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection()) // Deve redirecionar após sucesso
                .andExpect(redirectedUrl("/admin/como-chegar"))
                .andExpect(flash().attributeExists("success"));

        // Verifica se o serviço foi chamado com os dados corretos
        ArgumentCaptor<ComoChegar> captor = ArgumentCaptor.forClass(ComoChegar.class);
        verify(comoChegarService).salvar(captor.capture());

        ComoChegar salvo = captor.getValue();

        // Validações pontuais
        assertThat(salvo.getBarcoDescricao()).isEqualTo("Saída do Marco Zero");
        assertThat(salvo.getCarroPasso1()).isEqualTo("Seguir pela orla");
        assertThat(salvo.getCatamaraLink()).isEqualTo("https://catamara.com.br");

        // Garante que o ID é 1 (Singleton)
        assertThat(salvo.getId()).isEqualTo(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Não deve salvar se campos obrigatórios estiverem vazios")
    void testarSalvarComoChegarInvalido() throws Exception {
        // Tenta salvar com campos em branco (o que viola @NotBlank na entidade)
        mvc.perform(post("/admin/como-chegar")
                        .param("barcoDescricao", "") // Vazio (Erro)
                        .param("barcoPreco", "")
                        .param("carroDescricao", "Descrição válida")
                        .param("catamaraLink", "")   // Vazio (Erro)
                        .with(csrf())
                )
                .andExpect(status().isOk()) // Retorna 200 (não redireciona) para mostrar erros
                .andExpect(view().name("admin/como_chegar"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("comoChegar", "barcoDescricao"))
                .andExpect(model().attributeHasFieldErrors("comoChegar", "catamaraLink"));

        // Garante que o serviço NUNCA foi chamado para salvar
        verify(comoChegarService, never()).salvar(any(ComoChegar.class));
    }

    @Test
    @DisplayName("Deve negar acesso a usuários anônimos")
    void testarAcessoNegadoSemLogin() throws Exception {
        mvc.perform(get("/admin/como-chegar"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }
}