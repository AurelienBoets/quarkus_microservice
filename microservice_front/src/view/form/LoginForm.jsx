import { useEffect, useRef } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { identityApi } from "../../api/identityApi";
import { ToastContainer, toast } from "react-toastify";
import { getUserDetails } from "../../helper/user-details";

const LoginForm = () => {
  const email = useRef();
  const password = useRef();
  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    if (location.state && location.state.toast) {
      toast.success(location.state.toast);
      navigate(location.pathname, { replace: true, state: {} });
    }
  });

  const handleLogin = () => {
    identityApi
      .login({ mail: email.current.value, password: password.current.value })
      .then((resp) => {
        localStorage.setItem("user", resp.data.token);
        navigate("/");
      })
      .catch((e) => {
        console.log(e);
      });
  };

  return (
    <div className="h-full flex items-center justify-center">
      <ToastContainer />
      <div className="p-10 bg-gray-100 rounded-lg shadow-md border border-gray-300 absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 w-full max-w-md">
        <h2 className="text-2xl font-semibold mb-4">Connexion</h2>
        <form
          onSubmit={(e) => {
            e.preventDefault();
            handleLogin();
          }}
          className="flex flex-col gap-4"
        >
          <div className="flex flex-col gap-1">
            <label htmlFor="email" className="text-sm font-medium">
              Adresse email
            </label>
            <input
              ref={email}
              type="email"
              id="email"
              name="email"
              className="px-3 py-2 border border-gray-300 rounded-md"
              required
            />
          </div>
          <div className="flex flex-col gap-1">
            <label htmlFor="password" className="text-sm font-medium">
              Mot de passe
            </label>
            <input
              ref={password}
              type="password"
              id="password"
              name="password"
              className="px-3 py-2 border border-gray-300 rounded-md"
              required
            />
          </div>
          <button
            type="submit"
            className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
          >
            Se connecter
          </button>
        </form>
      </div>
    </div>
  );
};

export default LoginForm;
