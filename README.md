# BRTS_PROJECT_JAVA

## Overview
This project is an e-commerce system designed to manage bus ticket bookings. It utilizes a structured database to facilitate operations such as user management, ticket bookings, and payment processing. The project includes key modules written in Java to handle various functionalities.

## Modules
- **AdminModule.java**: Manages administrative functions.
- **BookingModule.java**: Handles ticket booking operations.
- **BusModel.java**: Represents the bus entity and its properties.
- **DBConnector.java**: Establishes connections to the database.
- **LoginModel.java**: Manages user login and authentication.
- **UserModule.java**: Handles user-related operations.
- **HashMap.java**: A custom implementation of a hash map data structure using a doubly linked list.

## Prerequisites
Ensure you have the following installed:
- Java Development Kit (JDK)
- MariaDB (Version: 10.4.32 or higher)
- phpMyAdmin (Optional, Version: 5.2.1 or higher)
- PHP (Version: 8.2.12 or higher)

## Database Structure
The database contains tables for managing bus ticket bookings, including:

- `user`: Stores user account information.
- `booking`: Contains booking details.
- `payment`: Manages payment information.
- `bus`: Stores bus-related data.

## Key Stored Procedures
The database includes stored procedures for operations like booking tickets, canceling bookings, and retrieving user bookings.

## Setup Instructions
1. **Clone the repository:**
   ```bash
   git clone https://github.com/joshivedant7/BRTS_PROJECT_JAVA
