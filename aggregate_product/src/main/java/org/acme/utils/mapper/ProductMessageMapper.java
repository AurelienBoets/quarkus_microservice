package org.acme.utils.mapper;

import org.acme.dto.ProductMessageDto;
import org.acme.dto.ProductPlatformMessageDto;
import aggregate.ProductRequest;
import aggregate.SendPlatform;
import java.util.ArrayList;
import java.util.List;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductMessageMapper {
    public ProductRequest messageToGrpc(ProductMessageDto message) {
        return ProductRequest.newBuilder().setName(message.getName())
                .setDescription(message.getDescription()).addAllCategoryId(message.getCategoryId())
                .addAllPlatforms(messagePlatformToGrpcPlatform(message.getPlatforms()))
                .setFormatImg(message.getFormatImg()).setImg(message.getImg()).build();
    }

    private List<SendPlatform> messagePlatformToGrpcPlatform(
            List<ProductPlatformMessageDto> messageList) {
        List<SendPlatform> platforms = new ArrayList<>();
        for (ProductPlatformMessageDto message : messageList) {
            SendPlatform platform = SendPlatform.newBuilder().setId(message.getId())
                    .setPrice(message.getPrice()).build();
            platforms.add(platform);
        }
        return platforms;
    }
}
