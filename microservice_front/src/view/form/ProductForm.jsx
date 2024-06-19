import { useEffect, useRef, useState } from "react";
import { platformApi } from "../../api/platformApi";
import { categoryApi } from "../../api/categoryApi";
import { productApi } from "../../api/productApi";
import { useNavigate } from "react-router-dom";

const ProductForm = () => {
  const navigate = useNavigate();
  const [platforms, setPlatforms] = useState([]);
  const [categories, setCategories] = useState([]);
  const name = useRef();
  const img = useRef();
  const description = useRef();
  const [platformPrice, setPlatformPrice] = useState([
    { id: "", name: "", price: "" },
  ]);
  const [categoryProduct, setCategoryProduct] = useState([]);

  useEffect(() => {
    platformApi
      .getAll()
      .then((resp) => {
        setPlatforms(resp.data);
      })
      .catch((e) => {
        console.log(e);
      });

    categoryApi
      .getAll()
      .then((resp) => {
        setCategories(resp.data);
      })
      .catch((e) => {
        console.log(e);
      });
  }, []);

  const handlePlatformChange = (index, id) => {
    const platform = platforms.find((p) => p.id === id);
    const updatedPlatformPrice = [...platformPrice];
    updatedPlatformPrice[index].id = id;
    updatedPlatformPrice[index].name = platform ? platform.name : "";
    setPlatformPrice(updatedPlatformPrice);
  };

  const handlePriceChange = (index, price) => {
    const updatedPlatformPrice = [...platformPrice];
    updatedPlatformPrice[index].price = price;
    setPlatformPrice(updatedPlatformPrice);
  };

  const handleAddPlatform = () => {
    setPlatformPrice([...platformPrice, { id: "", name: "", price: "" }]);
  };

  const handleRemovePlatform = (index) => {
    if (platformPrice.length > 1) {
      const updatedPlatformPrice = platformPrice.filter((_, i) => i !== index);
      setPlatformPrice(updatedPlatformPrice);
    }
  };

  const handleCategoryChange = (categoryId) => {
    setCategoryProduct((prev) => {
      if (prev.includes(categoryId)) {
        return prev.filter((id) => id !== categoryId);
      } else {
        return [...prev, categoryId];
      }
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const reader = new FileReader();
    reader.readAsDataURL(img.current.files[0]);
    reader.onloadend = () => {
      const base64Image = reader.result.split(",")[1];
      const product = {
        name: name.current.value,
        description: description.current.value,
        img: base64Image,
        formatImg: img.current.files[0].type,
        category_id: categoryProduct,
        platforms: platformPrice,
      };
      productApi
        .create(product)
        .then(() => {
          navigate("/dashboard");
        })
        .catch((e) => {
          console.log(e);
        });
    };
  };

  return (
    <div className="h-full flex items-center justify-center">
      <div className="p-10 bg-gray-100 rounded-lg shadow-md border border-gray-300 w-full max-w-2xl">
        <h2 className="text-2xl font-semibold mb-4">Créer Produit</h2>
        <form onSubmit={handleSubmit} className="flex flex-col gap-4">
          <div className="flex flex-col gap-1">
            <label htmlFor="name" className="text-sm font-medium">
              Nom du produit
            </label>
            <input
              ref={name}
              type="text"
              id="name"
              name="name"
              className="px-3 py-2 border border-gray-300 rounded-md"
              required
            />
          </div>
          <div className="flex flex-col gap-1">
            <label htmlFor="img" className="text-sm font-medium">
              Image du produit
            </label>
            <input
              ref={img}
              type="file"
              id="img"
              name="img"
              className="px-3 py-2 border border-gray-300 rounded-md"
              accept="image/*"
              required
            />
          </div>
          <div className="flex flex-col gap-1">
            <label htmlFor="description" className="text-sm font-medium">
              Description
            </label>
            <textarea
              ref={description}
              id="description"
              name="description"
              className="px-3 py-2 border border-gray-300 rounded-md"
              required
            />
          </div>
          <div className="flex flex-col gap-1">
            <label className="text-sm font-medium">Catégories</label>
            {categories.map((category) => (
              <div key={category.id} className="flex items-center gap-2">
                <input
                  type="checkbox"
                  id={`category-${category.id}`}
                  value={category.id}
                  onChange={() => handleCategoryChange(category.id)}
                />
                <label htmlFor={`category-${category.id}`}>
                  {category.name}
                </label>
              </div>
            ))}
          </div>
          <div className="flex flex-col gap-1">
            <label className="text-sm font-medium">Plateformes et Prix</label>
            {platformPrice.map((item, index) => (
              <div key={index} className="flex items-center gap-2">
                <select
                  value={item.id}
                  onChange={(e) => handlePlatformChange(index, e.target.value)}
                  className="px-3 py-2 border border-gray-300 rounded-md"
                >
                  <option value="" disabled>
                    Sélectionner une plateforme
                  </option>
                  {platforms.map((platform) => (
                    <option key={platform.id} value={platform.id}>
                      {platform.name}
                    </option>
                  ))}
                </select>
                <input
                  type="number"
                  placeholder="Prix"
                  value={item.price}
                  onChange={(e) => handlePriceChange(index, e.target.value)}
                  className="px-3 py-2 border border-gray-300 rounded-md"
                />
                {platformPrice.length > 1 && (
                  <button
                    type="button"
                    onClick={() => handleRemovePlatform(index)}
                    className="text-red-500"
                  >
                    Supprimer
                  </button>
                )}
              </div>
            ))}
            <button
              type="button"
              onClick={handleAddPlatform}
              className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded mt-2"
            >
              Ajouter une plateforme
            </button>
          </div>
          <button
            type="submit"
            className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded mt-4"
          >
            Créer
          </button>
        </form>
      </div>
    </div>
  );
};
export default ProductForm;
