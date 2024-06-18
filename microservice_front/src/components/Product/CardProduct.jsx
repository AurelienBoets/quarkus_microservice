import { useNavigate } from "react-router-dom";

const CardProduct = (props) => {
  const navigate = useNavigate();
  const { name, img, formatImg, price, platform, id } = props;

  return (
    <div className="p-4">
      <div className="max-w-sm shadow-md hover:shadow-lg transition-shadow bg-gray-800 border border-gray-700 rounded-lg overflow-hidden">
        <img
          onClick={() => {
            navigate(`/product/${id}`);
          }}
          className="cursor-pointer w-full h-48 object-cover transition-transform transform hover:scale-105"
          src={`data:${formatImg};base64, ${img}`}
          alt={name}
        />

        <div className="p-5">
          <div className="flex justify-between items-center">
            <div className="text-lg font-semibold text-white">
              {name} sur {platform}
            </div>

            <div className="text-lg font-bold text-white">{price}€</div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CardProduct;
