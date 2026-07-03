<p align="center">
  <img src="screenshots/banner.png" alt="Job Portal Management System Banner" width="100%">
</p>

# 💼 Job Portal Management System
# 💼 Job Portal Management System

A full-stack Job Portal Management System that enables candidates to search and apply for jobs, recruiters to manage job postings and applications, and administrators to monitor the platform. The application is built using **Spring Boot**, **MongoDB**, **Spring Security**, and **Thymeleaf**, with **OTP-based email verification** for secure user registration.

---

## 🚀 Features

### 👨‍🎓 Candidate Module
- User Registration & Login
- Email OTP Verification
- Candidate Dashboard
- Search & Filter Jobs
- View Job Details
- Apply for Jobs
- Upload Resume (MongoDB GridFS)
- Track Application Status
- Receive Email Notifications

### 🏢 Recruiter Module
- Recruiter Registration & Login
- Recruiter Dashboard
- Company Profile Management
- Create Job Posts
- Edit Job Posts
- Delete Job Posts
- View Applicants
- Update Candidate Application Status
- Automatic Email Notifications to Candidates

### 👨‍💼 Admin Module
- Secure Admin Login
- View Registered Users
- View Job Listings
- Monitor Applications
- Manage Platform Data

---

# 🔐 Security Features

- Spring Security Authentication
- Role-Based Authorization
- Password Encryption
- Email OTP Verification
- Protected Routes
- Session-Based Authentication

---

# 📧 Email Features

- OTP Verification During Registration
- Application Status Update Emails
- Gmail SMTP Integration

---

# 📄 Resume Management

- Resume Upload
- Resume Storage using MongoDB GridFS
- Resume Retrieval

---

# 📊 Application Workflow

Candidate applies for a job

⬇

Recruiter reviews the application

⬇

Status Updates

- Applied
- Under Review
- Shortlisted
- Interview
- Selected
- Rejected

⬇

Candidate receives email notification

---

# 🛠 Tech Stack

| Category | Technology |
|----------|------------|
| Language | Java 17 |
| Backend | Spring Boot |
| Database | MongoDB |
| Security | Spring Security |
| Frontend | Thymeleaf |
| Styling | HTML, CSS, JavaScript |
| Build Tool | Maven |
| IDE | Eclipse |
| Email | Gmail SMTP |

---

# 📂 Project Structure

```
src
 ├── main
 │   ├── java
 │   │   └── com.jobportal
 │   │       ├── config
 │   │       ├── controller
 │   │       ├── dto
 │   │       ├── exception
 │   │       ├── model
 │   │       ├── repository
 │   │       ├── security
 │   │       └── service
 │   └── resources
 │       ├── static
 │       ├── templates
 │       └── application.properties
```

---

# 🗄 Database

Database: **MongoDB**

Collections

- Users
- Jobs
- Applications
- OTP Verifications
- Application Status History

GridFS is used for storing uploaded resumes.

---

# ⚙️ Installation

## Clone Repository

```bash
git clone https://github.com/Nithishreddychaikam/Job-Portal-Management-System.git
```

## Open Project

Import as an Existing Maven Project in Eclipse.

## Configure MongoDB

Start MongoDB locally.

Default:

```
mongodb://localhost:27017/jobportal
```

## Configure Email

Update your local `application.properties` (or use environment variables) with your own Gmail credentials:

```properties
spring.mail.username=${MAIL_USERNAME}
spring.mail.password=${MAIL_PASSWORD}
```

Generate your own Gmail App Password from your Google Account. Do **not** commit real credentials to the repository.

## Run

Run

```
JobPortalApplication.java
```

Open

```
http://localhost:8080
```

---
# 📸 Screenshots

## 🏠 Home Page

![Home Page](screenshots/home.png)

---

## 📧 OTP Verification

![OTP Verification](screenshots/otp-verification.png)

---

## 👨‍🎓 Candidate Dashboard

![Candidate Dashboard](screenshots/candidate-dashboard.png)

---

## 🔍 Find Jobs

![Find Jobs](screenshots/find-jobs.png)

---

## 📝 Apply for Jobs

![Apply for Jobs](screenshots/apply-for-jobs.png)

---

## 🏢 Recruiter's Dashboard

![Recruiter's Dashboard](screenshots/recruiter-dashboard.png)

---

## ➕ Post New Job

![Post New Job](screenshots/post-new-job.png)

---

# 🚀 Future Enhancements

- JWT Authentication
- Resume Parsing
- AI Resume Matching
- Interview Scheduling
- Company Reviews
- Job Recommendations
- Analytics Dashboard
- Docker Deployment
- Cloud Deployment

---

# 👨‍💻 Author

**Chaikam Nithish Reddy**

GitHub: https://github.com/Nithishreddychaikam

---

# ⭐ Support

If you found this project useful, consider giving it a ⭐ on GitHub.
