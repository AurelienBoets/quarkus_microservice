const OrderItem = ({ item }) => {
  return (
    <div className="border p-4 rounded-md mb-2 shadow-sm">
      <p className="text-lg font-semibold text-gray-800">{item.productName}</p>
      <p className="text-gray-600">
        <strong>Plateforme:</strong> {item.platformName}
      </p>
      <p className="text-gray-600">
        <strong>Prix:</strong> {item.unitPrice}€
      </p>
    </div>
  );
};

export default OrderItem;
