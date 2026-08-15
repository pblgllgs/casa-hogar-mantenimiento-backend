# Casa Hogar — Sistema de Mantenimiento

Sistema web para administrar el mantenimiento, inventario, residentes, personal y fichas clínicas de una casa de acogida.

## Stack

| Capa | Tecnología |
|------|-----------|
| Frontend | React 19, Vite 7, Tailwind 4, react-router-dom 7, axios |
| Backend | Spring Boot 3.2.5, Java 17, Spring Security (JWT), Spring Data JPA, Flyway, springdoc-openapi |
| Base de datos | MySQL 8.0 |
| Almacenamiento | Cloudinary (imágenes) |
| Despliegue | Docker Compose (frontend con Nginx, backend, MySQL) |

## Requisitos

- Docker + Docker Compose
- Node.js 20+ (solo desarrollo frontend)
- Java 17 + Maven 3.9+ (solo desarrollo backend)

## Configuración

El proyecto exige un archivo `.env` en la raíz. Sin él, `docker compose` falla a propósito (variables con `${VAR:?}`):

```env
# Secretos obligatorios
JWT_SECRET=<secreto base64 de al menos 32 bytes>
MYSQL_ROOT_PASSWORD=<password root de MySQL>
CLOUDINARY_CLOUD_NAME=<cloud>
CLOUDINARY_API_KEY=<key>
CLOUDINARY_API_SECRET=<secret>

# Opcionales
MYSQL_ROOT_USER=root
CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:5173
```

Generar un JWT_SECRET válido:

```bash
node -e "console.log(require('crypto').randomBytes(32).toString('base64'))"
```

## Arranque con Docker

```bash
docker compose up -d            # levantar todo
docker compose build frontend && docker compose up -d frontend   # rebuild solo frontend
docker compose build backend && docker compose up -d backend     # rebuild solo backend
docker compose logs -f backend  # logs
docker compose down             # detener (sin borrar el volumen mysql_data)
```

Servicios:

| Servicio | URL |
|----------|-----|
| Frontend (SPA) | http://localhost:3000 |
| Backend API | http://localhost:8080 (proxy: frontend `/api` → backend) |
| MySQL | `127.0.0.1:3306` (solo loopback) |
| Healthcheck | http://localhost:8080/actuator/health |

## Usuarios de prueba (seed)

Todos los usuarios seed usan la contraseña **`admin123`**. Los roles están en `user_roles` (la tabla `users` no tiene columna de rol).

| Usuario | Rol |
|---------|-----|
| admin | ADMIN |
| supervisor | SUPERVISOR |
| mantenimiento | MAINTENANCE |
| tecnico1 / tecnico2 | MAINTENANCE (+ SUPERVISOR en tecnico1) |
| inventario | INVENTORY |
| residencias | RESIDENTS |
| rrhh | HR |
| visualizar | VIEWER |

## Roles

`ADMIN`, `SUPERVISOR`, `MAINTENANCE`, `INVENTORY`, `RESIDENTS`, `HR`, `VIEWER` — definidos en `Role.java` (auth/entity).

## API (rutas base)

| Módulo | Base |
|--------|------|
| Autenticación | `/auth` |
| Activos | `/assets`, `/assets/locations` |
| Ubicaciones | `/location-photos` |
| Inventario | `/inventory` |
| Residentes | `/residents` |
| Personal (HR) | `/hr/staff`, `/hr/shifts`, `/hr/shift-logs` |
| Mantenimiento | `/maintenance/work-orders` |
| Fichas clínicas | `/clinical-records`, `/clinical-records/attachments` |
| Medicamentos | `/medications` |
| Reportes | `/reports` |
| Uploads | `/uploads` |
| Health | `/` (HealthController) |

Autenticación: `POST /auth/login` con `{ username, password }` devuelve `data.accessToken` (y `data.refreshToken`). Enviar `Authorization: Bearer <accessToken>`.

## Documentación API (Swagger)

Habilitada con `springdoc-openapi`. Disponible en:

- Swagger UI: <http://localhost:8080/api/swagger-ui/index.html>
- OpenAPI JSON: <http://localhost:8080/api/v3/api-docs>

Incluye esquema de seguridad `bearerAuth` (JWT) para probar endpoints protegidos desde el UI ("Authorize").

## Frontend (rutas)

`/login`, `/register`, `/`, `/mantenimiento`, `/activos`, `/ubicaciones`, `/ubicaciones/galeria`, `/inventario`, `/residentes`, `/residentes/:id`, `/personal`, `/personal/:id`, `/turnos`, `/historia`, `/fichas-clinicas`, `/fichas-clinicas/:residentId`, `/reportes`.

## Tests

```bash
# Backend (JUnit 5 + Mockito + Spring Boot Test)
mvn test

# Backend — unit tests only (no DB required)
mvn test -Dtest=JwtServiceTest,InventoryServiceTest

# Backend — integration tests against dedicated MySQL container
# Requires Docker running.  Levantar el contenedor de test:
docker compose -f docker-compose.test.yml up -d
# Detenerlo cuando no se use:
docker compose -f docker-compose.test.yml down
# MySQL de test: casahogar-test-mysql (127.0.0.1:3307, root/testpass123, BD casahogar_test).
# Perfil "integration" — application-integration.yml → JDBC 127.0.0.1:3307/casahogar_test.
mvn test -Dtest=InventoryServiceIntegrationTest

# Frontend (Vitest)
cd frontend
npm install --legacy-peer-deps
npm test
```

**Notas sobre tests de integración**.  El contenedor MySQL de test se gestiona con `docker compose -f docker-compose.test.yml` (puerto 3307, BD `casahogar_test`, perfil `integration` en `application-integration.yml`).  Testcontainers fue retirado del `pom.xml` por incompatibilidad con Docker Desktop 4.84.

## Notas de desarrollo

- Migraciones con Flyway en `src/main/resources/db/migration/` (`V1__create_initial_schema.sql`).
- Build del backend fuera de Docker:

```bash
docker run --rm -v "$(pwd)":/app -w /app -e MAVEN_CONFIG=/tmp/.m2 maven:3.9.6-eclipse-temurin-17 mvn clean package
```

- La imagen del frontend usa Nginx: sirve el bundle y hace proxy `/api` → `backend:8080` (SRI/Cache-Control para estáticos: 1 año, inmutable).
- `.env` no debe versionarse ni copiarse dentro de las imágenes (el Dockerfile del backend solo copia `pom.xml` y `src/`).
- Solo `/actuator/health` está expuesto públicamente; el resto del actuator devuelve 403 sin autenticación.
- Bugs de producción encontrados y corregidos en este turno:
  - `InventoryService.createItem/updateItem` ignoran el retorno de `save()` — en IDENTITY strategy de MySQL el id se asigna al objeto original en persist(), pero `merge()`（usado por Spring Data para entidades no-nuevas）devuelve una copia gestionada y el objeto original conserva `id=null`. Se captura el retorno para evitar ambigüedades según estrategia de generación.
  - `BaseEntity.getRoles()` default `Set.of(Role.VIEWER)` es inmutable — Hibernate llama `clear()` en la colección durante `merge` y `UnsupportedOperationException` bloquea cualquier persist/modificación de usuario. Se cambió a `new HashSet<>(Set.of(Role.VIEWER))`.
- Los secrets (JWT, Cloudinary, MySQL) no tienen defaults en código: vienen únicamente de variables de entorno.
