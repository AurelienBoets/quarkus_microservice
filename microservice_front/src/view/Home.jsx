import { useEffect, useState } from "react";
import CardProduct from "../components/Product/CardProduct";
import { productApi } from "../api/productApi";
import { useLocation, useNavigate } from "react-router-dom";
import { ToastContainer, toast } from "react-toastify";

const Home = () => {
  const [products, setProducts] = useState([]);
  const location = useLocation();
  const navigate = useNavigate();

  useEffect(() => {
    if (location?.state?.products) {
      setProducts(location.state.products);
    } else {
      productApi
        .getAll()
        .then((resp) => {
          setProducts(resp.data);
        })
        .catch((e) => {
          console.log(e);
        });
    }
  }, [location]);

  useEffect(() => {
    if (location?.state?.toast) {
      toast.info(location.state.toast);
      navigate(location.pathname, { replace: true, state: {} });
    }
  });

  return (
    <div className="container mx-auto p-4">
      <ToastContainer />
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        {products.map((product) => (
          <CardProduct
            key={product.id}
            id={product.id}
            name={product.name}
            img={product.img}
            formatImg={product.formatImg}
          />
        ))}
      </div>
    </div>
  );
};

export default Home;
