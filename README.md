# 🛒 E-Commerce Shop (Android)

![Build](https://img.shields.io/badge/build-passing-brightgreen)
![Firebase](https://img.shields.io/badge/Firebase-RealTimeDB-orange)
![License](https://img.shields.io/badge/license-MIT-blue)

אפליקציית חנות מקוונת לאנדרואיד עם **ניהול משתמשים, סל קניות חי, חיפוש חכם, חיפוש קולי, פאנל אדמין ופאנל משתמש אישי**.  
הפרויקט נבנה במסגרת **בחינת בגרות במחשבים**, וכולל **תיעוד מלא בחוברת רשמית**.

---


## ✨ תכונות עיקריות

### Client (משתמש קצה)
- 🔐 **Authentication Services** – הרשמה/כניסה מאובטחת (Firebase Auth)  
- 💾 **SharedPreferences** – שמירת נתוני משתמש מקומית  
- 🔄 **Firebase Realtime Database** – סנכרון בזמן אמת של מוצרים והזמנות  
- 🔍 **Smart Search** – חיפוש מיידי עם סינון מתקדם  
- 🎤 **Speech Recognizer** – חיפוש באמצעות קול  
- 🛍️ **Shopping Cart** – סל קניות דינמי בזמן אמת  
- 💳 **Demo Checkout** – תהליך רכישה לדמו  
- 🔔 **Notifications** – קבלת מבצעים ועדכוני הזמנה  

### User Panel
- ✏️ **עדכון פרטים אישיים** – עריכת שם, מייל ופרטי חשבון נוספים  
- 📜 **היסטוריית רכישות** – צפייה בהזמנות קודמות  
- 🎉 **מבצעים אישיים** – תצוגה מרוכזת של מבצעים פעילים  

### Admin Panel
- ➕ **הוספת מוצר חדש** לקטלוג  
- 👥 **ניהול משתמשים** – צפייה בפרטי משתמשים רשומים  
- 📜 **ניהול הזמנות** – צפייה בהזמנות שבוצעו  
- 🎉 **ניהול מבצעים** – יצירת מבצעים חדשים ושליחתם ללקוחות  

---

## 📊 מבנה המערכת

| רכיב | תיאור |
|------|--------|
| **Client App** | חיפוש מוצרים, סל קניות, פרופיל אישי |
| **User Panel** | עדכון פרטים, היסטוריית רכישות, מבצעים |
| **Admin Panel** | ניהול מוצרים, משתמשים והזמנות |
| **Authentication** | Firebase Authentication |
| **Database** | Firebase Realtime Database |
| **Local Storage** | SharedPreferences לשמירת session |
| **UI/UX** | Java + XML, RecyclerView, Notifications, Speech Recognizer |

---

## 🚀 התקנה והרצה
1. `git clone https://github.com/USERNAME/e_commerce_shop.git`  
2. פתח את הפרויקט ב־**Android Studio**  
3. הוסף את קובץ ה־`google-services.json` תחת התיקייה `app/`  
4. הרץ על אמולטור או מכשיר אמיתי (מומלץ API 30+)  

---

## 📑 תיעוד נוסף
לפרויקט מצורפת **חוברת הבגרות הרשמית** (PDF), הכוללת:
- אפיון מלא (Use Cases, ERD, תרשימי זרימה)  
- הסברים על עיצוב המערכת והבחירות הטכנולוגיות  
- בדיקות ותסריטי שימוש  
- סיכום ותובנות  

📥 [לחוברת הפרויקט המלאה (PDF)](Raz%20Eini%2025.pdf)

---

## 🧩 מה מייחד את הפרויקט?
- שילוב **Client + User Panel + Admin Panel** באפליקציה אחת  
- אינטגרציה מלאה עם **Firebase Authentication & Realtime Database**  
- תמיכה ב־**חיפוש חכם וקולי** לצד מערכת מבצעים עם התראות  
- פאנל אישי למשתמש עם **ניהול פרטים אישיים + היסטוריית רכישות + מבצעים**  
- כולל **תיעוד מקיף** כחלק מבחינת בגרות  

---

👨‍💻 פותח ע"י: **Raz Eini (2025)**  
