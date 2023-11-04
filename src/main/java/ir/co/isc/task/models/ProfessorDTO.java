package ir.co.isc.task.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfessorDTO {
    private Long id;

    @NotNull
    @NotBlank
    @Size(max = 50)
    private String firstName;

    @NotNull
    @NotBlank
    @Size(max = 50)
    private String lastName;

    @NotNull
    @NotBlank
    @Pattern(regexp = "^\\d{10}$")
    private String nationalId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
