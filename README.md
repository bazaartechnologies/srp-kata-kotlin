# SRP Kata Repository

Welcome to the **SRP Kata** repository! This project is designed to help developers practice and understand the **Single Responsibility Principle (SRP)**, one of the key principles of the SOLID design guidelines.

---

## Table of Contents
- [What is SRP?](#what-is-srp)
- [About This Kata](#about-this-kata)
- [Getting Started](#getting-started)

---

## What is SRP?

The **Single Responsibility Principle (SRP)** states that a class or module should have one and only one reason to change. This means that a class should have a single responsibility or purpose, which makes it easier to:
- Understand the code
- Maintain the system
- Avoid tightly coupled dependencies

---

## About This Kata

The SRP Kata is a hands-on exercise that helps developers recognize violations of SRP and refactor the code to align with this principle. The goal is to:

1. Identify multiple responsibilities within a single class.
2. Refactor the class by separating these responsibilities into smaller, more focused classes or modules.
3. Ensure the code remains functional and testable after the refactoring.

---

## Getting Started

To get started with this repository, follow these steps:

1. **Clone the Repository**
   ```bash
   git clone https://github.com/bazaartechnologies/srp-kata-kotlin.git
   ```

2. **Create a New Branch**
   ```bash
   git checkout -b <your-branch-name>
   ```

3. **Build the Project** (using Maven)
   ```bash
   mvn clean install
   ```

4. **Run the Tests**
   Execute the existing test suite to ensure everything is working as expected before starting your refactor:
   ```bash
   mvn test
   ```
---

Happy coding and refactoring!

