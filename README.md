📚 Book Share Platform API
A robust RESTful API built with Spring Boot for a community-driven book sharing platform. Users can securely register, manage their book collections, share books with others, borrow books, and leave feedback.

---

## 🌟 Features & Use Cases

Our platform enables a seamless book sharing experience with comprehensive features organized around four core user journeys:

### 🔐 User Onboarding & Security
- **Registration**: Create an account with email verification via activation link
- **Authentication**: Secure login with JWT-based access and refresh tokens
- **Password Recovery**: Reset forgotten passwords through verified email tokens
- **Token Management**: Automatic token refresh for uninterrupted access

### 📖 Book Management
- **Add Books**: Contribute your collection to the platform
- **Status Control**: Toggle sharing availability or archive books
- **Discovery**: Browse, search, and filter books by availability or ownership
- **Details**: View comprehensive book information before borrowing

### 🔄 Book Borrowing Workflow
- **Borrow**: Request available books from other users
- **Track Borrowing**: Monitor all borrowed and shared books in real-time
- **Return Management**: Initiate returns with owner approval workflow
- **Lifecycle Tracking**: View books in different states (borrowed, returned, archived)

### ⭐ Feedback & Reviews
- **Submit Reviews**: Rate and review books you've interacted with
- **Community Insights**: Access paginated feedback to make informed borrowing decisions
- **Reputation Building**: Help the community discover quality books

---

## 🚀 Quick Start Guide

### Authentication Overview
The Book Share Platform uses **Bearer Token (JWT)** authentication. All requests to protected endpoints require a valid access token in the `Authorization` header.

### Step 1️⃣: Register & Activate Your Account
```bash
POST /auth/register
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "securePassword123"
}
```
An activation email will be sent to your inbox. Click the activation link or use the token:
```bash
GET /auth/activate-account?token=YOUR_ACTIVATION_TOKEN
```

### Step 2️⃣: Authenticate & Retrieve Tokens
```bash
POST /auth/authenticate
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "securePassword123"
}
```
**Response:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### Step 3️⃣: Access Protected Endpoints
Include your access token in all subsequent requests:
```bash
Authorization: Bearer <YOUR_ACCESS_TOKEN>
```

### 🔄 Token Refresh
When your access token expires, use the refresh token to obtain a new one:
```bash
POST /auth/refreshtoken
Content-Type: application/json

{
  "refreshToken": "YOUR_REFRESH_TOKEN"
}
```

---

## 📝 API Endpoints Documentation

### 1. Authentication Controller (`/auth`)
Handles user registration, login, account activation, and password management.

| Method | Endpoint                  | Description                                              | Request Body / Params                     |
|--------|---------------------------|---------------------------------------------------------|-------------------------------------------|
| POST   | `/auth/register`          | Register a new user account                             | `RegistrationRequest` (JSON Body)         |
| POST   | `/auth/authenticate`      | Login and retrieve JWT tokens                           | `AuthenticationRequest` (JSON Body)       |
| GET    | `/auth/activate-account`  | Activate account using email token                      | `?token=String` (Query Param)             |
| POST   | `/auth/refreshtoken`      | Get a new access token using a refresh token            | `TokenRefreshRequest` (JSON Body)         |
| POST   | `/auth/forgetPassword`    | Request a password reset email                          | `?email=String` (Query Param)             |
| POST   | `/auth/changePassword`    | Reset password using the token sent to email            | `?token=String` (Query), `ChangePasswordRequest` (JSON Body) |

---

### 2. Book Controller (`/books`)
Manages books, sharing status, and the borrowing lifecycle. **(All endpoints require Bearer Authentication)**

#### Book CRUD & Browsing
| Method | Endpoint           | Description                                   | Request Body / Params                                |
|--------|--------------------|-----------------------------------------------|------------------------------------------------------|
| POST   | `/books`           | Add a new book to the platform                | `BookRequest` (JSON Body)                            |
| GET    | `/books/{book-id}` | Find a specific book by its ID                | `book-id` (Path Variable)                            |
| GET    | `/books`           | Get all books (Paginated)                     | `?page=int`, `?size=int` (Query Params)              |
| GET    | `/books/owner`     | Get all books owned by the logged-in user     | `?page=int`, `?size=int` (Query Params)              |

#### Book Status Updates
| Method | Endpoint                       | Description                                  | Request Body / Params                  |
|--------|--------------------------------|----------------------------------------------|----------------------------------------|
| PATCH  | `/books/shareable/{book_id}`   | Toggle the shareable status of a book        | `book_id` (Path Variable)              |
| PATCH  | `/books/Archived/{book_id}`    | Toggle the archived status of a book         | `book_id` (Path Variable)              |

#### Borrowing Workflow
| Method | Endpoint                               | Description                                              | Request Body / Params                  |
|--------|----------------------------------------|---------------------------------------------------------|----------------------------------------|
| POST   | `/books/borrow/{book_id}`             | Borrow a book from another user                         | `book_id` (Path Variable)              |
| GET    | `/books/borrowed`                     | Get all books the current user has borrowed (Paginated) | `?page=int`, `?size=int` (Query Params)|
| GET    | `/books/returned`                     | Get all books returned by users to the owner (Paginated)| `?page=int`, `?size=int` (Query Params)|
| PATCH  | `/books/borrow/return/{book_id}`      | Borrower initiates a return for a borrowed book         | `book_id` (Path Variable)              |
| PATCH  | `/books/borrow/return/approve/{book_id}`| Owner approves the return of a borrowed book          | `book_id` (Path Variable)              |

---

### 3. Feedback Controller (`/feedbacks`)
Handles user reviews and ratings for books. **(All endpoints require Bearer Authentication)**

| Method | Endpoint              | Description                                            | Request Body / Params                                |
|--------|-----------------------|-------------------------------------------------------|------------------------------------------------------|
| POST   | `/feedbacks`          | Submit feedback/rating for a book                     | `FeedbackRequest` (JSON Body)                        |
| GET    | `/feedbacks/book/{book-id}` | Get all feedback for a specific book (Paginated) | `book-id` (Path Var), `?page=int`, `?size=int` (Query)|

*(Note: The base path for the feedback controller is assumed to be `/feedbacks` based on standard Spring Boot conventions, as the class-level mapping was not explicitly provided in the snippet).*

---

## 💻 Pagination Details

Endpoints that return lists (like getting all books or feedback) utilize pagination. 
Pass the following optional query parameters in your GET requests:
*   `page`: The page number you want to retrieve (starts at `0`). Default is `0`.
*   `size`: The number of items per page. Default is `10`.

**Example Request:**
```http
GET /books?page=1&size=5
Authorization: Bearer <your_token>
```
