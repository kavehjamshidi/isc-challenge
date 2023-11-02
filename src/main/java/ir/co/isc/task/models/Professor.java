package ir.co.isc.task.models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class Professor {
    private Long id;
    private String firstName;
    private String lastName;
    private String nationalId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
