import { authHeader } from "../helper/authHeader.js";
import api from "./api.js";

const getAll = () => {
  return api.get("/order", { headers: authHeader() });
};

const getById = (id) => {
  return api.get("/order/" + id, { headers: authHeader() });
};

const create = (value, sessionId) => {
  return api.post("/order/payment/" + sessionId, value, {
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
