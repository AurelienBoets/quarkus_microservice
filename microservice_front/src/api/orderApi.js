import { authHeader } from "../helper/authHeader.js";
import api from "./api.js";

const getAll = () => {
  return api.get("/order", { headers: authHeader() });
};

const getById = (id) => {
  return api.get("/order/" + id, { headers: authHeader() });
};

const create = (sessionId) => {
  return api.get("/order/payment/" + sessionId, {
    headers: authHeader(),
  });
};

const payment = (value) => {
  return api.post("/order/payment", value, { headers: authHeader() });
};

export const orderApi = {
  getAll,
  getById,
  create,
  payment,
};
