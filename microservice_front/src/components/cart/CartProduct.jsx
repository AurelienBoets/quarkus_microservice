import { useNavigate } from "react-router-dom";

const CartProduct = (props) => {
  const { product, removeInCart } = props;
  const navigate = useNavigate();

  return (
    <div className="flex items-center p-4 border-b border-gray-300 ">
      <div className="w-1/6">
        <img
          src={`data:image/webp;base64, ${product.img}`}
          alt={product.name}
          className="w-full h-auto object-cover rounded cursor-pointer"
          onClick={() => navigate(`/product/${id}`)}
        />
      </div>
      <div className="w-4/6 pl-4">
        <h3 className="text-xl font-semibold text-gray-800">{product.name}</h3>
        <p className="text-gray-600">{product.platforms[0].name}</p>
      </div>
      <div className="w-1/6 text-right">
        <span className="text-lg font-semibold text-gray-800">
          {product.platforms[0].price}€
        </span>
      </div>
      <span
        onClick={(e) => {
          e.preventDefault();
          removeInCart(product);
        }}
      >
        <i className="fa-solid fa-minus text-red-600 ml-4 cursor-pointer"></i>
      </span>
    </div>
  );
};

export default CartProduct;
