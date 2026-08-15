# Patrón de Proyecto — Familia Full-stack Java (Spring Boot + React)

> Documento de referencia para agentes de IA. Aplica a proyectos **Spring Boot + React**
> con backend monolítico por dominios, frontend Vite + Tailwind, MySQL, Flyway, JWT y
> Cloudinary. Referencia real: repos `casa-hogar-mantenimiento-backend` y
> `casa-hogar-mantenimiento-frontend`.

---

## 1. Resumen en una línea

**Backend:** Spring Boot 3 (Java 17, Maven) con Spring Security (JWT), Spring Data JPA,
**Flyway** para migraciones, **springdoc-openapi** para la API y **Cloudinary** para imágenes.
**Frontend:** Vite + React + Tailwind con axios y SPA routing servido por nginx.
**Deploy:** Docker Hub + Render. CI/CD con GitHub Actions (build, versionado y deploy automático).

- Idioma de la UI: **Español (Chile)**. Sin comentarios salvo que se pidan.
- Estructura backend **por dominio** (no por capas globales).
- **Dos repos separados** en GitHub (backend y frontend), cada uno con su propio workflow.
- Placeholders: `<proyecto>`, `<entidad>`, `<dominio>`.

---

## 2. Stack y herramientas (probado)

| Capa | Tecnología |
|---|---|
| Frontend | React 19, Vite 7, Tailwind 4, react-router-dom 7, axios, lucide-react, react-toastify, sweetalert, date-fns |
| Backend | Spring Boot 3.2, Java 17, Spring Security (JWT, jjwt 0.12), Spring Data JPA, Spring Validation, Mail, Actuator, MapStruct, Lombok |
| BD | TiDB Cloud (MySQL 8 compatible, puerto 4000, SSL) o MySQL 8 local |
| Almacenamiento | Cloudinary |
| API docs | springdoc-openapi (`/swagger-ui`) |
| CI/CD | GitHub Actions → Docker Hub → Render |
| Deploy | Render (web services) — MySQL puede ser MySQL local (Docker) o TiDB Cloud |

---

## 3. Estructura de carpetas (monorepo local → 2 repos remotos)

```
casa-hogar-mantenimiento/            # contenedor local de los 2 repos
├── casa-hogar-mantenimiento-backend/    # repo GitHub: pblgllgs/casa-hogar-mantenimiento-backend
│   ├── pom.xml
│   ├── Dockerfile                        # build Maven → imagen JRE
│   ├── docker-compose.yml                # mysql + backend + frontend (local)
│   ├── .github/workflows/docker-build.yml# build + push Docker Hub + deploy Render
│   ├── PATRON-PROYECTO.md
│   └── src/main/java/com/<grupo>/<proyecto>/
│       ├── auth/                # login, JWT, security (config/controller/dto/entity/repository/service)
│       ├── common/              # utilidades y respuestas genéricas
│       └── <dominio>/           # assets, clinical, hr, inventory, location, maintenance,
│           │                    # medications, reporting, residents...
│           ├── controller/      # REST controllers (DTO in/out)
│           ├── dto/             # request/response (MapStruct)
│           ├── entity/          # entidades JPA
│           ├── repository/      # Spring Data repos
│           └── service/         # lógica de negocio
│   └── src/main/resources/db/migration/  # scripts Flyway V1__*.sql
└── casa-hogar-mantenimiento-frontend/   # repo GitHub: pblgllgs/casa-hogar-mantenimiento-frontend
    ├── Dockerfile                        # node build → nginx (con entrypoint)
    ├── nginx.conf.template               # proxy /api → NGINX_BACKEND_URL (envsubst)
    ├── docker-entrypoint.sh              # sustituye NGINX_BACKEND_URL y Host
    ├── .github/workflows/docker-build.yml# build + push Docker Hub + deploy Render
    ├── PATRON-PROYECTO.md
    └── src/
        ├── pages/ components/ api/ context/ constants/ utils/
        └── App / main
```

---

## 4. Convenciones backend

- **Por dominio:** cada módulo agrupa `controller/dto/entity/repository/service`; nada de capas globales.
- **REST:** controllers devuelven DTOs (no entidades); validación con `spring-boot-starter-validation`
  (`@Valid`, Bean Validation). MapStruct mapea entidad ↔ DTO.
- **Seguridad:** Spring Security + JWT (jjwt); `auth/security` configura filtros, rutas públicas
  vs autenticadas y roles. Contraseñas hasheadas (BCrypt). `@PreAuthorize` por endpoint.
- **Persistencia:** JPA; migraciones con **Flyway** (`V1__init.sql`, etc.). MySQL/TiDB como motor.
  `spring.docker.compose.enabled` debe ir a `false` en producción (Render).
- **Errores:** respuestas uniformes `ApiResponse<T>` en `common`.
- **CORS:** `CORS_ALLOWED_ORIGINS` (lista separada por comas) en `application.yml`/env.
- **API docs:** springdoc-openapi expone OpenAPI/Swagger.

---

## 5. Convenciones frontend

- SPA con react-router-dom; capa de API en `src/api` usando **axios** (baseURL `/api`, token JWT
  en headers). Toast con `react-toastify`, modales/confirmaciones con `sweetalert`.
- Tailwind v4; sin comentarios en código.
- **Roles:** helper `isViewer(user)` en `src/utils/roles` — solo es VIEWER si es su ÚNICO rol
  (los usuarios multi-rol como admin NO se consideran viewer).
- **Subida de archivos:** selección local con preview; subir a Cloudinary solo al confirmar
  (evita fotos huérfanas si se cancela).

---

## 6. Deploy (CI/CD con GitHub Actions + Render)

### Workflow (`docker-build.yml`) — push a main/master
1. Login a Docker Hub (`DOCKERHUB_USERNAME`, `DOCKERHUB_TOKEN`).
2. Calcula versión incremental consultando los tags existentes en Docker Hub (`0.0.6` → `0.0.7`).
3. Build + push: `<imagen>:0.0.X` y `<imagen>:latest`.
4. PATCH del servicio en Render con el tag nuevo + POST deploy (usa `RENDER_API_KEY`,
   `RENDER_SERVICE_ID` y `RENDER_OWNER_ID`).

### Secrets necesarios en GitHub (Settings → Secrets → Actions)
| Secret | Descripción |
|---|---|
| `DOCKERHUB_USERNAME` | Usuario de Docker Hub |
| `DOCKERHUB_TOKEN` | Token de acceso de Docker Hub |
| `RENDER_API_KEY` | API key de Render (`rnd_...`) |

### Variables de entorno del backend (Render)
```
SPRING_DATASOURCE_URL=jdbc:mysql://<host>:4000/<db>?sslMode=REQUIRED&serverTimezone=America/Santiago&allowPublicKeyRetrieval=true
MYSQL_ROOT_USER=<user>
MYSQL_ROOT_PASSWORD=<pass>
JWT_SECRET=<secreto>
CLOUDINARY_CLOUD_NAME=<name>
CLOUDINARY_API_KEY=<key>
CLOUDINARY_API_SECRET=<secret>
CORS_ALLOWED_ORIGINS=https://<frontend-host>.onrender.com
SPRING_DOCKER_COMPOSE_ENABLED=false
```

### Variables de entorno del frontend (Render)
```
NGINX_BACKEND_URL=https://<backend-host>.onrender.com/api
```

### Detalles del nginx frontend (importantes)
- `nginx.conf.template` usa `${NGINX_BACKEND_URL}` y `${NGINX_BACKEND_HOST}`; el
  `docker-entrypoint.sh` los sustituye en runtime (no rebuild).
- **`proxy_set_header Host` debe ser el host del BACKEND**, no `$host` — de lo contrario
  Render detecta un loop (`508 x-render-routing: loop`).
- `proxy_ssl_server_name on` + TLS 1.2/1.3 son necesarios para el handshake con Cloudflare.

### TiDB Cloud (si se usa en vez de MySQL local)
- Puerto **4000** (no 3306), TLS obligatorio, crear la BD manualmente (no
  `createDatabaseIfNotExist`). Flyway ejecuta las migraciones al arrancar.

---

## 7. Checklist para replicar

1. [ ] Backend Spring Boot (starters: web, security, data-jpa, validation, mail, actuator) + MySQL.
2. [ ] Flyway migraciones + entidades/repositorios JPA por dominio.
3. [ ] Seguridad JWT (login, filtros, roles) + DTOs con MapStruct + validación.
4. [ ] springdoc-openapi para documentar la API.
5. [ ] Frontend Vite + React + Tailwind + axios con capa de API y auth.
6. [ ] Cloudinary para imágenes (config en `.env`).
7. [ ] `Dockerfile` (backend y frontend) + `nginx.conf.template` + `docker-entrypoint.sh`.
8. [ ] Workflow GitHub Actions por repo + secrets (`DOCKERHUB_*`, `RENDER_API_KEY`).
9. [ ] Verificación: `mvn test` + build frontend + `docker compose up` local.
