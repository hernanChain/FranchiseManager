
# Franchise Manager API

Este proyecto proporciona una API para gestionar franquicias, sucursales y productos dentro de una franquicia. Permite crear franquicias, añadir sucursales, gestionar productos y realizar operaciones como modificar el stock de productos y obtener el producto con mayor stock en cada sucursal.

## Requerimientos

Para ejecutar este proyecto, necesitas tener instalado lo siguiente en tu entorno local:

- **Java 21+** (JDK)
- **Docker** (si deseas usar contenedores)
- **Gradle** o **Maven** (para construir el proyecto)

## Desplegar la aplicación en un entorno local

### 1. Clonar el repositorio

Primero, clona el repositorio en tu máquina local:

```bash
git clone https://github.com/tu-usuario/franchise-manager.git
cd franchise-manager
```

### 2. Construir el proyecto


```bash
./gradlew build
```

### 3. Ejecutar el proyecto localmente

Para ejecutar la aplicación en tu entorno local, usa el siguiente comando:

```bash
./gradlew bootRun
```

Esto levantará el servidor en el puerto `8080` por defecto.

### 4. Acceder a la API

Una vez que la aplicación esté en ejecución, podrás acceder a la API en la siguiente URL:

```
http://localhost:8080
```

### 5. Probar la API

Puedes probar las siguientes rutas de la API:

- **Crear una franquicia:**
  - `POST /api/franchises`
  - Cuerpo: `{ "name": "Nombre de la franquicia" }`

- **Agregar una sucursal a una franquicia:**
  - `POST /api/franchises/{franchiseName}/branches`
  - Cuerpo: `{ "name": "Nombre de la sucursal" }`

- **Agregar un producto a una sucursal:**
  - `POST /api/franchises/{franchiseName}/branches/{branchName}/products`
  - Cuerpo: `{ "name": "Producto", "stock": 10 }`

- **Eliminar un producto de una sucursal:**
  - `DELETE /api/franchises/{franchiseName}/branches/{branchName}/products/{productName}`

- **Actualizar stock de un producto:**
  - `PUT /api/franchises/{franchiseName}/branches/{branchName}/products/{productName}/stock`
  - Cuerpo: `{ "stock": 20 }`

- **Obtener el producto con más stock por sucursal de una franquicia:**
  - `GET /api/franchises/{franchiseName}/products/top-stock`

## Desplegar la aplicación con Docker

Si prefieres usar Docker para desplegar la aplicación, sigue estos pasos:

### 1. Crear la imagen de Docker

Primero, construye la imagen de Docker ejecutando:

```bash
docker build -t franchisemanager .
```

Este comando construirá la imagen usando el `Dockerfile` en la carpeta deployment del proyecto.

### 2. Ejecutar el contenedor de Docker

Una vez que la imagen haya sido creada, puedes ejecutar el contenedor con el siguiente comando:

```bash
docker run -p 8080:8080 franchisemanager
```

Esto iniciará la aplicación dentro de un contenedor y estará disponible en el puerto `8080`.

### 3. Acceder a la API

Al igual que en el entorno local, puedes acceder a la API en la siguiente URL:

```
http://localhost:8080
```

## Configuración de MongoDB

Este proyecto utiliza **MongoDB** como base de datos. Para que la aplicación funcione correctamente, debes tener una instancia de MongoDB en funcionamiento.

### Usando Docker para MongoDB

Si no tienes MongoDB instalado en tu máquina, puedes correrlo usando Docker con el siguiente comando:

```bash
docker run --name mongodb -d -p 27017:27017 mongo:latest
```

Esto levantará un contenedor de MongoDB en el puerto `27017` y lo vinculará a tu aplicación.

## Contribuciones

Las contribuciones son bienvenidas. Si tienes alguna sugerencia, corrección o nueva característica que te gustaría agregar, por favor abre un **issue** o un **pull request**.

## Licencia

Este proyecto está bajo la **MIT License** - ver el archivo [LICENSE](LICENSE) para más detalles.

