# 🌍 Netty Country Info Project (Spring WebFlux)

A **reactive, non-blocking** Country Information app built using **Spring Boot + Spring WebFlux (Netty runtime)**.  
It integrates with the **REST Countries API** and exposes a backend endpoint that returns **structured country metadata** consumed by a modern responsive UI.

---

## 🚀 Why This Project

This project demonstrates:

- **Reactive Programming** with **Project Reactor (Mono)**
- **Non-blocking HTTP client** using **Spring WebClient**
- **Netty event-loop runtime** via WebFlux
- Clean JSON transformation (API → normalized response contract)
- Simple UI consuming a backend API (POST request)

---

## 🧰 Tech Stack

### Backend
| Technology | Purpose |
|------------|---------|
| Java 17+ | Core language |
| Spring Boot | Application framework |
| Spring WebFlux (Netty) | Reactive runtime |
| WebClient | Non-blocking HTTP client |
| Project Reactor (Mono) | Reactive streams |
| Jackson | JSON parsing & mapping |

### Frontend
| Technology | Purpose |
|------------|---------|
| HTML + CSS | Responsive card layout |
| Fetch API | POST requests to backend |

### External API
| API | Link |
|-----|------|
| REST Countries API | [https://restcountries.com](https://restcountries.com) |

---

## ✨ Features

- 🔍 Search any country by name
- 📄 Returns structured data:
  - Official/common name, capital, region/subregion
  - Flag + description, coat of arms
  - Population, languages, demonyms
  - Currencies, calling code
  - Borders, coordinates, area, landlocked status
  - Google Maps + OpenStreetMap links
  - UN membership, independence, FIFA code
- 📱 Responsive UI with card-based sections

---

## ⚙️ How It Works
```
UI takes country name input
        ↓
UI calls backend POST /gci
        ↓
Backend uses WebClient to call:
https://restcountries.com/v3.1/name/{country}
        ↓
Response is parsed via Jackson
        ↓
Service normalizes the external payload
into a clean JSON response
        ↓
UI renders data into cards
```

---

## 📌 API Contract

### Endpoint
```
POST /gci
Content-Type: application/json
```

### Request Body
```json
{
  "countryName": "India"
}
```

### ✅ Success Response
```json
{
  "status": "success",
  "countryName": "India",
  "officialName": "Republic of India",
  "capital": "New Delhi",
  "region": "Asia",
  "subregion": "Southern Asia",
  "population": 1400000000,
  "area": 3287263,
  "landlocked": false,
  "borders": "CHN, PAK, NPL, BTN, MMR, BGD",
  "coordinates": "20.00°N, 77.00°E",
  "currencies": "Indian rupee (₹)",
  "callingCode": "+91",
  "languages": "Hindi",
  "flagPath": "https://flagcdn.com/w320/in.png",
  "googleMaps": "https://...",
  "openStreetMaps": "https://..."
}
```

### ❌ Failure Response
```json
{
  "status": "failed",
  "errmsg": "error message"
}
```

---

## 📁 Project Structure
```
NettyCountryInfoProject/
├── backend/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/example/NettyCountryInfoProject/
│   │       │   ├── NettyCountryInfoProjectApplication.java
│   │       │   ├── NettyCountryInfoProjectController.java
│   │       │   └── NettyCountryInfoProjectService.java
│   │       └── resources/
│   │           ├── static/
│   │           │   └── index.html
│   │           └── application.properties
│   └── pom.xml
└── README.md
```

---

## ▶️ Run Locally

### Prerequisites

- Java 17+ (recommended)
- Maven
- Any modern browser

### Steps

**1. Clone the repo**
```bash
git clone <your-repo-url>
cd NettyCountryInfoProject
```

**2. Run Spring Boot**
```bash
cd backend
mvn spring-boot:run
```

**3. Open the UI**

If `index.html` is inside `resources/static`:
```
http://localhost:8080/
```

Otherwise open the HTML file directly and ensure the backend is running on:
```
http://localhost:8080/gci
```

---

## 🧪 Quick Test

Try searching these countries:

| Country | Region |
|---------|--------|
| India | Southern Asia |
| United States | Northern America |
| Japan | Eastern Asia |
| France | Western Europe |
| Brazil | South America |

---

## 🔒 Future Improvements

- [ ] Add input validation + global exception handling
- [ ] Add caching for repeated queries (Caffeine / Redis)
- [ ] Add reactive timeouts + retries
- [ ] Add unit tests using `WebTestClient`
- [ ] Dockerize + deploy (Cloud Run / Render / Railway)

---

## 📸 Demo

_Add screenshots or a short GIF of the UI here after running locally._

---

## 👤 Author

**Sakshi Tokekar**  
Java | Spring Boot | Reactive WebFlux | APIs
