import { createBrowserRouter, redirect } from "react-router-dom";
import Home from "./view/Home";
import Header from "./view/Header";
import ProductView from "./view/ProductView";
import CartView from "./view/CartView";
import LoginForm from "./view/form/LoginForm";
import RegisterForm from "./view/form/RegisterForm";
import { getUserDetails } from "./helper/user-details";
import Admin from "./view/Admin";
import ProductForm from "./view/form/ProductForm";
import OrderHistoric from "./view/OrderHistoric";
import PaymentSuccess from "./view/PaymentSuccess";

const isNotLogin = () => {
  if (getUserDetails()) {
    return redirect("/");
  }
  return true;
};

const isLogin = () => {
  if (getUserDetails()) {
    return true;
  }
  return redirect("/");
};

const isAdmin = () => {
  if (getUserDetails().role === "ROLE_ADMIN") {
    return true;
  }
  return redirect("/");
};

const router = createBrowserRouter([
  {
    element: <Header />,
    children: [
      {
        path: "/",
        element: <Home />,
      },
      {
        path: "/product/:id",
        element: <ProductView />,
      },
      {
        path: "/cart",
        element: <CartView />,
      },
      {
        path: "/login",
        element: <LoginForm />,
        loader: () => isNotLogin(),
      },
      {
        path: "/register",
        element: <RegisterForm />,
        loader: () => isNotLogin(),
      },
      {
        path: "/account/edit",
        element: <RegisterForm />,
        loader: () => isLogin(),
      },
      {
        path: "/dashboard",
        element: <Admin />,
        loader: () => isAdmin(),
      },
      {
        path: "/product/create",
        element: <ProductForm />,
        loader: () => isAdmin(),
      },
      {
        path: "/success",
        element: <PaymentSuccess />,
        loader: () => isLogin(),
      },
      {
        path: "/account/orders",
        element: <OrderHistoric />,
        loader: () => isLogin(),
      },
    ],
  },
]);

export default router;
