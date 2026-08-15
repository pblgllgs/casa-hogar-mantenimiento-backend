# Patrón de Proyecto — Familia Full-stack Java (Spring Boot + React)

> Documento de referencia para agentes de IA. Aplica a proyectos **Spring Boot + React**
> con backend monolítico por dominios, frontend Vite + Tailwind, MySQL, Flyway, JWT y
> Cloudinary. Referencia real: `E:\desarrollos 2026\casa-hogar-mantenimiento`.

---

## 1. Resumen en una línea

**Backend:** Spring Boot 3 (Java 17, Maven) con Spring Security (JWT), Spring Data JPA,
**Flyway** para migraciones, **springdoc-openapi** para la API y **Cloudinary** para imágenes.
**Frontend:** Vite + React + Tailwind con axios y SPA routing.
**Deploy:** Docker Compose (frontend Nginx + backend + MySQL).

- Idioma de la UI: **Español (Chile)**. Sin comentarios salvo que se pidan.
- Estructura backend **por dominio** (no por capas globales).
- Placeholders: `<proyecto>`, `<entidad>`, `<dominio>`.

---

## 2. Stack y herramientas (probado)

| Capa | Tecnología |
|---|---|
| Frontend | React 19, Vite 7, Tailwind 4, react-router-dom 7, axios, lucide-react, react-toastify, sweetalert, date-fns |
| Backend | Spring Boot 3.2, Java 17, Spring Security (JWT, jjwt 0.12), Spring Data JPA, Spring Validation, Mail, Actuator, MapStruct, Lombok |
| BD | MySQL 8 (Flyway para migraciones) |
| Almacenamiento | Cloudinary |
| API docs | springdoc-openapi (`/swagger-ui`) |
| Deploy | Docker Compose (frontend con Nginx + backend + MySQL) |

---

## 3. Estructura de carpetas

```
├── backend (raíz Maven)
│   ├── pom.xml
│   ├── src/main/java/com/<grupo>/<proyecto>/
│   │   ├── auth/                # login, JWT, security (config/controller/dto/entity/repository/service)
│   │   ├── common/              # utilidades y respuestas genéricas
│   │   └── <dominio>/           # assets, clinical, hr, inventory, location, maintenance,
│   │       │                    # medications, reporting, residents...
│   │       ├── controller/      # REST controllers (DTO in/out)
│   │       ├── dto/             # request/response (MapStruct)
│   │       ├── entity/          # entidades JPA
│   │       ├── repository/      # Spring Data repos
│   │       └── service/         # lógica de negocio
│   ├── src/main/resources/
│   │   └── db/migration/        # scripts Flyway V1__*.sql
│   └── src/test/                # tests (docker-compose.test.yml)
├── frontend/
│   ├── src/
│   │   ├── pages/ components/ lib/ (axios/api)
│   │   └── App / main
│   ├── vite.config.js|ts
│   ├── Dockerfile + nginx.conf  # SPA: try_files → /index.html
│   └── package.json
├── docker-compose.yml           # frontend + backend + mysql
├── Dockerfile                    # backend (build Maven → JRE)
└── .env.example                 # JWT_SECRET, MYSQL_*, CLOUDINARY_*
```

---

## 4. Convenciones backend

- **Por dominio:** cada módulo agrupa `controller/dto/entity/repository/service`; nada de capas globales.
- **REST:** controllers devuelven DTOs (no entidades); validación con `spring-boot-starter-validation`
  (`@Valid`, Bean Validation). MapStruct mapea entidad ↔ DTO.
- **Seguridad:** Spring Security + JWT (jjwt); `auth/security` configura filtros, rutas públicas
  vs autenticadas y roles. Contraseñas hasheadas (BCrypt).
- **Persistencia:** JPA; migraciones con **Flyway** (`V1__init.sql`, etc.). MySQL como motor.
- **Errores:** respuestas uniformes (helper en `common`).
- **API docs:** springdoc-openapi expone OpenAPI/Swagger en dev.

---

## 5. Convenciones frontend

- SPA con react-router-dom; capa de API en `lib` usando **axios** (baseURL al backend, token JWT
  en headers). Toast con `react-toastify`, modales/confirmaciones con `sweetalert`.
- Tailwind v4 (tokens en `@theme`); sin comentarios en código.

---

## 6. Deploy (Docker Compose)

```yaml
# docker-compose.yml (resumen)
services:
  frontend:   # build frontend, nginx.conf sirve dist con SPA fallback
  backend:    # build Maven → imagen JRE, expone el API (con CORS_ALLOWED_ORIGINS)
  mysql:      # imagen MySQL 8, env MYSQL_ROOT_USER/PASSWORD, volumen
```

- El `.env` en la raíz es obligatorio (`${VAR:?}`); sin él `docker compose` falla a propósito.
- Secrets: `JWT_SECRET` (≥32 bytes base64), `MYSQL_ROOT_PASSWORD`, `CLOUDINARY_*`.
- Tests: `docker-compose.test.yml` para levantar una BD de test.

---

## 7. Checklist para replicar

1. [ ] Backend Spring Boot (starters: web, security, data-jpa, validation, mail, actuator) + MySQL.
2. [ ] Flyway migraciones + entidades/repositorios JPA por dominio.
3. [ ] Seguridad JWT (login, filtros, roles) + DTOs con MapStruct + validación.
4. [ ] springdoc-openapi para documentar la API.
5. [ ] Frontend Vite + React + Tailwind + axios con capa de API y auth.
6. [ ] Cloudinary para imágenes (config en `.env`).
7. [ ] `Dockerfile` (backend y frontend) + `nginx.conf` + `docker-compose.yml` + `.env.example`.
8. [ ] Verificación: `mvn test` + build frontend + `docker compose up`.
