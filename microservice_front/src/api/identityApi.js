import axios from "axios";
import { authHeader } from "../helper/authHeader";

const api = axios.create({
  baseURL: `http://localhost:9000/api/user`,
});

const login = (value) => {
  return api.post("/login", value);
};

const register = (value) => {
  return api.post("/register", value);
};

const edit = (value) => {
  return api.put("/edit", value, { headers: authHeader() });
};

const remove = () => {
  return api.delete("/remove", { headers: authHeader() });
};

const get = () => {
  return api.get("", { headers: authHeader() });
};

export const identityApi = { login, register, edit, remove, get };
