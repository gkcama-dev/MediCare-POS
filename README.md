# MediCare-POS - Pharmacy Management System

A comprehensive, enterprise-grade Pharmacy Point of Sale (POS) system built with JavaFX and MySQL. Designed for efficient medicine inventory management, supplier handling, sales processing, and comprehensive reporting capabilities.

![Version](https://img.shields.io/badge/version-1.0-blue.svg)
![Java](https://img.shields.io/badge/Java-11-red.svg)
![License](https://img.shields.io/badge/license-MIT-green.svg)
![Status](https://img.shields.io/badge/status-Production%20Ready-brightgreen.svg)

---

## Table of Contents

- [Overview](#-overview)
- [Features](#-features)
- [Technology Stack](#-technology-stack)
- [Architecture](#-architecture)
- [Prerequisites](#-prerequisites)
- [Installation](#-installation)
- [Configuration](#-configuration)
- [Project Structure](#-project-structure)
- [Database Setup](#-database-setup)
- [Usage](#-usage)
- [Reporting](#-reporting)
- [Contributors](#-contributors)
- [License](#-license)

---

## Overview

MediCare-POS is a modern pharmacy management system designed to streamline operations for pharmacy chains and independent pharmacies. It provides a user-friendly interface for managing inventory, processing sales, tracking suppliers, and generating detailed business reports. The system follows best practices in software architecture with layered architecture and proven design patterns (Factory Pattern, Singleton Pattern, CRUD operations).

**Key Highlights:**
- Multi-user role-based access (Admin & Cashier)
- Real-time inventory management
- Secure database connectivity with connection pooling
- Comprehensive reporting with JasperReports
- Cross-platform compatibility (Windows, Linux, macOS)

---

## Features

### Inventory Management
- **Product Management**: Add, update, and manage pharmaceutical products
- **Stock Control**: Real-time stock tracking and low-stock alerts
- **Category & Brand Management**: Organize products by categories and brands
- **GRN (Goods Received Note)**: Track incoming inventory from suppliers

### 💳 Sales Operations
- **Invoice Generation**: Create and manage customer invoices
- **Point of Sale**: Fast and intuitive checkout system
- **Item Management**: Add/remove items from carts with quantity adjustments

### User Management
- **Role-Based Access Control**: Separate admin and cashier dashboards
- **User Authentication**: Secure login system
- **User Profiles**: Manage user accounts and permissions

### Reporting
- **GRN Reports**: Complete goods receipt records
- **Sales Reports**: Detailed invoice and sales analytics
- **Product Reports**: Inventory and product performance metrics
- **Stock Reports**: Current stock levels and availability
- **PDF Generation**: Export reports to PDF format

### Dashboard
- **Real-time Analytics**: Key business metrics at a glance
- **Quick Actions**: Rapid access to frequent operations
- **System Overview**: Complete operational visibility

---

## 🛠 Technology Stack

| Layer | Technology | Version | Purpose |
|-------|-----------|---------|---------|
| **UI Framework** | JavaFX | 19.0.2 | Modern desktop UI framework |
| **UI Components** | JFoenix | 9.0.1 | Material Design components |
| **Database** | MySQL | 8.0+ | Relational data management |
| **JDBC Driver** | MySQL Connector/J | 9.5.0 | Database connectivity |
| **Reporting** | JasperReports | 7.0.3 | Advanced PDF report generation |
| **Build Tool** | Maven | 3.6+ | Project management and build automation |
| **Java Version** | Java | 11 (LTS) | Programming language |
| **Utilities** | Lombok | 1.18.42 | Reduce boilerplate code (Getters/Setters) |

---

## Architecture

The project follows a **Layered Architecture Pattern** with clear separation of concerns:

```
┌─────────────────────────────────────┐
│      UI Layer (JavaFX Views)        │
│   (Controllers & FXML Layouts)      │
├─────────────────────────────────────┤
│    Controller Layer                 │
│   (Request Handling & Routing)      │
├─────────────────────────────────────┤
│    Service Layer                    │
│   (Business Logic & Processing)     │
├─────────────────────────────────────┤
│    Repository Layer                 │
│   (Data Access & CRUD Operations)   │
├─────────────────────────────────────┤
│    Database Layer                   │
│   (MySQL Database Connection)       │
└─────────────────────────────────────┘
```

### Design Patterns Used

- **Factory Pattern**: `ServiceFactory`, `RepositoryFactory` for object creation
- **Singleton Pattern**: `DbConnection` for single database instance
- **DAO Pattern**: Repository classes for data persistence
- **MVC Pattern**: Controllers, Views (FXML), Models

---

## Prerequisites

Before setting up the project, ensure you have the following installed:

- **Java Development Kit (JDK)** 11 or higher
  - Verify: `java -version`
- **Maven** 3.6.0 or higher
  - Verify: `mvn -version`
- **MySQL Server** 5.7 or higher
  - Verify: `mysql -u root -p`
- **Git** (optional, for version control)

### System Requirements

- **RAM**: Minimum 2GB (4GB recommended)
- **Disk Space**: 500MB for application and dependencies
- **OS**: Windows 10/11, Linux, or macOS

---

## Installation

### Step 1: Clone or Download the Project

```bash
# If using Git
git clone <repository-url>
cd MediCare_POS

# Or extract the zip file and navigate to the project directory
cd MediCare_POS
```

### Step 2: Build the Project

```bash
# Clean and build the project
mvn clean install

# Or just compile (if you already have dependencies)
mvn compile
```

### Step 3: Resolve Dependencies

Maven will automatically download all dependencies from pom.xml:

```bash
# Explicitly download dependencies
mvn dependency:resolve
```

### Step 4: Run the Application

```bash
# Option 1: Using Maven exec plugin
mvn javafx:run

# Option 2: Run the JAR file from target directory
java -jar target/MediCare_POS-1.0-SNAPSHOT.jar

# Option 3: Run directly using Java
java -cp target/classes:$(mvn dependency:build-classpath -q -Dmdep.outputFile=/dev/stdout) Main
```

---

## Configuration

### Database Configuration

Update the database connection details in `DbConnection.java`:

```java
// File: src/main/java/database/DbConnection.java

private DbConnection() throws SQLException {
    connection = DriverManager.getConnection(
        "jdbc:mysql://localhost:3306/medicare_pos",  // Change host/database name
        "root",                                         // Change username
        "Geeth@200104"                                 // Change password
    );
}
```

**Security Note**: Never commit plain-text passwords. Consider using:
- Environment variables
- Configuration files (git-ignored)
- Properties files for production

### Build Configuration

Maven properties in `pom.xml`:

```xml
<properties>
    <maven.compiler.source>11</maven.compiler.source>
    <maven.compiler.target>11</maven.compiler.target>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
</properties>
```

---

## Project Structure

```
MediCare_POS/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── Main.java                    # Application entry point
│   │   │   ├── Starter.java                 # JavaFX Application starter
│   │   │   ├── controller/
│   │   │   │   ├── adminController/        # Admin panel controllers
│   │   │   │   │   ├── DashboardController
│   │   │   │   │   ├── ProductController
│   │   │   │   │   ├── StockController
│   │   │   │   │   └── ... (other admin features)
│   │   │   │   ├── cashierController/      # Cashier panel controllers
│   │   │   │   │   └── ... (sales operations)
│   │   │   │   └── loggingController/      # Authentication controllers
│   │   │   ├── database/
│   │   │   │   └── DbConnection.java       # MySQL connection management (Singleton)
│   │   │   ├── model/
│   │   │   │   ├── Product.java
│   │   │   │   ├── Invoice.java
│   │   │   │   ├── GRN.java
│   │   │   │   ├── Stock.java
│   │   │   │   ├── User.java
│   │   │   │   ├── Supplier.java
│   │   │   │   ├── Category.java
│   │   │   │   ├── Brand.java
│   │   │   │   └── tableModel/             # Table rendering models
│   │   │   ├── repository/
│   │   │   │   ├── SuperRepository.java    # Base repository interface
│   │   │   │   ├── CrudRepository.java     # Generic CRUD operations
│   │   │   │   ├── RepositoryFactory.java  # Factory pattern implementation
│   │   │   │   └── custom/                 # Custom repository implementations
│   │   │   ├── service/
│   │   │   │   ├── SuperService.java       # Base service interface
│   │   │   │   ├── ServiceFactory.java     # Factory pattern for services
│   │   │   │   └── custom/                 # Business logic implementations
│   │   │   └── util/
│   │   │       ├── CrudUtil.java           # Utility methods for CRUD operations
│   │   │       ├── RepositoryType.java     # Enum for repository types
│   │   │       ├── ServiceType.java        # Enum for service types
│   │   │       ├── Session/                # User session management
│   │   │       └── listener/               # Event listeners
│   │   └── resources/
│   │       ├── css/                        # Stylesheet files
│   │       │   ├── style.css
│   │       │   ├── components-style.css
│   │       │   ├── table-style.css
│   │       │   └── splash.css
│   │       ├── view/
│   │       │   ├── adminView/              # Admin FXML layouts
│   │       │   ├── cashierView/            # Cashier FXML layouts
│   │       │   └── loginView/              # Login/splash FXML layouts
│   │       ├── report/                     # JasperReports templates (.jrxml)
│   │       │   ├── MedicarePOS-All-GRN-Report.jrxml
│   │       │   ├── MedicarePOS-All-Invoice-Report.jrxml
│   │       │   └── ... (other report templates)
│   │       ├── icon/                       # Application icons
│   │       └── img/                        # Images and logos
│   └── test/
│       └── java/                           # Unit tests
├── db_ER/
│   ├── medicarePOS.mwb                     # MySQL Workbench ER diagram
│   └── medicarePOS.mwb.bak                 # Backup of ER diagram
├── target/                                  # Compiled classes and JAR
├── pom.xml                                  # Maven configuration
└── LICENSE                                  # MIT License

```

### Key Directories Explained

| Directory | Purpose |
|-----------|---------|
| `controller/` | Handles UI events and user interactions |
| `model/` | Data models representing database entities |
| `repository/` | Data access layer, interacts with database |
| `service/` | Business logic and processing layer |
| `util/` | Helper classes and utilities |
| `resources/` | Static files (CSS, FXML, images, reports) |

---

## Database Setup

### Create Database

```sql
-- Create database
CREATE DATABASE IF NOT EXISTS medicare_pos;

-- Use the database
USE medicare_pos;
```

### Import ER Diagram

1. Open MySQL Workbench
2. File → Open Model → Select `db_ER/medicarePOS.mwb`
3. Database → Forward Engineer to create tables in your MySQL instance
4. Update connection credentials in `DbConnection.java`

### Expected Tables

- `users` - User authentication and profiles
- `products` - Pharmaceutical products
- `categories` - Product categories
- `brands` - Product brands
- `suppliers` - Supplier information
- `stock` - Inventory tracking
- `grn` - Goods Received Notes
- `grn_items` - Items in each GRN
- `invoices` - Sales invoices
- `invoice_items` - Items in each invoice

---

## Usage

### Starting the Application

1. **Build and Run**:
   ```bash
   mvn clean javafx:run
   ```

2. **Login Screen**: Enter credentials (default or admin setup required)

3. **Admin Dashboard**:
   - Manage products, categories, and brands
   - Track stock levels
   - Create GRNs for new inventory
   - View comprehensive reports
   - Manage user accounts

4. **Cashier Dashboard**:
   - Process customer sales (invoices)
   - Manage point-of-sale transactions
   - View inventory availability

### Main Features Usage

#### Adding a Product
1. Navigate to Admin → Products
2. Click "Add New Product"
3. Enter product details (name, code, price, category, brand)
4. Confirm to save

#### Creating a GRN (Goods Received Note)
1. Navigate to Admin → GRN Management
2. Click "New GRN"
3. Select supplier and add items with quantities
4. Confirm to record inventory

#### Processing a Sale
1. Login as Cashier
2. Create a new Invoice
3. Search and add products
4. Adjust quantities
5. Process payment
6. Generate receipt

#### Generating Reports
1. Navigate to Reports section
2. Choose report type (GRN, Invoice, Products, Stock)
3. Set filters/date range (if needed)
4. Generate and export to PDF

---

## Reporting

### Available Reports

| Report | Details | Format |
|--------|---------|--------|
| **All GRN Report** | Complete inventory receipt records | PDF |
| **All Invoice Report** | Sales transaction history | PDF |
| **All Products Report** | Product catalog with details | PDF |
| **All Stock Report** | Current inventory levels | PDF |
| **Invoice Bill** | Individual sales receipt | PDF |

**Report Templates**: Located in `src/main/resources/report/` directory

```
MedicarePOS-All-GRN-Report.jrxml
MedicarePOS-All-Invoice-Report.jrxml
MedicarePOS-All-Products-Report.jrxml
MedicarePOS-All-Stock-Report.jrxml
MedicarePOS-Invoice-Bill.jrxml
```

---

## Development

### Compile Project
```bash
mvn clean compile
```

### Run Tests
```bash
mvn test
```

### Package Application
```bash
mvn clean package
```

### Generate Javadoc
```bash
mvn javadoc:javadoc
```

### View Dependencies
```bash
mvn dependency:tree
```

---

## Troubleshooting

### Common Issues

**1. MySQL Connection Error**
```
Error: com.mysql.cj.jdbc.exceptions.CommunicationsException
```
- Verify MySQL server is running
- Check connection credentials in `DbConnection.java`
- Ensure database `medicare_pos` exists

**2. JavaFX Module Error**
```
Error: javafx.fxml.LoadException
```
- Ensure JavaFX version matches (19.0.2)
- Clear Maven cache: `mvn clean`
- Rebuild: `mvn install`

**3. FXML File Not Found**
```
Error: java.lang.NullPointerException when loading /view/...
```
- Verify FXML files exist in `src/main/resources/view/`
- Check file paths in controllers match actual locations
- Rebuild: `mvn clean javafx:run`

**4. Lombok Not Working**
```
Error: Getters/Setters not found
```
- Install Lombok annotation processor in IDE
- Run `mvn clean compile`
- Restart IDE

---

## Development Guidelines

### Code Standards

- **Naming Conventions**: Use camelCase for variables/methods, PascalCase for classes
- **Comments**: Add meaningful Javadoc comments for public methods
- **Error Handling**: Use try-catch for database operations
- **Logging**: Implement proper logging for debugging

### Commit Guidelines

```
[Feature] Add product management functionality
[Bug Fix] Resolve database connection timeout
[Refactor] Improve GRN item calculation logic
```

---

## Contributors

- **Geeth Kalhara** - Lead Developer
- ICET Development Team

---

## License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

```
MIT License

Copyright (c) 2026 Geeth Kalhara

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.
```

---

##  Support

For issues, questions, or suggestions:
- Review existing documentation
- Check the troubleshooting section
- Contact the development team

---

## Additional Resources

- [JavaFX Documentation](https://openjfx.io/)
- [MySQL Documentation](https://dev.mysql.com/doc/)
- [Maven Official Site](https://maven.apache.org/)
- [JasperReports Guide](https://community.jaspersoft.com/)

---

**Last Updated**: February 28, 2026 | **Version**: 1.0.0
