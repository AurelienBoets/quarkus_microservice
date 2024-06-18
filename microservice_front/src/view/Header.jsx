import { Outlet, useLocation, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import { getUserDetails } from "../helper/user-details";
import Account from "../components/Account/Account";

const Header = () => {
  const navigate = useNavigate();
  const userDetails = getUserDetails();
  const [isModalOpen, setModalOpen] = useState(false);
  const [isMenuOpen, setMenuOpen] = useState(false);
  const location = useLocation();

  useEffect(() => {
    const pathname = location.pathname;
    const isStripeSuccessPage = pathname.startsWith("/success/");

    if (localStorage.getItem("temp") && !isStripeSuccessPage) {
      localStorage.removeItem("temp");
    }
  }, [location]);

  const handleLogout = () => {
    localStorage.removeItem("user");
    navigate("/");
  };

  const handleAccountClick = () => {
    setModalOpen(!isModalOpen);
  };

  const handleCloseModal = () => {
    setModalOpen(false);
  };

  const toggleMenu = () => {
    setMenuOpen(!isMenuOpen);
  };

  return (
    <>
      <div className="bg-gray-800 h-20 w-screen flex items-center justify-between shadow-lg px-6 md:px-10">
        <h1
          className="text-2xl font-bold text-white cursor-pointer"
          onClick={() => navigate("/")}
        >
          ECommerce
        </h1>
        <div className="hidden md:flex items-center flex-1 mx-10">
          <div className="relative w-full max-w-md">
            <input
              type="text"
              placeholder="Rechercher..."
              className="bg-gray-700 border border-gray-600 text-gray-300 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full pl-10 pr-4 py-2"
            />
            <i className="fa-solid fa-magnifying-glass absolute inset-y-1/4 left-3 flex items-center text-gray-400"></i>
          </div>
        </div>
        <div className="flex items-center space-x-6 text-white">
          <span onClick={() => navigate("/cart")} className="cursor-pointer">
            <i className="fa-solid fa-cart-shopping text-lg"></i>
          </span>
          <div className="hidden md:flex items-center space-x-4">
            {userDetails ? (
              <>
                <span onClick={handleAccountClick} className="cursor-pointer">
                  Bonjour {userDetails.name}
                  <i className="fa-regular fa-user ml-1 text-lg"></i>
                </span>
                {userDetails.role === "ROLE_ADMIN" && (
                  <span
                    onClick={() => navigate("/dashboard")}
                    className="cursor-pointer"
                  >
                    Admin <i className="fa-solid fa-user-shield text-lg"></i>
                  </span>
                )}
                <button
                  onClick={handleLogout}
                  className="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded"
                >
                  Déconnexion
                </button>
              </>
            ) : (
              <>
                <button
                  onClick={() => navigate("/login")}
                  className="bg-gray-500 hover:bg-gray-700 text-white font-bold py-2 px-4 rounded"
                >
                  Connexion
                </button>
                <button
                  onClick={() => navigate("/register")}
                  className="bg-gray-500 hover:bg-gray-700 text-white font-bold py-2 px-4 rounded"
                >
                  Inscription
                </button>
              </>
            )}
          </div>
          <button
            className="md:hidden text-white focus:outline-none"
            onClick={toggleMenu}
          >
            <i className="fa-solid fa-bars text-xl"></i>
          </button>
        </div>
      </div>
      {isMenuOpen && (
        <div className="md:hidden bg-gray-800 text-white flex flex-col px-6 py-4 space-y-4">
          <div className="relative w-full max-w-md">
            <input
              type="text"
              placeholder="Rechercher..."
              className="bg-gray-700 border border-gray-600 text-gray-300 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full pl-10 pr-4 py-2"
            />
            <i className="fa-solid fa-magnifying-glass absolute inset-y-1/4 left-3 flex items-center text-gray-400"></i>
          </div>
          {userDetails ? (
            <>
              <span onClick={handleAccountClick} className="cursor-pointer">
                Bonjour {userDetails.name}
                <i className="fa-regular fa-user ml-1 text-lg"></i>
              </span>
              {userDetails.role === "ROLE_ADMIN" && (
                <span
                  onClick={() => navigate("/dashboard")}
                  className="cursor-pointer"
                >
                  Admin <i className="fa-solid fa-user-shield text-lg"></i>
                </span>
              )}
              <button
                onClick={handleLogout}
                className="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded"
              >
                Déconnexion
              </button>
            </>
          ) : (
            <>
              <button
                onClick={() => navigate("/login")}
                className="bg-gray-500 hover:bg-gray-700 text-white font-bold py-2 px-4 rounded"
              >
                Connexion
              </button>
              <button
                onClick={() => navigate("/register")}
                className="bg-gray-500 hover:bg-gray-700 text-white font-bold py-2 px-4 rounded"
              >
                Inscription
              </button>
            </>
          )}
        </div>
      )}
      <Account isOpen={isModalOpen} onClose={handleCloseModal} />
      <Outlet />
    </>
  );
};

export default Header;
