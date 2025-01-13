# Ex2 - Object-Oriented Design and Recursion

## Student Information
**Name:** Samuel Sebban  
**ID:** 340886878

---

## Project Overview

This project implements a simplified **Spreadsheet** application as part of the "Introduction to Computer Science, 2025A" course. It focuses on foundational concepts of **Object-Oriented Design** and **Recursion**.

The spreadsheet is a 2D array of cells, each containing one of the following:

- **Text** (e.g., "Hello")
- **Number** (e.g., `123.45`)
- **Formula** (e.g., `=A1+B2`, `=1+2*3`)

### Features
- Parsing and evaluating formulas with basic arithmetic operations (`+`, `-`, `*`, `/`).
- Supporting cell references (e.g., `=A1+B2`).
- Error handling for invalid formulas and circular references.
- Loading and saving the spreadsheet data to/from a CSV file.
- A GUI interface for interacting with the spreadsheet.

---

## Class Structure

### 1. **Core Classes**

#### `Cell`
- Interface for a spreadsheet cell.
- Types of cells:
    - **Text**
    - **Number**
    - **Formula**
    - **Error** (e.g., invalid formulas or circular references).

#### `SCell`
- Implementation of `Cell`.
- Stores data and type of the cell (e.g., text, number, formula).
- Contains methods to validate and compute formulas.

#### `Ex2Sheet`
- Main class representing the spreadsheet.
- Contains methods for:
    - Setting and retrieving cell values.
    - Evaluating formulas.
    - Computing cell dependencies (depth).
    - Handling circular references.

#### `CellEntry`
- Helper class for managing 2D indices of cells (e.g., `A1`, `B2`).
- Converts between cell coordinates and string representations.

### 2. **Utilities**

#### `Ex2Utils`
- Constants and utility methods for managing spreadsheet operations (e.g., cell dimensions, error messages).

### 3. **Testing**

#### `Ex2SheetTest`
- JUnit tests for verifying:
    - Basic cell operations.
    - Formula evaluation.
    - Error handling (e.g., invalid formulas, division by zero).
    - Save and load functionality.

---

## How to Run

### Prerequisites
- **Java Development Kit (JDK)** installed (version 11 or higher).
- An IDE such as IntelliJ IDEA.

### Steps
1. Clone the project repository from GitHub.
2. Open the project in your IDE.
3. Run the `Ex2GUI` class to launch the graphical interface.
4. Interact with the spreadsheet using the GUI.

---

## Key Functionalities

### 1. **Adding Data**
- Text: Enter plain text (e.g., `"Hello"`).
- Number: Enter numerical values (e.g., `123.45`).
- Formula: Enter formulas starting with `=` (e.g., `=A1+B2`).

### 2. **Formula Evaluation**
- Supports basic arithmetic operations (`+`, `-`, `*`, `/`).
- Evaluates expressions with parentheses (e.g., `=(1+2)*3`).
- Handles cell references (e.g., `=A1+B2`).
- Detects and reports errors for:
    - Invalid syntax (e.g., `=2++3`).
    - Circular references (e.g., `A1` depends on `A2`, and `A2` depends on `A1`).
    - Division by zero.

### 3. **Save and Load**
- Save the spreadsheet to a CSV file using the `save()` method.
- Load data from a CSV file using the `load()` method.
- Automatically evaluates formulas after loading.

---

## Project Directory Structure

```
Ex2/
├── src/
│   ├── assignments.ex2/
│   │   ├── Cell.java
│   │   ├── CellEntry.java
│   │   ├── Ex2GUI.java
│   │   ├── Ex2Sheet.java
│   │   ├── Ex2SheetTest.java
│   │   ├── Ex2Utils.java
│   │   ├── Index2D.java
│   │   ├── SCell.java
│   └── test/
│       ├── test.csv
│       ├── sheet_test.csv
│       ├── testSheet.csv
├── README.md
└── .idea/
```

---

## Example

### Input
|   | A  | B   | C      |
|---|----|-----|--------|
| 1 | 5  | 10  | `=A1+B1` |
| 2 | 2  | `=A1*C1` | `=(A2+B1)/A1` |
| 3 | `Hello` |  |  |

### Output
|   | A  | B   | C      |
|---|----|-----|--------|
| 1 | 5  | 10  | 15.0   |
| 2 | 2  | 75.0 | 3.5    |
| 3 | `Hello` |  |  |

---




## Screenshots

### GUI Overview
![GUI Overview](path/to/gui-image.png)

---

## Author
**Samuel Sebban**  
340886878
