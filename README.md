# Toy Language Interpreter with JavaFX  

## Overview  

This project extends the **Toy Language Interpreter** by integrating:  

- A **JavaFX-based GUI** for an interactive user experience  
- A **Type Checker** to ensure type safety before execution, preventing runtime errors  

The interpreter supports dynamic program execution while offering a real-time visualization of program state, memory, and execution flow.  

## Features  

### 1. Program Selection Window  

- Displays a list of predefined **Toy Language programs** (`IStmt`)  
- Allows users to **select a program** and execute it step by step  

### 2. Main Execution Window  

This window provides a **real-time visualization** of the interpreter’s state, including:  

#### PrgState Management  

- Displays the **number of active PrgState instances**  
- Lists **active PrgState IDs** for multi-threaded execution  

#### Heap Table  

- Shows **memory addresses** and their corresponding **stored values** dynamically  

#### Symbol Table  

- Displays **variable names** alongside their **current values**, updating in real time  

#### Execution Stack (ExeStack)  

- Shows the **remaining execution steps** in the currently selected `PrgState`, with the **top element displayed first**  

#### Output List  

- Dynamically updates with program **output values**  

#### File Table  

- Displays **open files** being managed by the interpreter  

#### Step-by-Step Execution  

- A **"Run One Step"** button executes the next instruction for all active `PrgState` instances  
- Uses **oneStepForAllPrg()** for synchronized updates across tables  
- Runs in a **dedicated service** for efficient GUI updates  

## 3. Type Checker (Static Analysis)  

Before execution, the Type Checker ensures that **all expressions and statements adhere to type rules** to prevent runtime errors.  

### Expression Type Checking  

Each expression implements a **type inference function**:  

| Expression        | Type Checking Rule |
|------------------|------------------|
| **ValueExp**     | Returns the **type** of the stored value |
| **VarExp**       | Looks up the **type** of the variable |
| **ArithExp**     | Ensures both operands are **integers** |
| **LogicExp**     | Ensures both operands are **booleans** |
| **RelationalExp** | Validates the types of **both operands** |
| **RHExp**        | Ensures that the referenced value is of **RefType** |

### Execution Guard  

The **Type Checker runs before execution**. If a program contains **type errors**, execution is **blocked**, and an **exception is raised**.  

## Project Structure  

The project follows the **MVC (Model-View-Controller) architecture**:  

- `controller/` - Manages user interactions and updates the GUI  
- `view/` - Contains JavaFX components (UI elements)  
- `model/` - Implements the interpreter logic (statements, expressions)  
- `repository/` - Handles storage of `PrgState` instances  
- `service/` - Connects repository and controller for state synchronization  

