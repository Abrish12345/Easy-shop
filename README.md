# Easy-shop
# 🛒 EasyShop - E-Commerce Backend API

EasyShop is a Spring Boot-based REST API for an e-commerce platform that allows users to browse products, manage their shopping cart, and maintain personal profiles. This project was developed as part of a capstone experience, demonstrating full-stack principles with a focus on secure backend development.

## 🔧 Technologies Used
- Java 17
- Spring Boot
- Spring Security (JWT-based authentication)
- MySQL
- JDBC
- Postman (for API testing)

## 📦 Features

### ✅ Authentication
- User registration and login (with JWT token generation)
- Role-based access control (`USER`, `ADMIN`)

### 🛍️ Shopping Cart
- View user-specific cart (`GET /cart`)
- Add products to cart (`POST /cart/products/{productId}`)
- Update item quantity (`PUT /cart/product/{productId}`)
- Clear entire cart (`DELETE /cart`)

### 👤 User Profile
- View current profile (`GET /profile`)
- Update profile information (`PUT /profile`)

### 📦 Products
- List all products
- Filter by category or keyword

> Admins can also create, update, or delete products.
