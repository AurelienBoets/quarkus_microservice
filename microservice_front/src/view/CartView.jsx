import { useEffect, useState } from "react";
import { cart } from "../helper/cart";
import { productApi } from "../api/productApi";
import CartProduct from "../components/cart/CartProduct";
import { orderApi } from "../api/orderApi";

const CartView = () => {
  const [products, setProducts] = useState([]);
  const [total, setTotal] = useState(0);

  useEffect(() => {
    const productCart = cart.getProducts();
    const tab = [];
    let totalPrice = 0;

    const fetchProducts = async () => {
      for (const p of productCart) {
        try {
          const resp = await productApi.getById(p.id);
          let product = resp.data;
          product.platforms = product.platforms.filter(
            (platform) => platform.id == p.platforms
          );
          tab.push(product);
          console.log(product);
          totalPrice += product.platforms[0].price;
          console.log(totalPrice);
        } catch (e) {
          console.log(e);
        }
      }
      setProducts(tab);
      setTotal(totalPrice);
    };

    fetchProducts();
  }, []);

  const removeInCart = (value) => {
    cart.removeInCart(value);
    const updatedProducts = products.filter(
      (product) =>
        product.id !== value.id ||
        product.platforms[0].id !== value.platforms[0].id
    );
    setProducts(updatedProducts);

    const newTotal = updatedProducts.reduce(
      (sum, product) => sum + product.platforms[0].price,
      0
    );
    setTotal(newTotal);
  };

  const handlePayment = () => {
    let orderItems = [];
    products.forEach((product) => {
      let item = {
        product_id: product.id,
        product_name: product.name,
        unitPrice: product.platforms[0].price,
        platformId: product.platforms[0].id,
        platformName: product.platforms[0].name,
      };
      orderItems.push(item);
    });
    orderApi
      .payment({
        totalAmount: total,
        items: orderItems,
      })
      .then((resp) => {
        localStorage.setItem(
          "temp",
          JSON.stringify({
            totalAmount: total,
            items: orderItems,
          })
        );
        window.location.replace(resp.data);
      })
      .catch((e) => {
        console.log(e);
      });
  };

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-3xl font-semibold text-gray-800 mb-4">Mon Panier</h1>
      {products.length > 0 ? (
        <>
          <div className="mb-4">
            <button
              className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded"
              onClick={handlePayment}
            >
              Payer ({total}€)
            </button>
          </div>
          {products.map((product) => (
            <CartProduct
              key={product.id + product.platforms[0].name}
              product={product}
              removeInCart={removeInCart}
            />
          ))}
        </>
      ) : (
        <p className="text-gray-700">Votre panier est vide.</p>
      )}
    </div>
  );
};

export default CartView;
