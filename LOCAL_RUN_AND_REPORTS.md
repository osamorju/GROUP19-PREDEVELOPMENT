# Running the Reports Locally and Publishing Them

The project uses two containers:

1. The `db` container runs MySQL and loads `database/db/world.sql`.
2. The `app` container runs the compiled Java application and creates 32
   Markdown reports.

Docker Compose starts the database first. It waits for MySQL to become healthy
before it starts the application.

## Requirements

To use the published images, install Docker with Docker Compose.

To work directly with the Java source, also install:

- JDK 24
- Maven 3.9 or later
- Git

## School Database Settings

The Compose setup uses the MySQL `root` account with `root` as its password.
This keeps the school project simple.

Do not use this setup in production. Production should use a limited database
user, a strong unique password, and Docker secrets or a managed secret service.

## Build Both Images from Source

Run this command from the repository root:

```bash
docker compose build
```

The application image uses a multi-stage build. Its first stage compiles the
source and creates `gp19CourseWork.jar`. Its final stage contains only the Java
runtime, the compiled JAR, and the reports directory.

The database image uses MySQL 8.4 and includes the World SQL dataset.

## Generate Reports from Local Images

```bash
docker compose up --no-build --abort-on-container-exit --exit-code-from app
docker compose cp app:/app/reports/. reports/
docker compose down --volumes
```

The `--exit-code-from app` option makes the command fail if the report
application fails. Removing the volume gives the next run a clean database.

## Check Container Health

Use these commands when debugging:

```bash
docker compose ps --all
docker compose logs db
docker compose logs app
```

The database shows `healthy` when MySQL accepts connections. The application
shows `healthy` while it runs. After it generates the reports, `Exited (0)`
means it completed successfully.

## Generate Reports from Public GHCR Images

The workflow publishes:

- `ghcr.io/osamorju/group19-predevelopment-app:latest`
- `ghcr.io/osamorju/group19-predevelopment-database:latest`

Pull and run them:

```bash
docker compose pull
docker compose up --no-build --abort-on-container-exit --exit-code-from app
docker compose cp app:/app/reports/. reports/
docker compose down --volumes
```

No Java, Maven, or local MySQL installation is required for this method.

GitHub creates new container packages as private packages. After the first
successful workflow run, the repository owner must open each package setting
and change its visibility to **Public**. Public images can be pulled without
running `docker login`.

## Run the Java Application Directly

Start only the database:

```bash
docker compose build db
docker compose up --detach --no-build --wait db
```

```bash
mvn clean package
java -jar target/gp19CourseWork.jar localhost:33060 1000
```

Stop the database when finished:

```bash
docker compose down --volumes
```

## GitHub Actions Pipeline

The workflow on the `master` branch performs these stages:

1. Run unit and integration tests while both container images build.
2. Build AMD64 and ARM64 application and database images.
3. Publish immutable `sha-<commit>` image tags.
4. Pull those tags and generate all 32 reports.
5. Point `latest` to the tested images.
6. Release the JAR copied from the tested app image.
7. Update the `reports` branch with the tested reports.

Pull requests build and test the two images but do not publish them.
Documentation-only changes do not start the full container pipeline.

## Current Published Output

- Source branch: `master`
- Reports branch: `reports`
- Reports directory:
  <https://github.com/osamorju/GROUP19-PREDEVELOPMENT/tree/reports>
