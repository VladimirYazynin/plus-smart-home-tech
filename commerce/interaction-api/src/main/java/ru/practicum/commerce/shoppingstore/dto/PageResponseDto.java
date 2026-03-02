package ru.practicum.commerce.shoppingstore.dto;

import java.util.List;

public record PageResponseDto<T>(
        int totalPages,
        long totalElements,
        int size,
        List<T> content,
        int number,
        List<SortOrderDto> sort,
        boolean first,
        boolean last,
        int numberOfElements,
        boolean empty) {

}
