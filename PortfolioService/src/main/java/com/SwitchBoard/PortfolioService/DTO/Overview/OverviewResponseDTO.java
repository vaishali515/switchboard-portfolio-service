package com.SwitchBoard.PortfolioService.DTO.Overview;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Overview response payload")
public class OverviewResponseDTO {

    @Schema(description = "Unique identifier of the overview", example = "1")
    private UUID id;

    @Schema(description = "Brief overview or introduction of the user", example = "Highly motivated software engineer skilled in backend development.")
    private String overview;
}
