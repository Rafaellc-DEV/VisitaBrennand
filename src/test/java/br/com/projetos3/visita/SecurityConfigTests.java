package br.com.projetos3.visita;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigTests {

    @Autowired
    private MockMvc mvc;

    @Test
    void testaAcessoAdminSemAutenticacao() throws Exception {
        // 1. Tenta acessar uma rota /admin (ex: /admin/avisos) sem estar logado
        mvc.perform(get("/admin/avisos"))
                .andExpect(status().is3xxRedirection()) // Espera um redirecionamento
                .andExpect(redirectedUrl("http://localhost/login")); // Espera ser redirecionado para /login
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN") // 2. Simula um usuário logado com ROLE_ADMIN
    void testaAcessoAdminComUsuarioAdmin() throws Exception {
        // Tenta acessar a mesma rota /admin/avisos
        mvc.perform(get("/admin/avisos"))
                .andExpect(status().isOk()); // Espera acesso permitido (HTTP 200)
    }

    @Test
    void testaAcessoPublicoPaginaHome() throws Exception {
        // 3. Tenta acessar a rota pública /
        mvc.perform(get("/"))
                .andExpect(status().isOk()); // Espera acesso permitido
    }

    @Test
    void testaAcessoPublicoPaginaLogin() throws Exception {
        // 4. Tenta acessar a rota pública /login
        mvc.perform(get("/login"))
                .andExpect(status().isOk()); // Espera acesso permitido
    }
}