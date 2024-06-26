package org.acme.dto.product;

import java.util.List;

import org.acme.dto.category.CategoryDto;
import org.acme.dto.platform.PlatformProductDto;

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
