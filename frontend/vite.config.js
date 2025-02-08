import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

export default defineConfig({
  plugins: [react()],
  root: "frontend", // 🟢 Asegúrate de que el `root` apunta a la carpeta correcta
  build: {
    outDir: "dist",
    emptyOutDir: true,
  },
});
