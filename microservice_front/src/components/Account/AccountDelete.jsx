import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

const AccountDelete = ({ isOpen, onClose, onDelete }) => {
  const handleDelete = () => {
    onDelete();
    onClose();
  };

  if (!isOpen) {
    return null;
  }

  return (
    <div className="fixed top-0 right-0 mt-16 mr-6 z-50">
      <div className="bg-white rounded-lg shadow-lg p-6 w-80 border border-gray-300">
        <div className="flex justify-between items-center border-b pb-3">
          <h3 className="text-lg font-medium">Confirmation de suppression</h3>
          <button
            onClick={onClose}
            className="text-gray-600 hover:text-gray-900"
          >
            &times;
          </button>
        </div>
        <div className="mt-4">
          <p className="text-gray-700 mb-4">
            Êtes-vous sûr de vouloir supprimer votre compte ?
          </p>
          <div className="flex justify-end">
            <button
              onClick={handleDelete}
              className="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded mr-2"
            >
              Oui, supprimer
            </button>
            <button
              onClick={onClose}
              className="bg-gray-500 hover:bg-gray-700 text-white font-bold py-2 px-4 rounded"
            >
              Annuler
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AccountDelete;
