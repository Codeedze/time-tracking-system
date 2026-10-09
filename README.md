# Time Tracking System

A Java Swing desktop application for tracking employee work hours, built for the SCSE 3rd Semester Live Project.

## Team Members

| Name | Roll Number |
|---|---|
| Kshitiz Shukla | 25SCSE1011077 |
| Siddhansh Dubey | 25SCSE1010932 |
| Ali Hassan | 25SCSE1011099 |
| Ansh Sharma | 25SCSE1010980 |

**Course:** 3rd Semester SCSE — 2029 Passout  
**University:** Galgotias University  
**Program:** GUVI Live Project

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Frontend | Java Swing (GUI) |
| Database | MariaDB (MySQL-compatible) |
| Connectivity | JDBC + PreparedStatement |
| Charts | JFreeChart 1.5.4 |
| Concurrency | Java Threads + `synchronized` |
| Driver | MariaDB Connector/J 3.3.5 |

---

## Features

### Admin
- User management (Create, Read, Update, Delete)
- View all employees' time logs
- Reports with bar, pie, and log-count charts using JFreeChart
- System settings for work hours and break time
- Transactional user deletion (user and related logs deleted atomically)

### Employee
- Log daily work hours with validation
- View personal logs and total hours
- Personal productivity reports

---

## Architecture

```text
GUI (Java Swing)
      |
      v
Service Layer (Interfaces + Implementations)
      |
      v
DAO Layer (JDBC + PreparedStatement + Transactions)
      |
      v
MariaDB (users | time_logs | settings)
```

## Package Structure

| Package | Purpose |
|---|---|
| `src/db/` | Singleton `DBConnection` |
| `src/model/` | `Person`, `User`, `Admin`, `Employee`, `TimeLog`, `Settings` |
| `src/exception/` | Four custom exceptions |
| `src/dao/` | `UserDAO`, `TimeLogDAO`, `SettingsDAO` |
| `src/service/` | `UserService`, `TimeLogService` (interfaces and implementations) |
| `src/thread/` | `AutoSaveThread`, `ReportThread` |
| `src/util/` | `TimeLogManager` (synchronized singleton) |
| `src/gui/` | `LoginFrame`, `AdminDashboard`, `EmployeeDashboard`, and panels |

---

## Setup Instructions

### 1. Prerequisites

- JDK 17 or higher
- MariaDB or MySQL
- A Java IDE, such as VS Code, IntelliJ IDEA, or Eclipse

### 2. Create the Database

Run the following SQL in MariaDB:

```sql
CREATE DATABASE time_tracking_db;
USE time_tracking_db;

CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL
);

CREATE TABLE time_logs (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    log_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    task VARCHAR(200) NOT NULL,
    description TEXT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE settings (
    id INT PRIMARY KEY,
    work_hours DOUBLE DEFAULT 8.0,
    break_time DOUBLE DEFAULT 1.0
);

INSERT INTO settings (id, work_hours, break_time)
VALUES (1, 8.0, 1.0);

INSERT INTO users (name, email, password, role)
VALUES ('Admin', 'admin@tts.com', 'admin123', 'ADMIN');
```

### 3. Configure the Database Password

Open `src/db/DBConnection.java` and update the password constant:

```java
private static final String PASSWORD = "your_mariadb_password";
```

Replace `your_mariadb_password` with your local MariaDB password. Do not commit real passwords or other secrets to a public repository.

### 4. Compile

Run this command from the project root on Windows:

```bash
javac -cp "lib/mariadb-java-client-3.3.5.jar;lib/jfreechart-1.5.4.jar;lib/jcommon-1.0.24.jar" -d bin src/db/*.java src/model/*.java src/exception/*.java src/dao/*.java src/service/*.java src/thread/*.java src/util/*.java src/gui/*.java
```

On macOS or Linux, replace semicolons (`;`) in the classpath with colons (`:`).

### 5. Run

On Windows:

```bash
java -cp "bin;lib/mariadb-java-client-3.3.5.jar;lib/jfreechart-1.5.4.jar;lib/jcommon-1.0.24.jar" gui.LoginFrame
```

On macOS or Linux, replace the classpath semicolon (`;`) with a colon (`:`).

---

## Default Credentials

| Role | Email | Password |
|---|---|---|
| Admin | `admin@tts.com` | `admin123` |

Additional employee accounts can be created through **Admin → Users**.

> Change the default admin password before using the application beyond local testing.

---

## Rubric Coverage

| Rubric Item | Marks | Where Implemented |
|---|---:|---|
| OOP — Inheritance, Polymorphism, Exceptions, Interfaces | 10 | `model/`, `service/`, `exception/` |
| Collections & Generics | 6 | `dao/`, `gui/ReportPanel.java` |
| Multithreading & Synchronization | 4 | `thread/`, `util/TimeLogManager.java` |
| Database Operation Classes | 7 | `db/DBConnection.java`, `dao/*DAO.java` |
| JDBC CRUD + PreparedStatement | 3 | All DAO classes |
| Transaction Management | 3 | `dao/UserDAO.deleteUserWithLogs()` |
| **Total** | **33** | |

---

## How the App Works

1. **Login:** The user enters an email and password. The app opens the Admin or Employee dashboard based on the user's role.
2. **Admin:** Can manage users, view all logs, generate reports, and configure settings.
3. **Employee:** Can log work hours, view personal logs, and see productivity charts.
4. **AutoSave:** A background daemon thread flushes pending logs every 30 seconds.
5. **Reports:** JFreeChart renders bar, pie, and log-count charts from database data.

---

## Notes

- The `lib/` folder contains the required JAR files.
- Database operations use `PreparedStatement` to help protect against SQL injection.
- Transactions use `setAutoCommit(false)` with explicit `commit()` and `rollback()`.
- `AutoSaveThread` runs as a daemon and shuts down with the application.
