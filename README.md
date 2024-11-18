# To-Do List Application (Java Swing GUI)

This is a Java-based To-Do List application that allows users to efficiently manage their tasks. The application utilizes **Object-Oriented Programming (OOP)** principles and includes a graphical user interface (GUI) built with **Java Swing**. It provides functionalities for task creation, editing, deletion, search, and filtering, as well as saving tasks to a file.

---

## Features

### 1. **Task Management (CRUD Operations)**
- Add, edit, and delete tasks.
- Mark tasks as completed.

### 2. **Task Details**
- Each task includes:
  - **Title**
  - **Description**
  - **Due Date** (optional)
  - **Category**
  - **Priority** (High, Medium, Low)
  - **Status** (Completed or Not Completed)

### 3. **Search and Filters**
- **Search:** Search tasks by title or description.
- **Filter:** 
  - By **Priority** (High, Medium, Low).
  - By **Status** (Completed or Not Completed).

### 4. **Recurring Tasks**
- Create tasks that repeat periodically (e.g., daily, weekly, monthly).

### 5. **Save and Load**
- Tasks are saved to a file and loaded on application startup for persistence.

---

## Project Structure
```
src/
├── com.and.is.pbo_perubahan
│   ├── Pbo_perubahan.java          # Main entry point of the application
│
├── com.and.is.pbo_perubahan.model
│   ├── Task.java                   # Task data model
│
├── com.and.is.pbo_perubahan.service
│   ├── TaskManager.java            # Manages CRUD operations for tasks
│   ├── FileStorage.java            # Handles saving and loading tasks to/from a file
│
├── com.and.is.pbo_perubahan.ui
│   ├── TaskAppGUI.java             # Main GUI interface for the application
│   ├── TaskForm.java               # GUI form for adding/editing tasks
```


---

## How to Run

### Prerequisites
- **Java Development Kit (JDK)** 8 or higher.
- Any Java IDE (e.g., IntelliJ IDEA, Eclipse, NetBeans) or command-line tools.

### Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/r3nyah/Java-FinalProject.git
2. Open the project in your IDE or navigate to the project directory in the terminal.
3. Compile the project:
   ```
   javac -d bin src/**/*.java
   ```
4. Run the application:
   ```
   java -cp bin com.and.is.pbo_perubahan.Pbo_perubahan
   ```


### Screenshots
Main GUI


### Task Form


Future Improvements
- Add notifications for due tasks.
- Implement recurring task automation.
- Cloud-based storage for tasks.
- Dark mode for the GUI.

### Contributions
Feel free to fork this repository, make improvements, and submit a pull request. Contributions are welcome!
