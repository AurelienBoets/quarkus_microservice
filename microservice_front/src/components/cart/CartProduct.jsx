import { useNavigate } from "react-router-dom";

const CartProduct = (props) => {
  const { product, removeInCart } = props;
  const navigate = useNavigate();

  return (
    <div className="flex items-start p-4 border-b border-gray-300 md:flex-row md:items-center md:space-x-4">
      <div className="w-1/2 md:w-1/6 mb-4 md:mb-0">
        <img
          src={`data:${product.formatImg};base64, ${product.img}`}
          alt={product.name}
          className="w-full h-auto md:h-full object-cover rounded cursor-pointer"
          onClick={() => navigate(`/product/${product.id}`)}
        />
      </div>
      <div className="w-1/2 md:w-5/6 md:pl-4">
        <div className="flex flex-col justify-between h-full">
          <div>
            <h3 className="text-lg md:text-xl font-semibold text-gray-800 mb-1">
              {product.name}
            </h3>
            <p className="text-gray-600 mb-2">{product.platforms[0].name}</p>
          </div>
          <div className="flex justify-between items-center">
            <span className="text-lg font-semibold text-gray-800">
              {product.platforms[0].price}€
            </span>
            <span
              onClick={(e) => {
                e.preventDefault();
                removeInCart(product);
              }}
              className="cursor-pointer text-red-600"
            >
              <i className="fa-solid fa-minus"></i>
            </span>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CartProduct;
