export function authHeader() {
  const token = localStorage.getItem("user");
  if (token) {
    return { Authorization: "Bearer " + token };
  } else {
    return {};
  }
}
