package ir.co.isc.task.models;


import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class CourseDTO {
    private Long id;

    @NotNull
    @NotBlank
    @Size(max = 50)
    private String title;

    @NotNull
    @Positive
    private Integer capacity;

    @NotNull
    private Long professorId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
