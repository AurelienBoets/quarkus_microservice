import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import AccountDelete from "./AccountDelete";
import { identityApi } from "../../api/identityApi";

const Account = ({ isOpen, onClose }) => {
  const navigate = useNavigate();
  const [isDeleteModalOpen, setDeleteModalOpen] = useState(false);

  const handleDeleteAccount = () => {
    identityApi
      .remove()
      .then(() => {
        localStorage.removeItem("user");
        navigate("/", { state: { toast: "Compte supprimé" } });
      })
      .catch((e) => {
        console.log(e);
      });
    onClose();
  };

  if (!isOpen) {
    return null;
  }

  return (
    <div className="fixed top-0 right-0 mt-16 mr-6 z-50">
      <div className="bg-white rounded-lg shadow-lg p-6 w-80 border border-gray-300">
        <div className="flex justify-between items-center border-b pb-3">
          <h3 className="text-lg font-medium">Mon Compte</h3>
          <button
            onClick={onClose}
            className="text-gray-600 hover:text-gray-900"
          >
            &times;
          </button>
        </div>
        <div className="mt-4">
          <ul>
            <li className="mb-2">
              <button
                onClick={() => {
                  navigate("/account/edit");
                  onClose();
                }}
                className="w-full text-left px-4 py-2 hover:bg-gray-100 rounded-md"
              >
                Modifier le compte
                <i className="ml-2 fa-regular fa-pen-to-square"></i>
              </button>
            </li>
            <li className="mb-2">
              <button
                onClick={() => setDeleteModalOpen(true)}
                className="w-full text-left px-4 py-2 hover:bg-gray-100 rounded-md"
              >
                Supprimer le compte
                <i className="ml-2 fa-regular fa-trash-can"></i>
              </button>
            </li>
            <li className="mb-2">
              <button
                onClick={() => {
                  navigate("/account/orders");
                  onClose();
                }}
                className="w-full text-left px-4 py-2 hover:bg-gray-100 rounded-md"
              >
                Historique des commandes
                <i className="ml-2 fa-regular fa-rectangle-list"></i>
              </button>
            </li>
          </ul>
        </div>
      </div>
      <AccountDelete
        isOpen={isDeleteModalOpen}
        onClose={() => setDeleteModalOpen(false)}
        onDelete={handleDeleteAccount}
      />
    </div>
  );
};

export default Account;
