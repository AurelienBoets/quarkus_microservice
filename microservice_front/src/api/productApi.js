import { authHeader } from "../helper/authHeader.js";
import api from "./api.js";

const getAll = () => {
  return api.get("/product");
};

const getById = (id) => {
  return api.get("/product/" + id);
};

const create = (value) => {
  return api.post("/product", value, { headers: authHeader() });
};

export const productApi = {
  getAll,
  getById,
  create,
};
