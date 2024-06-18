import { authHeader } from "../helper/authHeader.js";
import api from "./api.js";

const createPayment = (value) => {
  return api.post("/payment", { amount: value }, { headers: authHeader() });
};

export const paymentApi = { createPayment };
