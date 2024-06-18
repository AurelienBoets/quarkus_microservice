import { useEffect, useRef, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { identityApi } from "../../api/identityApi";

const RegisterForm = () => {
  const navigate = useNavigate();
  const email = useRef();
  const password = useRef();
  const firstname = useRef();
  const lastname = useRef();
  const navigation = useLocation();
  const [isEdit, setIsEdit] = useState(false);
  const [user, setUser] = useState({
    email: "",
    firstname: "",
    lastname: "",
  });

  useEffect(() => {
    if (navigation.pathname === "/account/edit") {
      setIsEdit(true);
      identityApi
        .get()
        .then((resp) => {
          setUser({
            email: resp.data.email,
            firstname: resp.data.firstname,
            lastname: resp.data.lastname,
          });
        })
        .catch((e) => {
          console.log(e);
        });
    }
  }, []);

  const handleRegister = () => {
    identityApi
      .register({
        email: email.current.value,
        password: password.current.value,
        firstname: firstname.current.value,
        lastname: lastname.current.value,
      })
      .then(() => {
        navigate("/login", {
          state: { toast: "Compte créé avec succès !" },
        });
      })
      .catch((e) => {
        console.log(e);
      });
  };

  const handleEdit = () => {
    identityApi
      .edit({
        email: email.current.value,
        password: password.current.value,
        firstname: firstname.current.value,
        lastname: lastname.current.value,
      })
      .then((resp) => {
        localStorage.setItem("user", resp.data.token);
        navigate("/", {
          state: { toast: "Compte modifié avec succès !" },
        });
      })
      .catch((e) => {
        console.log(e);
      });
  };

  return (
    <div className="h-full flex items-center justify-center">
      <div className="p-10 bg-gray-100 rounded-lg shadow-md border border-gray-300 absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 w-full max-w-md">
        <h2 className="text-2xl font-semibold mb-4">
          {isEdit ? "Modification" : "Inscription"}
        </h2>
        <form
          onSubmit={(e) => {
            e.preventDefault();
            isEdit ? handleEdit() : handleRegister();
          }}
          className="flex flex-col gap-4"
        >
          <div className="flex flex-col gap-1">
            <label htmlFor="email" className="text-sm font-medium">
              Adresse email
            </label>
            <input
              ref={email}
              type="email"
              id="email"
              name="email"
              defaultValue={user.email}
              className="px-3 py-2 border border-gray-300 rounded-md"
              required
            />
          </div>
          <div className="flex flex-col gap-1">
            <label htmlFor="password" className="text-sm font-medium">
              Mot de passe
            </label>
            {isEdit ? (
              <input
                ref={password}
                type="password"
                id="password"
                name="password"
                className="px-3 py-2 border border-gray-300 rounded-md"
              />
            ) : (
              <input
                ref={password}
                type="password"
                id="password"
                name="password"
                className="px-3 py-2 border border-gray-300 rounded-md"
                required
              />
            )}
          </div>
          <div className="flex flex-col gap-1">
            <label htmlFor="firstname" className="text-sm font-medium">
              Prénom
            </label>
            <input
              ref={firstname}
              type="text"
              id="firstname"
              name="firstname"
              className="px-3 py-2 border border-gray-300 rounded-md"
              defaultValue={user.firstname}
              required
            />
          </div>
          <div className="flex flex-col gap-1">
            <label htmlFor="lastname" className="text-sm font-medium">
              Nom de famille
            </label>
            <input
              ref={lastname}
              defaultValue={user.lastname}
              type="text"
              id="lastname"
              name="lastname"
              className="px-3 py-2 border border-gray-300 rounded-md"
              required
            />
          </div>
          <button
            type="submit"
            className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
          >
            {isEdit ? "Modifier" : "S'inscrire"}
          </button>
        </form>
      </div>
    </div>
  );
};

export default RegisterForm;
