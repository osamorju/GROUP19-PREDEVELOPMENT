# Running the Reports Locally and Publishing Them to GitHub

This guide explains how the population reports are generated on a developer's
computer and then published to the GitHub repository. GitHub stores the report
files after they are pushed; it does not generate them automatically.

## How the workflow fits together

1. The application in `src/main/java/com/napier/gp19/App.java` connects to a
   MySQL database containing the `world` schema.
2. The SQL dataset is in `database/db/world.sql`.
3. The application writes Markdown files to `reports/`. It generates the 32
   numbered report files there.
4. Git stages and commits changed report files. A push uploads that commit to
   the selected GitHub branch.

The `reports/` directory in GitHub is the published output. The latest
32-report commit is on the `preDevelopment` branch.

## Requirements

- JDK 24
- Maven 3.9 or later
- MySQL Server, or Docker Desktop with Docker Compose
- Git and access to the GitHub repository

The application currently connects with the `root` MySQL account and password
`root`, using the database location supplied as an argument (the default is
`localhost:33060`). These credentials are defined in the application and are
for the coursework development setup only; do not use them for a production
database.

## Prepare the database

The database image in `docker-compose.yml` initializes its `world` database
from `database/db/world.sql`. If using a local MySQL installation instead,
create/configure a development server on port `33060`, then import the dataset.
For example, in PowerShell from the repository root:

```powershell
Get-Content -Raw .\database\db\world.sql |
  mysql -uroot -p --host=127.0.0.1 --port=33060
```

Enter the MySQL password when prompted. The dump creates and loads the `world`
database, so only import it into a development instance where replacing that
database is acceptable.

## Build and run

From the repository root:

```powershell
mvn clean package
java -cp target\gp19CourseWork.jar com.napier.gp19.App localhost:33060 0
```

The application must be able to connect to MySQL. On success, it prints the
reports and writes Markdown output under `reports/`. The verified local run
used MySQL populated from the included SQL dataset.

With Docker Desktop available, the repository also defines a Compose setup:

```powershell
mvn package
docker compose up --build
```

The Compose app service runs the packaged application against its `db` service.
Stop the services with `docker compose down`.

## Publish reports to GitHub

After generating the reports, inspect the changes before committing:

```powershell
git status --short
git diff -- reports/
```

Then stage, commit, and push the report changes to `preDevelopment`:

```powershell
git add reports/
git commit -m "Generate population reports"
git push origin preDevelopment
```

GitHub authentication may open a sign-in flow through Git Credential Manager.
If Git does not know the commit author yet, configure a name and email locally
for this repository:

```powershell
git config user.name "Your Name"
git config user.email "your-verified-github-email@example.com"
```

To verify that the push completed:

```powershell
git fetch origin
git status -sb
```

The branch should no longer show an ahead count. Open the repository's
`preDevelopment` branch and browse the `reports/` directory to view the
published output.

Git only lists files whose contents changed in a commit. If some reports are
unchanged, they remain available in the branch but will not appear in that
commit's changed-file list. Do not make artificial edits just to make an
unchanged file appear as changed.

## Current published output

- Branch: `preDevelopment`
- Reports directory: [`reports/`](https://github.com/osamorju/GROUP19-PREDEVELOPMENT/tree/preDevelopment/reports)
- Report commit: [`7ea59cc`](https://github.com/osamorju/GROUP19-PREDEVELOPMENT/commit/7ea59cc1551b982616ac962781a7d5118075cbd4)
