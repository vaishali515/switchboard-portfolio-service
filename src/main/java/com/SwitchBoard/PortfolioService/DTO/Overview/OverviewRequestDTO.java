package com.SwitchBoard.PortfolioService.DTO.Overview;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Overview creation/update request payload")
public class OverviewRequestDTO {

    @NotBlank(message = "Overview content is required")
    @Schema(description = "Brief overview or introduction of the user", example = "Highly motivated software engineer skilled in backend development.")
    private String overview;
}
