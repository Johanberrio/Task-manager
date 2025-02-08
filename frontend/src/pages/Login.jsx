import { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../css/styles.css";

function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState(""); // Estado para manejar errores
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    setError(""); // Limpiar errores previos

    try {
      const response = await fetch("http://localhost:8080/auth/login", {
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

      const data = await response.json();
      localStorage.setItem("token", data.token); // Guardar el token en localStorage

      alert("Inicio de sesión exitoso");
      navigate("/dashboard"); // Redirigir al dashboard
    } catch (error) {
      setError(error.message);
    }
  };

  return (
    <div className="container">
      <h2>Iniciar Sesión</h2>
      {error && <p className="error-message">{error}</p>} {/* Mostrar error si existe */}
      <form onSubmit={handleLogin}>
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
        <button type="submit">Ingresar</button>
      </form>
    </div>
  );
}

export default Login;
