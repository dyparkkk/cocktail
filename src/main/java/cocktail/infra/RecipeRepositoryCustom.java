package cocktail.infra;

import org.springframework.data.domain.Pageable;

import java.util.List;

import static cocktail.dto.RecipeResponseDto.*;

public interface RecipeRepositoryCustom {

    List<RecipeListDto> findAllListDto(Pageable pageable);
}
