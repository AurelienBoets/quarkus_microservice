package org.acme.dto.category;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@RegisterForReflection
@Data
public class CategoryDto {
    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
}
