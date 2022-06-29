package cocktail.dto;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SuccessResponseDto {
    private boolean success;

    public SuccessResponseDto(boolean success) {
        this.success = success;
    }
}
