# Estudio Musical - Entregable Backend y Frontend

Proyecto web con backend Spring Boot, frontend Angular y base de datos MariaDB.

## Requisitos

- Git
- Docker Desktop o Docker Engine con Docker Compose

## Ejecutar con Docker

```powershell
git clone https://github.com/MHSS-SYSTEM/ZoneStyle-Project.git
cd ZoneStyle-Project
docker compose up --build
```

Cuando termine de levantar:

- Frontend: http://localhost:4000
- Backend: http://localhost:9090
- Base de datos MariaDB: localhost:3307

## Usuarios de prueba

- admin@zonestyle.com / admin159
- ingeniero@zonestyle.com / ingeniero159
- cliente@zonestyle.com / cliente159

## Ejecutar sin Docker

Backend:

```powershell
cd backend
mvn spring-boot:run
```

En este repositorio el backend esta en la raiz, por eso tambien puede ejecutarse desde la carpeta principal:

```powershell
mvn spring-boot:run
```

Frontend:

```powershell
cd frontend
npm install --legacy-peer-deps
npm run build
npm run serve:ssr:estudiomusical-frontend
```

## Variables importantes

El backend acepta estas variables de entorno:

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `JWT_SECRET`

En Docker Compose ya estan configuradas.
