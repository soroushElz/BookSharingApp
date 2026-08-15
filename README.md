📚 Book Share Platform API
A robust RESTful API built with Spring Boot for a community-driven book sharing platform. Users can securely register, manage their book collections, share books with others, borrow books, and leave feedback and ratings. The API includes authentication with JWT, email-based activation and password recovery, book management, borrowing workflows, and feedback endpoints to support a collaborative sharing experience.

🌟 Use Cases
This application is designed to support the following user journeys:

User Onboarding & Security:
Users can register for an account and receive an activation email to verify their identity.
Secure login using email and password to receive JWT access and refresh tokens.
Password recovery flow for forgotten passwords via email verification tokens.
Book Management:
Authenticated users can add new books to the platform.
Owners can update the status of their books (e.g., toggle "shareable" or "archived").
Users can browse all available books, view specific book details, or filter books by ownership.
Book Borrowing Workflow:
Users can borrow available books from other users.
Borrowers can initiate a return request for books they currently possess.
Book owners can approve or reject return requests to complete the borrowing lifecycle.
Users can track books they have borrowed, books they have shared, and books currently returned.
Feedback & Reviews:
Users can submit feedback/ratings for books they have interacted with.
Users can view paginated feedback for specific books to help decide whether to borrow them.
🚀 How to Use the APIs (Introduction)
To interact with this API, you must first authenticate. The platform uses Bearer Token (JWT) authentication.

Step 1: Register & Activate
Send a POST request to /auth/register with your name, email, and password.
An activation email will be sent. Click the link (or use the token) to call GET /auth/activate-account?token=YOUR_TOKEN.
Step 2: Authenticate
Send a POST request to /auth/authenticate with your credentials.
The API will return a JSON object containing your accessToken and refreshToken.
Step 3: Access Protected Endpoints
For all subsequent requests to /books and /feedbacks, include the following header:
Authorization: Bearer <YOUR_ACCESS_TOKEN>

 If your access token expires, use the `POST /auth/refreshtoken` endpoint with your `refreshToken` to get a new access token.

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
