package org.acme.utils;

import com.stripe.Stripe;
import com.stripe.model.Address;
import com.stripe.model.LineItemCollection;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.model.StripeCollection;
import com.stripe.model.StripeError;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionCreateParams.LineItem;
import com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData;
import com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData.ProductData;
import com.stripe.param.checkout.SessionListLineItemsParams;
import com.stripe.param.checkout.SessionRetrieveParams;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection(targets = {StripeError.class, Session.class, SessionCreateParams.class,
                Session.class, Stripe.class, Address.class, LineItem.class,
                LineItemCollection.class, SessionListLineItemsParams.class,
                SessionRetrieveParams.class, com.stripe.model.LineItem.class, PriceData.class,
                ProductData.class, com.stripe.param.PriceCreateParams.ProductData.class,
                StripeCollection.class, Price.class, Product.class})
public class Reflection {

}
