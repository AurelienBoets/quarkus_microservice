import OrderItem from "./OrderItem";

const OrderDetails = ({ order }) => {
  return (
    <div className="p-4 border rounded-lg shadow-lg mb-4">
      <h2 className="text-xl font-semibold">Commande #{order.id}</h2>
      <p>Date de commande: {new Date(order.orderDate).toLocaleDateString()}</p>
      <p>Montant total: {order.totalAmount}€</p>
      <h3 className="text-lg font-semibold mt-4">Articles</h3>
      <div className="mt-2">
        {order.items.map((item, index) => (
          <OrderItem key={index} item={item} />
        ))}
      </div>
    </div>
  );
};

export default OrderDetails;
