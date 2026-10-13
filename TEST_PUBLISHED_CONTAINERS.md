# Testing the Published Containers

You can run the published containers from a `test` folder and save the reports
under `test/reports`.

## Requirements

Install and start Docker Desktop before running these commands.

You do not need Java, Maven, or MySQL. The published containers include
everything needed to run the application.

## Folder Layout

The `test` folder and repository folder should be next to each other:

```text
SET09803 2026-7 TR1 001 - DevOps Global Online/
├── GROUP19-PREDEVELOPMENT/
│   └── docker-compose.yml
└── test/
    └── reports/
```

## 1. Create the Test Folder

Open a terminal in the folder that contains `GROUP19-PREDEVELOPMENT`, then run:

```bash
mkdir -p test/reports
cd test
```

## 2. Pull the Published Images

```bash
docker compose \
  -f ../GROUP19-PREDEVELOPMENT/docker-compose.yml \
  pull
```

This pulls:

- `ghcr.io/osamorju/group19-predevelopment-app:latest`
- `ghcr.io/osamorju/group19-predevelopment-database:latest`

The packages must be public. Public packages do not require `docker login`.

## 3. Run the Application

```bash
docker compose \
  -f ../GROUP19-PREDEVELOPMENT/docker-compose.yml \
  up \
  --no-build \
  --abort-on-container-exit \
  --exit-code-from app
```

Docker Compose will:

1. Start the MySQL database.
2. Wait until the database is healthy.
3. Start the Java application.
4. Generate all 32 reports.
5. Stop after the application finishes.

`Exited (0)` means the application completed successfully.

## 4. Copy the Reports

Run this command before removing the containers:

```bash
docker compose \
  -f ../GROUP19-PREDEVELOPMENT/docker-compose.yml \
  cp app:/app/reports/. ./reports/
```

The generated files are now under:

```text
test/reports/
```

## 5. Verify the Reports

Count the Markdown report files:

```bash
find reports -maxdepth 1 -type f -name '*.md' | wc -l
```

The expected result is:

```text
32
```

List the report files:

```bash
ls -la reports
```

## 6. Clean Up

Remove the containers, network, and test database volume:

```bash
docker compose \
  -f ../GROUP19-PREDEVELOPMENT/docker-compose.yml \
  down --volumes
```

The copied files under `test/reports` remain available after cleanup.

## Complete Command Sequence

Run these commands from the folder that contains `GROUP19-PREDEVELOPMENT`:

```bash
mkdir -p test/reports
cd test

docker compose \
  -f ../GROUP19-PREDEVELOPMENT/docker-compose.yml \
  pull

docker compose \
  -f ../GROUP19-PREDEVELOPMENT/docker-compose.yml \
  up \
  --no-build \
  --abort-on-container-exit \
  --exit-code-from app

docker compose \
  -f ../GROUP19-PREDEVELOPMENT/docker-compose.yml \
  cp app:/app/reports/. ./reports/

find reports -maxdepth 1 -type f -name '*.md' | wc -l
ls -la reports

docker compose \
  -f ../GROUP19-PREDEVELOPMENT/docker-compose.yml \
  down --volumes
```

## Debugging

Show container health and exit status:

```bash
docker compose \
  -f ../GROUP19-PREDEVELOPMENT/docker-compose.yml \
  ps --all
```

Show database logs:

```bash
docker compose \
  -f ../GROUP19-PREDEVELOPMENT/docker-compose.yml \
  logs db
```

Show application logs:

```bash
docker compose \
  -f ../GROUP19-PREDEVELOPMENT/docker-compose.yml \
  logs app
```

If the application shows `Exited (0)`, it completed successfully. Any non-zero
exit code means the application failed and its logs should be checked.
