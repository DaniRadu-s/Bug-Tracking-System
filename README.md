# Bug Tracking System

A multi-module client-server application for managing software bugs, user roles, and workflow in a development team. The system supports multiple user types (Admin, Tester, Programmer) and provides dedicated interfaces for each role using **JavaFX**, **Hibernate ORM**, and **SQLite**.

## 🚀 Features

### 🔧 Core Functionality
- Manage **bugs**, **testers**, **programmers**, and **admins**.
- Client-server architecture with clearly separated modules.
- Real-time interaction between clients and the server.
- Persistent data storage using **Hibernate ORM** on top of **SQLite**.

### 👥 Role-Based Interfaces
- **Admin**: manage users, roles, and global system settings.
- **Tester**: create and assign bugs, track progress.
- **Programmer**: view assigned bugs, update status, submit fixes.

Each interface is built with **FXML views** and dedicated **JavaFX controllers**.

## 🗂️ Technologies Used

- **Java 17+**
- **JavaFX**
- **Hibernate ORM**
- **SQLite**
- **FXML**
- **Layered Architecture**

## 📦 Domain Entities

- `Admin`
- `Tester`
- `Programmer`
- `Bug`

### Steps
1. Start the **server module**.
2. Launch one or more **client modules**.
3. Log in with any role.
4. Interact with the system depending on your role.

## 📝 Screenshots
<img width="883" height="639" alt="Screenshot 2025-11-16 123955" src="https://github.com/user-attachments/assets/56c048f7-023a-42b6-b442-bdf2341cedc6" />

<img width="896" height="641" alt="Screenshot 2025-11-16 124031" src="https://github.com/user-attachments/assets/fc27425a-f494-44af-bc20-1db3c4ac4a72" />

<img width="1149" height="993" alt="Screenshot 2025-11-16 124102" src="https://github.com/user-attachments/assets/958fbc3f-1379-44a9-ae11-26a7483236ba" />

<img width="1393" height="911" alt="Screenshot 2025-11-16 124157" src="https://github.com/user-attachments/assets/9076b8f6-4d3d-4ec2-bd43-c4eef4b03f4a" />

<img width="1377" height="913" alt="Screenshot 2025-11-16 124547" src="https://github.com/user-attachments/assets/a55f5260-0346-4818-9ea3-aeef5e4af56d" />




## 📌 Future Improvements
- Authentication with JWT
- Bug history tracking
- Attachments (screenshots, logs)
- Email notifications
