import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { orderApi } from "../api/orderApi";
import OrderDetails from "../components/order/OrderDetails";

const OrderHistoric = () => {
  const [orders, setOrders] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    orderApi
      .getAll()
      .then((resp) => {
        setOrders(resp.data);
      })
      .catch((e) => {
        console.log(e);
      });
  }, []);

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-3xl font-semibold text-gray-800 mb-4">
        Historique des commandes
      </h1>
      {orders.length === 0 ? (
        <p className="text-gray-700">Aucune commande trouvée.</p>
      ) : (
        orders.map((order, index) => <OrderDetails key={index} order={order} />)
      )}
      <button
        onClick={() => navigate("/")}
        className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded mt-4"
      >
        Retour à l'accueil
      </button>
    </div>
  );
};

export default OrderHistoric;
