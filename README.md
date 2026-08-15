2. If your access token expires, use the `POST /auth/refreshtoken` endpoint with your `refreshToken` to get a new access token.

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