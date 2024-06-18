import { authHeader } from "../helper/authHeader.js";
import api from "./api.js";

const getAll = () => {
  return api.get("/category");
};

const getById = (id) => {
  return api.get("/category/" + id);
};

const create = (value) => {
  return api.post("/category", value, { headers: authHeader() });
};

export const categoryApi = {
  getAll,
  getById,
  create,
};
