import { authHeader } from "../helper/authHeader.js";
import api from "./api.js";

const getAll = () => {
  return api.get("/platform");
};

const getById = (id) => {
  return api.get("/platform/" + id);
};

const create = (value) => {
  return api.post("/platform", value, { headers: authHeader() });
};

export const platformApi = {
  getAll,
  getById,
  create,
};
