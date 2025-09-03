package co.com.crediya.model.login;

import lombok.Builder;

@Builder
public record LoginDTO(String identityDocument,
                       String password) {}
