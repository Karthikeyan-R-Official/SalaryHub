
package com.salaryhub;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityApiIntegrationTest {

    @org.springframework.beans.factory.annotation.Autowired
    private MockMvc mockMvc;

    @Value("${HR_MANAGER_USERNAME}")
    private String username;

    @Value("${HR_MANAGER_PASSWORD}")
    private String password;

    @Test
    void shouldLoginWithValidCredentials() throws Exception {

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.role").value("HR_MANAGER"));
    }

    @Test
    void shouldRejectInvalidCredentials() throws Exception {

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "%s",
                                    "password": "IncorrectPassword123!"
                                }
                                """.formatted(username)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRejectProtectedApiWithoutJwt() throws Exception {

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldAllowProtectedApiWithValidJwt() throws Exception {

        String token = obtainAccessToken();

        mockMvc.perform(get("/api/employees")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void shouldAllowHealthEndpointWithoutAuthentication() throws Exception {

        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk());
    }

    private String loginRequest() {
        return """
                {
                    "username": "%s",
                    "password": "%s"
                }
                """.formatted(username, password);
    }

    private String obtainAccessToken() throws Exception {

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest()))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        Pattern pattern = Pattern.compile(
                "\"accessToken\"\\s*:\\s*\"([^\"]+)\""
        );

        Matcher matcher = pattern.matcher(responseBody);

        if (!matcher.find()) {
            throw new IllegalStateException(
                    "Access token not found in login response"
            );
        }

        return matcher.group(1);
    }
}
