import path from "node:path";
import { fileURLToPath } from "node:url";
import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import tailwindcss from "tailwindcss";
import autoprefixer from "autoprefixer";

const projectDir = path.dirname(fileURLToPath(import.meta.url));

// https://vite.dev/config/
// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  css: {
    postcss: {
      plugins: [
        tailwindcss({ config: path.join(projectDir, "tailwind.config.js") }),
        autoprefixer(),
      ],
    },
  },
  server: {
    port: 3000,
    host: true,
  },
});
