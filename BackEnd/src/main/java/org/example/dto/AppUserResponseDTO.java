package org.example.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUserResponseDTO {
    private Long id;
    private String username;
    private String role;
}
