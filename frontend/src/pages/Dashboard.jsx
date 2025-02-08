import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "../css/styles.css";

function Dashboard() {
  const navigate = useNavigate();
  const [tasks, setTasks] = useState([]);
  const [filterStatus, setFilterStatus] = useState("");
  const [filterPriority, setFilterPriority] = useState("");

  const [newTask, setNewTask] = useState({
    title: "",
    description: "",
    status: "pendiente",
    priority: "media",
  });

  const [editingTask, setEditingTask] = useState(null);

  useEffect(() => {
    const fetchTasks = async () => {
      const token = localStorage.getItem("token"); // Obtiene el token guardado

      if (!token) {
        alert("No tienes permisos para acceder. Inicia sesión.");
        navigate("/");
        return;
      }

      try {
        const token = localStorage.getItem("token");
        console.log(token)
        const response = await fetch("http://localhost:8080/api/tasks", {
          method: "GET",
          credentials: "include", 
          headers: {
            "Authorization": `Bearer ${token}`, // Envía el token en la cabecera
            "Content-Type": "application/json",
          },
        });

        if (!response.ok) {
          throw new Error(`Error: ${response.status}`);
        }

        const data = await response.json();
        setTasks(data);
      } catch (error) {
        console.error("Error al obtener tareas:", error);
      }
    };

    fetchTasks();
  }, [navigate]);

  const handleLogout = () => {
    localStorage.removeItem("token"); // Eliminar el token al cerrar sesión
    navigate("/");
  };

  const handleInputChange = (e) => {
    setNewTask({ ...newTask, [e.target.name]: e.target.value });
  };

  const handleCreateOrUpdateTask = async () => {
    const token = localStorage.getItem("token");
    const method = editingTask ? "PUT" : "POST";
    const url = editingTask
      ? `http://localhost:8080/api/tasks/${editingTask.id}`
      : "http://localhost:8080/api/tasks";

    try {
      const response = await fetch(url, {
        method,
        credentials: "include",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify(newTask),
      });

      if (!response.ok) {
        throw new Error("Error al guardar la tarea");
      }

      const savedTask = await response.json();

      if (editingTask) {
        setTasks(
          tasks.map((task) => (task.id === editingTask.id ? savedTask : task))
        );
        setEditingTask(null);
      } else {
        setTasks([...tasks, savedTask]);
      }

      setNewTask({ title: "", description: "", status: "pendiente", priority: "media" });
    } catch (error) {
      console.error("Error al guardar la tarea:", error);
    }
  };

  const handleEditTask = (task) => {
    setEditingTask(task);
    setNewTask(task);
  };

  const handleDeleteTask = async (id) => {
    const token = localStorage.getItem("token");

    try {
      const response = await fetch(`http://localhost:8080/api/tasks/${id}`, {
        method: "DELETE",
        credentials: "include",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (!response.ok) {
        throw new Error("Error al eliminar la tarea");
      }

      setTasks(tasks.filter((task) => task.id !== id));
    } catch (error) {
      console.error("Error al eliminar la tarea:", error);
    }
  };

  // Filtrar tareas por estado y prioridad
  const filteredTasks = tasks.filter((task) => {
    return (
      (filterStatus ? task.status === filterStatus : true) &&
      (filterPriority ? task.priority === filterPriority : true)
    );
  });

  return (
    <div className="container">
      <h2>Dashboard</h2>
      <p>Bienvenido al sistema de gestión de tareas.</p>

      {/* Filtros */}
      <div className="filters">
        <select onChange={(e) => setFilterStatus(e.target.value)}>
          <option value="">Todos los estados</option>
          <option value="pendiente">Pendiente</option>
          <option value="en progreso">En progreso</option>
          <option value="completado">Completado</option>
        </select>

        <select onChange={(e) => setFilterPriority(e.target.value)}>
          <option value="">Todas las prioridades</option>
          <option value="baja">Baja</option>
          <option value="media">Media</option>
          <option value="alta">Alta</option>
        </select>
      </div>

      {/* Formulario para crear o editar tareas */}
      <div className="task-form">
        <h3>{editingTask ? "Editar Tarea" : "Nueva Tarea"}</h3>
        <input
          type="text"
          name="title"
          placeholder="Título"
          value={newTask.title}
          onChange={handleInputChange}
        />
        <input
          type="text"
          name="description"
          placeholder="Descripción"
          value={newTask.description}
          onChange={handleInputChange}
        />
        <select name="status" value={newTask.status} onChange={handleInputChange}>
          <option value="pendiente">Pendiente</option>
          <option value="en progreso">En progreso</option>
          <option value="completado">Completado</option>
        </select>
        <select name="priority" value={newTask.priority} onChange={handleInputChange}>
          <option value="baja">Baja</option>
          <option value="media">Media</option>
          <option value="alta">Alta</option>
        </select>
        <button onClick={handleCreateOrUpdateTask}>
          {editingTask ? "Actualizar" : "Crear"} Tarea
        </button>
        {editingTask && (
          <button onClick={() => setEditingTask(null)}>Cancelar</button>
        )}
      </div>

      {/* Tabla de tareas */}
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Título</th>
            <th>Descripción</th>
            <th>Estado</th>
            <th>Prioridad</th>
          </tr>
        </thead>
        <tbody>
          {filteredTasks.map((task) => (
            <tr key={task.id}>
              <td>{task.id}</td>
              <td>{task.title}</td>
              <td>{task.description}</td>
              <td>{task.status}</td>
              <td>{task.priority}</td>
              <td>
                <button onClick={() => handleEditTask(task)}>✏️ Editar</button>
                <button onClick={() => handleDeleteTask(task.id)}>🗑️ Eliminar</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      <button onClick={handleLogout}>Cerrar sesión</button>
    </div>
  );
}

export default Dashboard;
