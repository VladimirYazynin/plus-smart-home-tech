package ru.practicum.commerce.shoppingstore.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.practicum.commerce.shoppingstore.enums.ProductCategory;
import ru.practicum.commerce.shoppingstore.enums.ProductState;
import ru.practicum.commerce.shoppingstore.enums.QuantityState;

import java.math.BigDecimal;
import java.util.UUID;

public class ProductDto {

    private UUID productId;

    @NotBlank(message = "Наименование товара не может быть пустым")
    @Size(max = 256, message = "Наименование товара должно содержать до 256 символов")
    private String productName;

    @NotBlank(message = "Описание товара не может быть пустым")
    @Size(max = 512, message = "Описание товара должно содержать до 512 символов")
    private String description;

    @Size(max = 256, message = "Длина ссылки на картинку должна быть до 256 символов")
    private String imageSrc;

    @NotNull(message = "Количество товара должно быть указано")
    private QuantityState quantityState;

    @NotNull(message = "Статус товара должен быть укзаан")
    private ProductState productState;

    @NotNull(message = "Категория товара должна быть указана")
    private ProductCategory productCategory;

    @NotNull(message = "Необходимо указать цену товара")
    @DecimalMin(value = "1.00", message = "Минимальная стоимость товара 1 рубль")
    private BigDecimal price;
}
