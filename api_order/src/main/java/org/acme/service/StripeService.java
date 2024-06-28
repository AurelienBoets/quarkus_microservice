package org.acme.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.LineItemCollection;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionListLineItemsParams;
import jakarta.inject.Singleton;
import order.OrderItem;
import order.Payment;

@Singleton
public class StripeService {

    private static final String STRIPE_API_KEY =
            "sk_test_51LXT08E7l5c5NjS32ioBp2okfcM39LAR2Tz5nj6n1ZIPRVl3FPmp2eReZj1FmSdWPdbptI1glR84jtomC5EWaOn600MLGICwza";

    public StripeService() {
        Stripe.apiKey = STRIPE_API_KEY;
    }

    public Session createSession(Payment request, String clientUrl) {
        SessionCreateParams.Builder paramsBuilder =
                SessionCreateParams.builder().setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl(clientUrl + "/success?s={CHECKOUT_SESSION_ID}")
                        .setCancelUrl(clientUrl + "/failure");

        for (OrderItem item : request.getOrderItemsList()) {
            BigDecimal unitPrice = BigDecimal.valueOf(item.getUnitPrice() * 100);
            paramsBuilder.addLineItem(SessionCreateParams.LineItem.builder().setQuantity(1L)
                    .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                            .setProductData(SessionCreateParams.LineItem.PriceData.ProductData
                                    .builder().putMetadata("app_id", item.getProductId())
                                    .putMetadata("platformId", item.getPlatformId())
                                    .setName(item.getProductName() + " sur "
                                            + item.getPlatformName())
                                    .build())
                            .setCurrency("eur")
                            .setUnitAmountDecimal(unitPrice.setScale(0, RoundingMode.HALF_UP))
                            .build())
                    .build());
        }

        try {
            return Session.create(paramsBuilder.build());
        } catch (StripeException error) {
            return null;
        }
    }

    public LineItemCollection retrieveSessionLineItems(String sessionId) throws StripeException {
        Session resource = Session.retrieve(sessionId);
        SessionListLineItemsParams params = SessionListLineItemsParams.builder().build();
        return resource.listLineItems(params);
    }

    public Session retrieveSession(String sessionId) throws StripeException {
        return Session.retrieve(sessionId);
    }
}
