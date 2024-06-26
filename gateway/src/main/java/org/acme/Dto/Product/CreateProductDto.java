package org.acme.dto.product;

import java.util.List;

import org.acme.dto.platform.SendPlatformDto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.*;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
@RegisterForReflection
public class CreateProductDto {
    private String name;
    private String description;
    private String img;
    private String formatImg;
    private List<String> category_id;
    private List<SendPlatformDto> platforms;
}
