# Franchise App – API para la gestión de Franquicias

Esta es una solución para la **Prueba Práctica Dev Backend**, donde se implementa un API para gestionar franquicias, sus sucursales y los productos asociados a cada sucursal.  
Incluye funcionalidades CRUD, buenas prácticas de desarrollo y despliegue en AWS usando Cloudformation.

---

## Requerimientos de la prueba

La prueba solicita:

- API para manejar franquicias, sucursales y productos.
- Spring Boot como framework obligatorio.
- Persistencia con alguna base de datos (Redis, MySQL, MongoDB, DynamoDB, etc.).
- Endpoints obligatorios:
    1. Agregar una nueva franquicia.
    2. Agregar una nueva sucursal a una franquicia.
    3. Agregar un nuevo producto a una sucursal.
    4. Eliminar un producto de una sucursal.
    5. Modificar el stock de un producto.
    6. Endpoint para obtener el producto con mayor stock por cada sucursal de una franquicia.
- Plus:
    - Docker
    - Programación funcional o reactiva
    - Endpoints para actualizar nombres
    - Infraestructura como código (Terraform / CloudFormation)
    - Despliegue en nube

---

# 🚀 Tecnologías utilizadas

| Tecnología                  | Descripción              |
|-----------------------------|--------------------------|
| **Java 21**                 | Lenguaje principal       |
| **Spring Boot**             | Framework para el desarrollo del API |
| **Spring WebFlux**          | Programación reactiva    |
| **R2DBC**                   | Persistencia reactiva o tradicional |
| **PostgreSQL**              | Motor de base de datos   |
| **Docker & Docker Compose** | Empaquetado              |
| **CloudFormation**          | Infraestructura como código |
| **GitFlow**                 | Flujo de trabajo en ramas |
| **GitHub Actions**          | Flujo de integración continua |
| **Flyway**                  | Migraciones              |
| **Java validation**         | Validación de entradas   |
| **JUnit5 Mockito**          | Pruebas unitarias        |
| **Lombok**                  | Getters, Setters, etc.   |
| **Maven**                   | Gestor de dependencias   |



---

# 🛠️ Arquitectura

La aplicación sigue una arquitectura en capas:

El proyecto está organizado por módulos de dominio (Franchise, Branch y Product).
Cada módulo contiene sus propios componentes internos agrupados por responsabilidad:

```
franchise/
 ├── api/           → Casos de uso (servicios de aplicación)
 │    └── FranchiseCreator.java (Crea la entidad franquicia)
 ├── endpoint/      → Controlador REST reactivo
 ├── entity/        → Entidades del dominio (Franchise.java)
 ├── request/       → DTOs de entrada
 ├── exception/     → Excepciones específicas del dominio
 ├── dto/           → DTO's
 ├── mapper/        → Convertidos de dto a entity y visceversa 
 ├── repository/    → repositorio reactivo
```

✅ Ventajas

1. Alta Cohesión: Todo lo relacionado con Franquicia está junto
2. Mejor Encapsulamiento: Puedes hacer package-private algunos componentes
3. Más Fácil de Navegar: Encuentras todo de una entidad en un solo lugar
4. Escalabilidad: Al crecer el proyecto, es más manejable
5. Microservicios Ready: Si decides separar en microservicios, solo mueves la carpeta
6. Menos Scrolling: No tienes que saltar entre carpetas lejanas
7. Contexto Claro: Es evidente qué hace cada módulo
8. Equipos Independientes: Diferentes equipos pueden trabajar en diferentes features sin conflictos

# 📡 API (/api/v1)

|           Endpoint	            |  Method  |           Req. body           | Status |                                                                                                               Resp. body                                                                                                                | Description    		   	                     |
|:------------------------------:|:--------:|:-----------------------------:|:------:|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|:------------------------------------------|
|          `/franchise`          |  `POST`  |       `{"name":"Nike"}`       |  201   | FranchiseDto  | Registra una franquicia                   |
|          `/franchise`          |  `POST`  |       `{"name":"Nike"}`       |  400   |`{"timestamp":"2025-11-22T03:32:05.795048800Z","status":400,"error":"Solicitud invalida.","messages":["El nombre Nike ya se encuentra registrado."]}`                                          | El nombre ingresado ya se esta registrado |
|          `/franchise`          |  `POST`  |       `{"name":"Nike"}`       |  400   || Nombre invalido                           |
| `"/{franchise_id}/branch/add"` |  `POST`  | `{"name": "Nike - Calle 85"}` |  201   |                                                                                                                BranchDto| Agregar una sucursal a una franquicia     |
|      `"/{franchise_id}/branch/add"`      |  `POST`  | `{"name": "Nike - Calle 85"}` |  400   |                                    `{"timestamp":"2025-11-22T03:38:18.406993100Z","status":400,"error":"Solicitud invalida.","messages":["El nombre Nike - Calle 85 ya se encuentra registrado."]}`| Nombre de sucursal duplicado              |
|      `"/{franchise_id}/branch/add"`      |  `POST`  | `{"name": "Nike - Calle 85"}` |  404   |                                              `{"timestamp":"2025-11-22T03:43:52.973225800Z","status":404,"error":"No se encontro.","messages":["La franquicia con Id 4 no se encontro."]}`| La sucursal no esta registrada            |


|                 |          |                   | 422    |                                                                                                                                                       | A book with the same ISBN already exists. |
| `/books/{isbn}` |  `GET`   |                   | 200    |                                                                         Book                                                                          | Get the book with the given ISBN.         |
|                 |          |                   | 404    |                                                                                                                                                       | No book with the given ISBN exists.       |
| `/books/{isbn}` |  `PUT`   |       Book        | 200    |                                                                         Book                                                                          | Update the book with the given ISBN.      |
|                 |          |                   | 200    |                                                                         Book                                                                          | Create a book with the given ISBN.        |
| `/books/{isbn}` | `DELETE` |                   | 204    |                                                                                                                                                       | Delete the book with the given ISBN.      |



## 1. Crear franquicia

`POST /api/franchises`

```json
{
  "name": "Franquicia A"
}
```

---

## 2. Agregar sucursal a una franquicia
`POST /api/franchises/{id}/branches`

```json
{
  "name": "Sucursal Norte"
}
```

---

## 3. Agregar producto a una sucursal
`POST /api/branches/{id}/products`

```json
{
  "name": "Café",
  "stock": 50
}
```

---

## 4. Eliminar producto
`DELETE /api/products/{id}`

---

## 5. Actualizar stock de un producto
`PUT /api/products/{id}/stock`

```json
{
  "stock": 120
}
```

---

## 6. Producto con mayor stock por sucursal
`GET /api/franchises/{id}/top-products`

```json
[
  {
    "branch": "Sucursal Norte",
    "product": {
      "name": "Café",
      "stock": 150
    }
  }
]
```

---

# ⭐ Endpoints extra (Plus)

- Actualizar nombre de franquicia
- Actualizar nombre de sucursal
- Actualizar nombre de producto

---

# 🐳 Ejecutar con Docker

Construir imagen:

```bash
docker build -t franchise-app .
```

Ejecutar contenedor:

```bash
docker run -p 8080:8080 franchise-app
```

Ejecutar con Docker Compose:

```bash
docker-compose up --build
```

---

# 💻 Ejecutar en entorno local

## Requisitos

- Java 21
- Maven o Gradle
- Docker (opcional)
- Base de datos configurada (según tu implementación)

## 1. Clonar el repositorio

```bash
git clone https://github.com/<tu-user>/<repo>
cd <repo>
```

## 2. Configurar variables de entorno

Ejemplo:

```bash
export DB_HOST=localhost
export DB_PORT=5432
export DB_USER=postgres
export DB_PASSWORD=postgres
```

## 3. Ejecutar

```bash
./mvnw spring-boot:run
```

---

# 🧪 Pruebas

```bash
./mvnw test
```

---

# ☁️ Infraestructura como Código (Plus)

Si la app incluye una plantilla IaC:

El archivo se encuentra en `/infra` e incluye la provisión de recursos como:
- Base de datos
- ECS Fargate / EC2
- Redes (VPC, subnets, security groups)
- Roles IAM

---

# 📝 Flujo de trabajo en Git

Se utilizó un flujo basado en **GitFlow**:

- `main` – rama estable
- `develop` – rama para integración
- `feature/*` – funcionalidades independientes
- `fix/*` – correcciones

---

# 📄 Licencia

MIT – Libre para uso educativo o profesional.
