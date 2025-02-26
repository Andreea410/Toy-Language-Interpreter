## Overview  
This project extends the **Toy Language Interpreter** by adding a **JavaFX-based GUI** and a **Type Checker** to ensure type safety before execution. The GUI enhances usability, allowing users to interact with the interpreter visually, while the Type Checker prevents runtime errors by verifying type correctness.  

---

## Features  

### 1. **Program Selection Window**  
- Displays a list of available **Toy Language programs** (IStmt).  
- Allows users to **select a program** for execution.  

### 2. **Main Execution Window**  
This window provides a real-time visualization of the interpreter’s state, including:  

#### 📌 **Number of PrgStates**  
- A TextField displays the count of active PrgState instances.  

#### 📌 **Heap Table**  
- A TableView with:  
  - Address (Memory Location)  
  - Value (Stored Data)  

#### 📌 **Output List**  
- A ListView displaying program output dynamically.  

#### 📌 **File Table**  
- Shows the list of **open files** managed by the interpreter.  

#### 📌 **PrgState Identifiers**  
- A ListView containing IDs of all active PrgState instances.  

#### 📌 **Symbol Table**  
- A TableView with:  
  - Variable Name  
  - Value  
- Displays the **SymTable** of the currently selected PrgState.  

#### 🚀 **Execution Stack**  
- A ListView representing the **ExeStack** of the selected PrgState, with the **top element displayed first**.  

#### 🚀 **Run One Step Button**  
- Executes **one step** of all PrgState instances using oneStepForAllPrg().  
- Updates all tables and lists dynamically.  
- Uses a **dedicated service** to manage state changes efficiently.  

---

## 3. **Type Checker** 
Ensures that programs are **type-safe** before execution.  

###  **Expression Type Checking**  
Each expression implements:  

- **ValueExp**:  
  Returns the type of the stored value.

- **VarExp**:  
  Looks up the type of a variable.

- **ArithExp**:  
  Ensures both operands are integers.

- **LogicExp**:  
  Ensures both operands are booleans.

- **RelationalExp**:  
  Validates the types of the operands.

- **RHExp**:  
  Ensures that the referenced value is of `RefType`.

##  Execution Guard
The Type Checker runs before execution.  
Programs execute only if type-safe; otherwise, an exception is raised.

##  Project Structure
- `controller/` → Handles user interactions and GUI updates.
- `view/` → Contains JavaFX components for the interface.
- `model/` → Implements interpreter logic (statements, expressions).
- `repository/` → Manages storage of PrgState instances.
- `service/` → Wraps the repository and synchronizes GUI state changes.

## 🛠️ How to Run

### Clone the Repository
```sh
git clone https://github.com/yourusername/interpreter-javafx.git
cd interpreter-javafx
