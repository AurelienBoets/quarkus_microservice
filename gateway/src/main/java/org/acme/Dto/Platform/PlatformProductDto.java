package org.acme.Dto.Platform;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@RegisterForReflection
public class PlatformProductDto {
    private String id;
    private String name;
    private double price;
}
