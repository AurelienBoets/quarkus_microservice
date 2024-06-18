import { useEffect, useState } from "react";
import { productApi } from "../api/productApi";
import { useParams } from "react-router-dom";
import { cart } from "../helper/cart";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

const ProductView = () => {
  const { id } = useParams();
  const [product, setProduct] = useState({
    id: "",
    name: "",
    description: "",
    img: "",
    categories: [{ id: "", name: "" }],
    platforms: [{ id: "", name: "", price: 0 }],
  });
  const [price, setPrice] = useState(0);
  const [selectedPlatform, setSelectedPlatform] = useState(
    product.platforms[0]
  );
  const [isInCart, setIsInCart] = useState(false);

  const handlePrice = (platform) => {
    setPrice(platform.price);
    setSelectedPlatform(platform);
    const selectedProduct = {
      id: product.id,
      platforms: [{ id: platform.id }],
    };

    setIsInCart(cart.isAlreadyInCart(selectedProduct));
  };

  useEffect(() => {
    productApi
      .getById(id)
      .then((resp) => {
        setProduct(resp.data);
        setPrice(resp.data.platforms[0].price);
        setSelectedPlatform(resp.data.platforms[0]);
        const selectedProduct = {
          id: id,
          platforms: [{ id: resp.data.platforms[0].id }],
        };
        setIsInCart(cart.isAlreadyInCart(selectedProduct));
      })
      .catch((e) => {
        console.log(e);
      });
  }, [id]);

  const addToCart = (product) => {
    const selectedProduct = {
      id: product.id,
      platforms: [{ id: selectedPlatform.id }],
    };
    if (isInCart) {
      cart.removeInCart(selectedProduct);
      setIsInCart(false);
      toast.info("Produit retiré du panier");
    } else {
      cart.addInCart(selectedProduct);
      setIsInCart(true);
      toast.success("Produit ajouté au panier");
    }
  };

  return (
    <div className="container mx-auto p-4">
      <ToastContainer />
      <div className="max-w-3xl mx-auto bg-white shadow-lg rounded-lg overflow-hidden flex justify-between mt-4">
        <div className="relative w-1/2 p-6 bg-gray-100">
          <img
            src={`data:image/webp;base64, ${product.img}`}
            alt={product.name}
            className="w-full h-auto object-cover"
          />
          <div className="absolute bottom-4 left-5 bg-blue-500 bg-opacity-75 text-white rounded-lg px-4 py-2">
            <span className="text-xl font-semibold">{price}€</span>
          </div>
        </div>
        <div className="p-6 w-1/2 bg-gray-100">
          <h1 className="text-3xl font-semibold text-gray-800 mb-4">
            {product.name}
          </h1>
          <div>
            <h2 className="text-lg font-semibold mb-2 text-gray-600">
              Categories:
            </h2>
            <div className="flex flex-wrap">
              {product.categories.map((category, index) => (
                <span
                  key={index}
                  className="mr-2 mb-2 px-2 py-1 bg-blue-100 text-blue-800 rounded-full text-sm"
                >
                  {category.name}
                </span>
              ))}
            </div>
          </div>
          <div>
            <h2 className="text-lg font-semibold mt-4 mb-2 text-gray-600">
              Platforms:
            </h2>
            {product.platforms.map((platform, index) => (
              <div
                key={index}
                className={`mb-2 cursor-pointer px-2 py-1 rounded-full text-sm mr-2 ${
                  selectedPlatform.id === platform.id
                    ? "bg-gray-500 text-white"
                    : "bg-gray-300 text-gray-800"
                }`}
                onClick={() => handlePrice(platform)}
              >
                {platform.name}
              </div>
            ))}
          </div>
          <span className="text-lg font-semibold mt-4 mb-2 text-gray-600">
            Description:
          </span>
          <p className="text-gray-700 mt-4">{product.description}</p>
          <button
            className={`${
              isInCart
                ? "bg-red-500 hover:bg-red-700"
                : "bg-blue-500 hover:bg-blue-700"
            } text-white font-bold py-2 px-4 rounded mt-4`}
            onClick={() => addToCart(product)}
          >
            {isInCart ? "Retirer du panier" : "Ajouter au panier"}
          </button>
        </div>
      </div>
    </div>
  );
};

export default ProductView;
