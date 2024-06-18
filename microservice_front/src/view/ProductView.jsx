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
      <div className="max-w-3xl mx-auto bg-white shadow-lg rounded-lg overflow-hidden mt-4">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4 p-4">
          <div className="relative w-full h-80 md:h-auto">
            <img
              src={`data:${product.formatImg};base64, ${product.img}`}
              alt={product.name}
              className="w-full h-full object-cover rounded-lg"
            />
            <div className="absolute bottom-4 left-4 md:bottom-2 md:left-2 bg-blue-500 bg-opacity-75 text-white rounded-lg px-4 py-2">
              <span className="text-lg md:text-xl font-semibold">{price}€</span>
            </div>
          </div>
          <div>
            <h1 className="text-3xl font-semibold text-gray-800 mb-4">
              {product.name}
            </h1>
            <div className="mb-4">
              <h2 className="text-lg font-semibold mb-1 text-gray-600">
                Catégories:
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
            <div className="mb-4">
              <h2 className="text-lg font-semibold mb-1 text-gray-600">
                Plateformes:
              </h2>
              <div className="flex flex-wrap">
                {product.platforms.map((platform, index) => (
                  <div
                    key={index}
                    className={`mb-2 cursor-pointer px-2 py-1 rounded-full text-sm ${
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
            </div>
            <div>
              <h2 className="text-lg font-semibold mb-1 text-gray-600">
                Description:
              </h2>
              <p className="text-gray-700">{product.description}</p>
            </div>
            <button
              className={`${
                isInCart
                  ? "bg-red-500 hover:bg-red-700"
                  : "bg-blue-500 hover:bg-blue-700"
              } text-white font-bold py-2 px-4 rounded mt-4 w-full md:w-auto`}
              onClick={() => addToCart(product)}
            >
              {isInCart ? "Retirer du panier" : "Ajouter au panier"}
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ProductView;
