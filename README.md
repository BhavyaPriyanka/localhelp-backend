# LocalHelp Backend Documentation

## Overview

LocalHelp backend is a Spring Boot REST API application responsible for user authentication, medicine management, order processing, inventory updates, and order history.

## Technology

- Java 17
- Spring Boot
- Spring Security
- JWT Authentication
- Hibernate JPA
- Maven
- MySQL


# Database

## Database Technology

```
MySQL
```


## Database Name

```
localhelp
```


# Database Tables


## users Table

Stores application users and roles.

Columns:

```
id
username
password
role
enabled
created_at
updated_at
```


## medicine Table

Stores medicine information and stock details.

Columns:

```
id
name
price
stock
```


## orders Table

Stores customer order details.

Columns:

```
id
user_id
total_amount
status
created_at
```


## order_items Table

Stores medicines included in an order.

Columns:

```
id
order_id
medicine_id
quantity
price
```


---

# Authentication


## Login API

```
POST /auth/login
```


Functionality:

- Validate username and password
- Authenticate user
- Generate JWT token
- Return user details and token


Request:

```json
{
  "username":"user",
  "password":"password"
}
```


Response:

```json
{
  "username":"user",
  "role":"USER",
  "token":"JWT_TOKEN"
}
```


---

# Medicine APIs


## Get All Medicines

```
GET /medicines
```


Functionality:

- Fetch all available medicines
- Display medicine name
- Display price
- Display available stock


Response:

```json
[
 {
  "id":1,
  "name":"Paracetamol",
  "price":20,
  "stock":50
 }
]
```


---

# Order APIs


## Create Order

```
POST /orders
```


Headers:

```
Authorization: Bearer JWT_TOKEN
```


Functionality:

1. Get logged-in user from JWT token
2. Validate medicine availability
3. Check available stock
4. Reduce medicine stock
5. Create order record
6. Create order item records


Request:

```json
{
 "items":[
  {
   "medicineId":2,
   "quantity":2
  }
 ]
}
```


Response:

```json
{
 "orderId":9,
 "totalAmount":70,
 "message":"ORDER PLACED SUCCESSFULLY"
}
```


---

## Get User Order History

```
GET /orders/my-orders
```


Headers:

```
Authorization: Bearer JWT_TOKEN
```


Functionality:

- Identify logged-in user
- Fetch user's orders
- Fetch order items
- Return complete order details


Response:

```json
[
 {
  "orderId":9,
  "totalAmount":70,
  "items":[
   {
    "medicineName":"Dolo 650",
    "quantity":2,
    "price":35
   }
  ]
 }
]
```


---

# Order Processing Flow


```
User Request

      |

JWT Authentication

      |

Controller Layer

      |

Service Layer

      |

Repository Layer

      |

MySQL Database
```


---

# Inventory Management Flow


Before Order:

```
Medicine: Dolo 650

Stock = 100
```


Customer places order:

```
Quantity = 2
```


After Order:

```
Medicine: Dolo 650

Stock = 98
```


Stock is automatically reduced during successful order creation.


---

# Entity Relationship


```
User

 |
 |
One User

 |
 |
Many Orders


Order

 |
 |
Many Order Items


Order Item

 |
 |
Medicine
```


---

# Current Completed Backend Features

- User authentication
- JWT based security
- Role based users
- Medicine management
- Order creation
- Stock reduction
- Order history
- MySQL database integration
- REST API implementation
