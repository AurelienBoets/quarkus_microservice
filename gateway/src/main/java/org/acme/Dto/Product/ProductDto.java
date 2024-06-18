package org.acme.Dto.Product;

import java.util.List;

import org.acme.Dto.Category.CategoryDto;
import org.acme.Dto.Platform.PlatformProductDto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.*;


@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
@RegisterForReflection
public class ProductDto {
    private String id;
    private String name;
    private String description;
    private String img;
    private String formatImg;
    private List<CategoryDto> categories;
    private List<PlatformProductDto> platforms;
}
