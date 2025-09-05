package co.com.crediya.api.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record UserApplicationsRequest (
    @NotEmpty(message = "documents cannot be empty")
    List<String> documents
){
}
