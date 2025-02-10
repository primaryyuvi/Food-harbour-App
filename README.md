# Food Harbor 🍽️🤝

## 🌟 Project Overview

Food Harbor is a compassionate mobile application designed to bridge the gap between donors and NGOs, transforming food donation into a seamless, impactful experience. By leveraging modern Android development technologies, we've created a platform that makes social impact accessible and transparent.

## 🎥 App Demo

[![Food Harbor App Demo](https://img.youtube.com/vi/0EJt4vMCGa/0.jpg)](https://youtu.be/0EJt4vMCGaU)

## 🎯 Problem Statement

In a world where hunger persists alongside food waste, Food Harbor provides a technological solution to:
- Connect individual donors with NGOs
- Facilitate direct, traceable food donations
- Create a transparent donation ecosystem
- Reduce food waste and hunger simultaneously

## 🚀 Key Differentiators

- **Real-time Donation Tracking**: Know exactly where your donation goes
- **User-Friendly Interface**: Simplifying the donation process
- **Comprehensive Donation History**: Track your social impact

## 📱 App Walkthrough

### 👤 User Journey
<p>
  <img src="/images/Landing-Screen.png" width="250" alt="Landing Screen">
  &nbsp;&nbsp;&nbsp;&nbsp;
  <img src="/images/Home-Screen-User.png" width="250" alt="User Home Screen">
  &nbsp;&nbsp;&nbsp;&nbsp;
  <img src="/images/Donation-Screen-User-1.png" width="250" alt="Donation Screen">
  &nbsp;&nbsp;&nbsp;&nbsp;
  <img src="/images/Profile-Screen-User-1.png" width="250" alt="User Profile">
</p>

### 🏢 NGO Perspective
<p>
  <img src="/images/Home-Screen-NGO.png" width="250" alt="NGO Home Screen">
  &nbsp;&nbsp;&nbsp;&nbsp;
  <img src="/images/Donation-Screen-NGO-1.png" width="250" alt="NGO Donation Management">
  &nbsp;&nbsp;&nbsp;&nbsp;
  <img src="/images/Donation-Screen-NGO-2.png" width="250" alt="NGO Donation Management">
  &nbsp;&nbsp;&nbsp;&nbsp;
  <img src="/images/Profile-Screen-NGO.png" width="250" alt="NGO Profile">
</p>

### 🤝 Interaction Ecosystem
<p>
  <img src="/images/Chat-Screen.png" width="250" alt="Communication Channel">
  &nbsp;&nbsp;&nbsp;&nbsp;
  <img src="/images/Donation-History.png" width="250" alt="Donation Tracking">
  &nbsp;&nbsp;&nbsp;&nbsp;
  <img src="/images/News-Screen.png" width="250" alt="Social Impact News">
</p>

## 🛠 Technical Architecture

### Design Philosophy
- **Clean Architecture**: Ensuring scalability and maintainability
- **MVVM Design Pattern**: Separating concerns, enhancing testability
- **Unidirectional Data Flow**: Predictable state management

### 🔧 Technology Stack
<table>
  <tr>
    <th>Category</th>
    <th>Technologies</th>
  </tr>
  <tr>
    <td>UI Framework</td>
    <td>Jetpack Compose, Material 3</td>
  </tr>
  <tr>
    <td>Asynchronous Programming</td>
    <td>Kotlin Coroutines, Flow</td>
  </tr>
  <tr>
    <td>Dependency Injection</td>
    <td>Hilt</td>
  </tr>
  <tr>
    <td>Local Storage</td>
    <td>Room Database</td>
  </tr>
  <tr>
    <td>Network</td>
    <td>Retrofit, OkHttp</td>
  </tr>
  <tr>
    <td>Image Loading</td>
    <td>Coil</td>
  </tr>
</table>

## 🏗️ Project Structure

```
food-harbor/
├── app/                   # App module
├── data/                  # Data layer
│   ├── local/             # Local database
│   ├── remote/            # Network calls
│   └── repository/        # Data repositories
├── domain/                # Business logic
│   ├── models/            # Data models
│   ├── usecases/          # Business logic
│   └── repository/        # Domain repositories
└── presentation/          # UI layer
    ├── screens/           # Composables
    ├── viewmodels/        # UI state management
    └── theme/             # Design system
```

## ✨ Features Highlights

- **User Features**
  - Profile Management
  - Donation Tracking
  - NGO Discovery

- **NGO Features**
  - Donation Management
  - Profile Verification
  - Communication Tools

- **Interaction Features**
  - Real-time Chat
  - Donation Confirmation
  - Impact Reporting

## 🚀 Getting Started

1. Clone the repository
2. Open in Android Studio Arctic Fox or later
3. Sync Gradle and run on emulator/device

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push and create a Pull Request

## 📄 License

Distributed under the MIT License. See `LICENSE` for more information.


---

**Built with ❤️ using Kotlin & Jetpack Compose**
