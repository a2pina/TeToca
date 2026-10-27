TeToca
<!-- TODO: logotipo y eslogan -->

Aplicación para personas que comparten piso que reparte las tareas del hogar de forma equitativa. Cada tarea tiene un peso en puntos según su esfuerzo, y el reparto busca que todos los convivientes acumulen una carga similar.

Estado: en desarrollo · Próxima entrega: Demo 1, 13 de noviembre de 2026

Funcionalidades

Demo 1 (13 de noviembre)

Registrar un piso.
Añadir y consultar los convivientes del piso.
Crear y consultar las tareas del piso, cada una con sus puntos.
Asignar tareas a un conviviente con fecha límite.
Marcar tareas como completadas.
Consultar tareas por estado: pendiente, completada o vencida.
Acceso con usuario y contraseña, con perfiles de administrador y conviviente.

Versiones posteriores

Reparto semanal automático y equitativo por puntos.
App móvil con tablero de tareas y avisos de tareas vencidas.
Notificaciones, intercambio de tareas y ausencias.
Tecnologías
Capa	Tecnología
Backend	Java 21, Spring Boot 4 (Web, Data JPA, Validation, Security)
Base de datos	PostgreSQL 17 en Docker, migraciones con Flyway
Documentación de la API	springdoc-openapi (Swagger UI)
Estructura del repositorio
src/main/java/com/pisocompartido/backendtetoca/
├─ controller/     rutas de la API
├─ service/        lógica de negocio
├─ repository/     consultas a la base de datos
├─ model/          entidades
├─ dto/            datos que entran y salen
├─ config/         configuración, filtros y seguridad
└─ exception/      gestión de errores
src/main/resources/
├─ application.properties
└─ db/migration/   scripts de Flyway
docs/              documentación del proyecto
compose.yaml       base de datos PostgreSQL
pom.xml            dependencias (Maven)
Ejecución

Requisitos: JDK 21 y Docker Desktop.

bash
docker compose up -d      # Arranca PostgreSQL en el puerto 5433
./mvnw spring-boot:run    # En Windows: .\mvnw.cmd spring-boot:run

La API queda disponible en http://localhost:8080.

Flujo de trabajo
Cada tarea del proyecto es una issue de GitHub.
Se trabaja en una rama por issue: backend/... o docs/.... Nunca directamente en main.
Los commits siguen Conventional Commits: feat(backend): crear y consultar pisos.
Los cambios entran en main mediante Pull Request, con Closes #N en la descripción y revisión del otro miembro del equipo.