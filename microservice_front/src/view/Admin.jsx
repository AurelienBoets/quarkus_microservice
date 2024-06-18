import { useState } from "react";
import { useNavigate } from "react-router-dom";
import PlatformModal from "../components/platform/PlatformModal";
import CategoryModal from "../components/category/CategoryModal";

const Admin = () => {
  const navigate = useNavigate();
  const [isPlatformModalOpen, setPlatformModalOpen] = useState(false);
  const [isCategoryModalOpen, setCategoryModalOpen] = useState(false);

  const handlePlatformModalOpen = () => {
    setPlatformModalOpen(!isPlatformModalOpen);
    handleCategoryModalClose();
  };
  const handlePlatformModalClose = () => setPlatformModalOpen(false);

  const handleCategoryModalOpen = () => {
    setCategoryModalOpen(!isCategoryModalOpen);
    handlePlatformModalClose();
  };
  const handleCategoryModalClose = () => setCategoryModalOpen(false);

  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold mb-4">Dashboard</h1>
      <div className="space-y-4">
        <button
          onClick={() => navigate("/product/create")}
          className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
        >
          Créer Produit
        </button>
        <button
          onClick={handlePlatformModalOpen}
          className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded"
        >
          Ajouter Plateforme
        </button>
        <button
          onClick={handleCategoryModalOpen}
          className="bg-yellow-500 hover:bg-yellow-700 text-white font-bold py-2 px-4 rounded"
        >
          Ajouter Catégorie
        </button>
      </div>
      <PlatformModal
        isOpen={isPlatformModalOpen}
        onClose={handlePlatformModalClose}
      />
      <CategoryModal
        isOpen={isCategoryModalOpen}
        onClose={handleCategoryModalClose}
      />
    </div>
  );
};

export default Admin;
