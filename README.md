# LabLogix
LabLogix is a real-time tracking app that helps students see lab availability in real time.

## First-Time Setup (Everyone)

1. Clone the repo.
2. Make sure the MySQL connector JAR exists in [lib/mysql-connector-j-9.6.0.jar](lib/mysql-connector-j-9.6.0.jar).
3. Create two env files:
   1. Repo root: [.env](.env)
   2. Website folder: [website/.env](website/.env)

Use this format in both files:

```bash
DB_HOST=localhost
DB_PORT=3306
DB_USER=root
DB_PASSWORD=your_mysql_password
DB_NAME=lablogix
```

Optional Java-only alternative:
- Instead of repo-root [.env](.env), you can create [.env.java](.env.java) with the same keys.
- You can also use DB_URL directly, example:

```bash
DB_URL=jdbc:mysql://localhost:3306/lablogix
DB_USER=root
DB_PASSWORD=your_mysql_password
```

## Requirements

- Java JDK installed (javac and java available)
- Node.js + npm installed (for website)
- MySQL server running and schema loaded

## Run Interactive Terminal App

### Windows
1. Open terminal in repo root.
2. Run:

```bash
scripts\run-terminal.cmd
```

### macOS
1. Open terminal in repo root.
2. Make script executable (one-time):

```bash
chmod +x scripts/run-terminal.sh
```

3. Run:

```bash
./scripts/run-terminal.sh
```

## Run Website Server

### Windows
1. Open terminal in repo root.
2. Run:

```bash
scripts\run-website.cmd
```

### macOS
1. Open terminal in repo root.
2. Make script executable (one-time):

```bash
chmod +x scripts/run-website.sh
```

3. Run:

```bash
./scripts/run-website.sh
```

The website script auto-installs dependencies if [website/node_modules](website/node_modules) is missing.

## macOS Troubleshooting

If still cannot run:

1. Check Java is installed:

```bash
java -version
```

```bash
javac -version
```

2. Check Node and npm:

```bash
node -v
```

```bash
npm -v
```

3. Confirm they are running from repo root (not inside another folder).
4. Confirm [lib/mysql-connector-j-9.6.0.jar](lib/mysql-connector-j-9.6.0.jar) exists.
5. Confirm both [.env](.env) and [website/.env](website/.env) exist and have correct DB credentials.
6. If script permissions were lost after pulling:

```bash
chmod +x scripts/run-terminal.sh
chmod +x scripts/run-website.sh
```

7. If a script still fails, run commands manually from repo root:

```bash
find src -name "*.java" > out/sources.txt
javac -d out @out/sources.txt
java -cp "out:lib/mysql-connector-j-9.6.0.jar" src.Main
```

And for website:

```bash
cd website
npm install
npm start
```