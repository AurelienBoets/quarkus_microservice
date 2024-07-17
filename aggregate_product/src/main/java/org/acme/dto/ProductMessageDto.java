package org.acme.dto;

import java.util.List;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.*;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
@RegisterForReflection

public class ProductMessageDto {
    private String name;
    private String description;
    private String img;
    private String formatImg;
    private List<String> categoryId;
    private List<ProductPlatformMessageDto> platforms;
}

