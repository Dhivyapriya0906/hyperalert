# 🚨 HyperAlert — Village-Level Disaster Alert System

A real-time emergency alert management system built with Spring Boot that provides **village-level weather coverage** for Indian pincodes — solving the gap where most weather APIs only cover city/district level.

## 🌟 Key Features

- **Pincode-based weather coverage** — Enter any 6-digit Indian pincode to get real-time weather data for that exact village
- **Auto-alert generation** — Automatically detects and saves alerts for 7 disaster types (Heatwave, Flood Risk, Storm, Cyclone Warning, Cold Wave, etc.)
- **Email notifications** — Automatically emails all subscribers when severe weather is detected in their pincode
- **Subscription system** — Users can subscribe to specific pincodes and receive instant email alerts
- **JWT Authentication** — Secure login/register with BCrypt password hashing
- **Full CRUD REST API** — Complete alert management with POST, GET, PUT, DELETE endpoints
- **Frontend UI** — Clean, dark-themed web interface for live demo

## 🏗️ Architecture

```
User enters Pincode
        ↓
India Post API (Postalpincode) → Official village/post office name
        ↓
OpenWeatherMap Geocoding API → Converts pincode to lat/long
        ↓
OpenWeatherMap Weather API → Real-time weather data
        ↓
Spring Boot processes → Detects severe conditions
        ↓
Auto-saves Alert to MySQL + Emails all subscribers
```

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 17, Spring Boot 3.5 |
| Security | Spring Security + JWT (jjwt 0.11.5) |
| Database | MySQL 8.0 + Spring Data JPA + Hibernate |
| APIs | OpenWeatherMap, Postalpincode (India Post) |
| Email | Spring Mail + Gmail SMTP |
| Frontend | HTML5, CSS3, Vanilla JavaScript |
| Build Tool | Maven |

## 📡 API Endpoints

### Authentication
| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| POST | `/api/auth/register` | Register new user | No |
| POST | `/api/auth/login` | Login, get JWT token | No |

### Alerts
| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| GET | `/api/alerts` | Get all alerts | Yes |
| POST | `/api/alerts` | Create alert manually | Yes |
| PUT | `/api/alerts/{id}` | Update alert | Yes |
| DELETE | `/api/alerts/{id}` | Delete alert | Yes |

### Weather
| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| GET | `/api/weather/{pincode}` | Get weather + auto-generate alerts | Yes |

### Subscriptions
| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| POST | `/api/subscriptions` | Subscribe to pincode alerts | Yes |
| GET | `/api/subscriptions/my` | View my subscriptions | Yes |
| DELETE | `/api/subscriptions/{id}` | Unsubscribe | Yes |

## ⚙️ Setup & Installation

### Prerequisites
- Java 17
- MySQL 8.0
- Maven

### Steps

1. Clone the repository:
```bash
git clone https://github.com/Dhivyapriya0906/hyperalert.git
cd hyperalert
```

2. Create MySQL database:
```sql
CREATE DATABASE hyperalert_db;
```

3. Create `src/main/resources/application.properties`:
```properties
spring.application.name=hyperalert
spring.datasource.url=jdbc:mysql://localhost:3306/hyperalert_db
spring.datasource.username=root
spring.datasource.password=YOUR_DB_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=YOUR_EMAIL
spring.mail.password=YOUR_APP_PASSWORD
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

openweather.api.key=YOUR_OPENWEATHERMAP_API_KEY

jwt.secret=YOUR_JWT_SECRET_KEY_MIN_256_BITS
jwt.expiration=86400000
```

4. Run the application:
```bash
mvn spring-boot:run
```

5. Open browser:
```
http://localhost:8080
```

## 🔐 Security Notes

- `application.properties` is excluded from version control (contains sensitive credentials)
- Passwords stored using BCrypt hashing
- All data endpoints protected with JWT Bearer token authentication
- CSRF disabled for stateless REST API design

## 👩‍💻 Developer

**Dhivya V** — Final Year B.Tech Information Technology  
M.A.M. College of Engineering & Technology, Tiruchirappalli  
Anna University | Regulation 2021

---

*Built as a portfolio project targeting placement at product companies (Zoho, etc.)*