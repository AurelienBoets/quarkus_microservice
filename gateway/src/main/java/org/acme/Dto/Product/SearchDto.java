package org.acme.dto.product;

import java.util.List;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.*;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
@RegisterForReflection
public class SearchDto {
    private List<ProductDto> products;
    private int page;
}
