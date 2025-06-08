# Dulce Amor - Sistema de Gestión de Pedidos

Este proyecto fue desarrollado como parte de un trabajo práctico gradual para la materia de desarrollo de software. Se trata de un sistema de escritorio en JavaFX para registrar y gestionar pedidos, clientes y productos de un emprendimiento de postres.

---

## 🛠 Tecnologías utilizadas

- **Java** (con JavaFX) para la interfaz y lógica de negocio
- **MySQL** para la persistencia de datos
- **XAMPP** como entorno local de base de datos
- **JDBC** para la conexión entre Java y MySQL
- **NetBeans 25** como entorno de desarrollo

---

## 📦 Funcionalidades implementadas

### ✅ Clientes
- Registro de nuevos clientes (nombre, teléfono, dirección, email)
- Visualización de lista de clientes
- Modificación de datos
- Eliminación desde base de datos

### ✅ Productos *(en desarrollo)*
- Registro de productos con nombre, precio y stock

### ✅ Pedidos *(a implementar)*

---

## 🗃️ Estructura del proyecto
/dulceamor
├── src/dulcamor/
│ ├── SistemaPedidosFX.java
│ ├── ConexionBD.java
│ └── ClienteItem.java
├── sql/
│ └── script_creacion.sql
├── README.md
└── .gitignore


---

## 🗄 Script de base de datos

En la carpeta `/sql` encontrarás el archivo `script_creacion.sql` que te permite crear la base de datos `dulceamor` y su tabla `clientes`.

Ejecutalo desde phpMyAdmin o consola MySQL:

```sql
CREATE DATABASE IF NOT EXISTS dulceamor;
USE dulceamor;

CREATE TABLE IF NOT EXISTS clientes (
  id_cliente INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100),
  telefono VARCHAR(20),
  direccion VARCHAR(150),
  email VARCHAR(100)
);

🚀 Cómo ejecutar el sistema
Cloná este repositorio o descargalo

Abrilo con NetBeans 25

Configurá el conector MySQL (mysql-connector-j) en las librerías del proyecto

Verificá que tu servidor XAMPP esté encendido (MySQL en ejecución)

Ejecutá SistemaPedidosFX.java

🙋‍♂️ Autor
Felipe Pravisán – Estudiante de [nombre de tu institución si querés incluirla]

📄 Licencia
Este proyecto es solo para fines educativos.
