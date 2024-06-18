import React, { useRef } from "react";
import { categoryApi } from "../../api/categoryApi";

const CategoryModal = ({ isOpen, onClose }) => {
  const name = useRef();

  if (!isOpen) return null;

  const handleSave = () => {
    categoryApi
      .create({ name: name.current.value })
      .then(() => {
        onClose();
      })
      .catch((e) => {
        console.log(e);
      });
  };

  return (
    <div className="fixed w-full flex items-center justify-center z-50">
      <div className="bg-white rounded-lg shadow-lg p-6 w-80 border border-gray-300">
        <div className="flex justify-between items-center border-b pb-3">
          <h3 className="text-lg font-medium">Ajouter Catégorie</h3>
          <button
            onClick={onClose}
            className="text-gray-600 hover:text-gray-900"
          >
            &times;
          </button>
        </div>
        <div className="mt-4">
          <label htmlFor="category" className="block text-sm font-medium">
            Nom de la Catégorie
          </label>
          <input
            ref={name}
            type="text"
            id="category"
            className="mt-1 px-3 py-2 border border-gray-300 rounded-md w-full"
          />
        </div>
        <div className="mt-4 flex justify-end space-x-2">
          <button
            onClick={onClose}
            className="bg-gray-300 hover:bg-gray-400 text-gray-800 font-bold py-2 px-4 rounded"
          >
            Annuler
          </button>
          <button
            onClick={handleSave}
            className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
          >
            Enregistrer
          </button>
        </div>
      </div>
    </div>
  );
};

export default CategoryModal;
