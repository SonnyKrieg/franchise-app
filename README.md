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

|           Endpoint	            |  Method  |          Req. body  (Ejemplo)          | Status |                                                                                                       Resp. body                                                                                                        | Description    		   	                                                 |
|:------------------------------:|:--------:|:--------------------------------------:|:------:|:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------|
|          `/franchise`          |  `POST`  |           `{"name":"Nike"}`            |  201   |                                                                                                      FranchiseDto                                                                                                       | Registra una franquicia                                               |
|          `/franchise`          |  `POST`  |           `{"name":"Nike"}`            |  400   |                                  `{"timestamp":"2025-11-22T03:32:05.795048800Z","status":400,"error":"Solicitud invalida.","messages":["El nombre Nike ya se encuentra registrado."]}`                                  | El nombre ingresado ya se esta registrado                             |
|          `/franchise`          |  `POST`  |           `{"name":"Nike"}`            |  400   |                                                                                                                                                                                                                         | Nombre invalido                                                       |
| `/franchise/{franchise_id}/max-stock` |  `GET`   |           |  200   | `[{"productId":2,"productName":"Jordan 3","productStock":2222,"branchId":1,"branchName":"Nike - Calle 45"},{"productId":4,"productName":"Airmax 90","productStock":66666,"branchId":2,"branchName":"Nike - Calle 85"}]` | Devuelve el producto con mas stock de cada sucursal de una franquicia |
| `/franchise/{franchise_id}/max-stock` |  `GET`   |           |  404   |                                      `{"timestamp":"2025-11-22T03:43:52.973225800Z","status":404,"error":"No se encontro.","messages":["La franquicia con Id 4 no se encontro."]}`                                      | No se encontro la franquicia                                          |
| `/franchise/{franchise_id}/max-stock` |  `GET`   |           |  400   |                                      `{"timestamp":"2025-11-22T03:43:52.973225800Z","status":404,"error":"Solicitud invalida.","messages":["La franquicia no tiene sucursales."]}`                                      | La franquicia no tiene sucursales                                     |
| `"/{franchise_id}/branch/add"` |  `POST`  |     `{"name": "Nike - Calle 85"}`      |  201   |                                                                                                                BranchDto| Agregar una sucursal a una franquicia         |
| `"/{franchise_id}/branch/add"` |  `POST`  |     `{"name": "Nike - Calle 85"}`      |  400   |                                    `{"timestamp":"2025-11-22T03:38:18.406993100Z","status":400,"error":"Solicitud invalida.","messages":["El nombre Nike - Calle 85 ya se encuentra registrado."]}`| Nombre de sucursal duplicado                  |
| `"/{franchise_id}/branch/add"` |  `POST`  |     `{"name": "Nike - Calle 85"}`      |  404   |                                              `{"timestamp":"2025-11-22T03:43:52.973225800Z","status":404,"error":"No se encontro.","messages":["La franquicia con Id 4 no se encontro."]}`| La sucursal no esta registrada                |
|  `"/{branch_id}/product/add"`  |  `POST`  | `{"name": "Airmax 90", "stock": 1111}` |  201   |             ProductoDto                       | Registra un producto en una sucursal          |
|  `"/{branch_id}/product/add"`  |  `POST`  | `{"name": "Airmax 90", "stock": 1111}` |  400   |`{"timestamp":"2025-11-22T03:52:55.326874500Z","status":400,"error":"Solicitud invalida.","messages":["La sucursal 2 ya cuenta con un producto llamado Airmax 90."]}`| El producto ya esta registrado en la sucursal |
|   `"/product/{product_id}"`    | `DELETE` |  |  204   |                                    | Elimina un producto                           |
|   `"/product/{product_id}"`    | `DELETE` |  |  404   |`{"timestamp":"2025-11-22T03:57:10.898521800Z","status":404,"error":"No se encontro.","messages":["El producto con Id 45 no se encontro."]}`| El producto no se encontro                    |
|   `/product/{product_id}/stock`    | `PATCH`  | `{"newStock": 16}` |  200   |   ProductDto      | Actualiza el stock de un producto                          |
|   `/product/{product_id}/stock"`   | `PATCH`  |  |  404   |`{"timestamp":"2025-11-22T03:57:10.898521800Z","status":404,"error":"No se encontro.","messages":["El producto con Id 45 no se encontro."]}`| El producto no se encontro                    |
|   `/franchise/{franchise_id}/name`   |  `PATCH`  | `{"name": "Adidas"}` |  200   |   FranchisetDto      | Actualiza el nombre de franquicia                          |
|   `/franchise/{franchise_id}/name`   |  `PATCH`  | `{"name": "Adidas"}`|  404   |`{"timestamp":"2025-11-22T03:57:10.898521800Z","status":404,"error":"No se encontro.","messages":["La franquicia con Id 45 no se encontro."]}`| La franquicia no se encontro                    |
|   `/branch/{branch_id}/name`   |  `PATCH`  | `{"name": "Adidas" - calle 66"}` |  200   |   BranchDto      | Actualiza el nombre de sucursal                          |
|   `/branch/{branch_id}/name`   |  `PATCH`  | `{"name": "Adidas" - calle 66}`|  404   |`{"timestamp":"2025-11-22T03:57:10.898521800Z","status":404,"error":"No se encontro.","messages":["La sucursal con Id 45 no se encontro."]}`| La sucursal no se encontro                    |
|   `/product/{product_id}/name`   |  `PATCH`  | `{"name": "Adidas Forum"}` |  200   |   ProductDto      | Actualiza el nombre de producto                         |
|   `/product/{product_id}/name`   |  `PATCH`  | `{"name": "Adidas Forum"}`|  404   |`{"timestamp":"2025-11-22T03:57:10.898521800Z","status":404,"error":"No se encontro.","messages":["El producto con Id 45 no se encontro."]}`| El producto no se encontro                    |

# Ejecutar local con docker 🐳

Construir imagen:

1. Clone el repositorio:

```bash
git clone https://github.com/SonnyKrieg/franchise-app
```

2. Cree el Jar ejecutando:
```bash
./mvnw clean package -DskipTests
```
3. Ejecute:

```bash
docker build -t franchise-app .
```

4. Vaya a la carpeta franchise-deployment/dockere y ejecute:

```bash
docker-compose up 
```
5. Pruebe la API con localhost:8080
---

# ☁️ Infraestructura como Código (Plus)

Se creo una plantilla con CloudFormation:

El archivo se encuentra en `/franchise/deployment` e incluye la provisión de recursos como:
- Base de datos (RDS)
- EC2
- Redes (VPC, subnets, security groups)
- Roles IAM

Puede crear un stack en AWS y probarla

---

# 📝 Flujo de trabajo en Git

Se utilizó un flujo basado en **GitFlow**:

- `main` – rama estable
- `feature/*` – funcionalidades
- `chore/*` – tareas triviales

---

# Pruebas

Todos los casos de uso tienen pruebas unitarias y todos los endpoint se probaron con Postman

# Flujo de integración continua:

Se creo un flujo de integración continua usando GitHub Actions
para: 
- Automatizar pruebas y compilación para detectar fallos temprano.
- Escanear vulnerabilidades tanto en el código fuente como en la imagen de contenedor.
- Publicar automáticamente una imagen en el registro cuando el código llega a main, facilitando despliegues continuos
