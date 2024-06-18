const isAlreadyInCart = (value) => {
  const products = JSON.parse(localStorage.getItem("cart")) || [];
  const index = products.find(
    (product) =>
      product.id == value.id && product.platforms == value.platforms[0].id
  );
  if (index !== undefined) {
    return true;
  }
  return false;
};

const addInCart = (value) => {
  let cart = JSON.parse(localStorage.getItem("cart")) || [];
  if (!isAlreadyInCart(value)) {
    cart.push({ id: value.id, platforms: value.platforms[0].id });
    localStorage.setItem("cart", JSON.stringify(cart));
  }
};

const removeInCart = (value) => {
  let cart = JSON.parse(localStorage.getItem("cart")) || [];
  if (isAlreadyInCart(value)) {
    cart = cart.filter(
      (product) =>
        product.id !== value.id || product.platforms !== value.platforms[0].id
    );
    localStorage.setItem("cart", JSON.stringify(cart));
  }
};

const getProducts = () => {
  return JSON.parse(localStorage.getItem("cart")) || [];
};

export const cart = { isAlreadyInCart, addInCart, removeInCart, getProducts };
