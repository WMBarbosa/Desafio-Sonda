package com.barbosa.taskmanager.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        final String schemeName = "bearerAuth";
        return new OpenAPI()
            .info(new Info()
                .title("Task Manager — API Back-end")
                .description("""
                    API REST para o sistema de gerenciamento de tarefas.

                    ## Funcionalidades
                    - Autenticação e autorização com JWT
                    - Controle de acesso por perfis/roles de usuário
                    - Cadastro, consulta, atualização e remoção de usuários
                    - Cadastro, consulta, atualização e remoção de tarefas
                    - Associação de tarefas a usuários
                    - Controle de status das tarefas
                    - Definição de prioridade das tarefas
                    - Validação de dados de entrada com DTOs
                    - Tratamento centralizado de exceções
                    - Respostas padronizadas para erros de validação, recursos não encontrados e falhas de banco de dados

                    ## Segurança
                    - API protegida com Bearer Token JWT
                    - Controle de permissões por usuário autenticado
                    - Configuração de Resource Server
                    - Configuração de Authorization Server
                    - Senhas armazenadas de forma segura com BCrypt

                    ## Recursos principais
                    - Usuários
                    - Tarefas
                    - Perfis de acesso
                    - Status de tarefas
                    - Prioridades de tarefas
                    """)
                .version("1.0.0")
                .contact(new Contact()
                    .name("Desafio Sonda - Wesley Barbosa")
                    .email("wesleymario01@gmail.com")))
            .addSecurityItem(new SecurityRequirement().addList(schemeName))
            .components(new Components()
                .addSecuritySchemes(schemeName, new SecurityScheme()
                    .name(schemeName)
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")));
    }
}
