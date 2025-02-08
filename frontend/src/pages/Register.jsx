import { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../css/styles.css";

function Register() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState(""); // Estado para manejar errores
  const navigate = useNavigate();

  const handleRegister = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch("http://localhost:8080/auth/register", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ username, password }),
      });

      if (!response.ok) {
        const errorMessage = await response.text();
        throw new Error(errorMessage);
      }

      alert("Usuario registrado exitosamente");
      navigate("/"); // Redirige al usuario a la página de inicio de sesión
    } catch (error) {
      setError(error.message);
    }
  };

  return (
    <div className="container">
      <h2>Registro</h2>
      {error && <p className="error-message">{error}</p>} {/* Mostrar error si existe */}
      <form onSubmit={handleRegister}>
        <input
          type="text"
          placeholder="Usuario"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          required
        />
        <input
          type="password"
          placeholder="Contraseña"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />
        <button type="submit">Registrarse</button>
      </form>
    </div>
  );
}

export default Register;
