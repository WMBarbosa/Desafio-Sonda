package com.barbosa.taskmanager.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserResquestDTO {

    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    private String name;

    @NotBlank(message = "O email é obrigatório")
    private String email;

    @NotNull(message = "A senha é obrigatória")
    private String password;

    @NotNull(message = "É obrigatorio as roles")
    private List<String> roles = new ArrayList<>();

}
