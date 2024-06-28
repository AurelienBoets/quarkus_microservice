import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import OrderItem from "../components/order/OrderItem";
import { orderApi } from "../api/orderApi";

const PaymentSuccess = () => {
  const [orderDetails, setOrderDetails] = useState(null);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();
  const search = new URLSearchParams(window.location.search);
  const stripeId = search.get("s");

  useEffect(() => {
    orderApi
      .create(stripeId)
      .then((resp) => {
        setLoading(false);
        localStorage.removeItem("cart");
        setOrderDetails(resp.data);
      })
      .catch((e) => {
        console.log(e);
        navigate("/");
      });
  }, [navigate, stripeId]);

  if (loading) {
    return (
      <div className="container mx-auto p-4">
        <h1 className="text-3xl font-semibold text-gray-800 mb-4">
          Paiement réussi
        </h1>
        <p className="text-gray-700">
          Chargement des détails de la commande...
        </p>
      </div>
    );
  }

  if (!orderDetails) {
    navigate("/");
    return null;
  }

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-3xl font-semibold text-gray-800 mb-4">
        Paiement réussi
      </h1>
      <p className="text-gray-700">Merci pour votre achat !</p>
      <div className="mt-4">
        <h2 className="text-2xl font-semibold text-gray-800">
          Détails de la commande
        </h2>
        <p className="text-gray-700">
          Montant total: {orderDetails.totalAmount}€
        </p>
        <div className="mt-2 grid grid-cols-1 gap-4">
          {orderDetails.items.map((item, index) => (
            <OrderItem key={index} item={item} />
          ))}
        </div>
      </div>
      <button
        onClick={() => navigate("/")}
        className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded mt-4"
      >
        Retour à l'accueil
      </button>
    </div>
  );
};

export default PaymentSuccess;
