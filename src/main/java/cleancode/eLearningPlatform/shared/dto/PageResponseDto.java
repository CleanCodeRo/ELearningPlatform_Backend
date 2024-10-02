package cleancode.eLearningPlatform.shared.dto;

import lombok.Builder;

@Builder
public record PageResponseDto<T>(T data, Long totalItems, Integer currentPage, Integer totalPages) {
}
