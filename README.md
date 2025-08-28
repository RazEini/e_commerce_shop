<div dir="rtl">

<h1>🛒 E-Commerce Shop (Android)</h1>

<img src="https://img.shields.io/badge/build-passing-brightgreen">
<img src="https://img.shields.io/badge/Firebase-RealTimeDB-orange">
<img src="https://img.shields.io/badge/license-MIT-blue">

<p>אפליקציית חנות מקוונת לאנדרואיד עם <strong>ניהול משתמשים, סל קניות חי, חיפוש חכם, חיפוש קולי, פאנל אדמין ופאנל משתמש אישי</strong>.<br>
הפרויקט נבנה במסגרת <strong>בחינת בגרות במחשבים</strong>, וכולל <strong>תיעוד מלא בחוברת רשמית</strong>.</p>

<hr>

<h2>🎥 Demo – מצגת תכונות</h2>

<div style="display:flex; gap:10px; justify-content: flex-start;">
  <img src="assets/gifs/demo1.gif" alt="Client Demo" width="220px">
  <img src="assets/gifs/demo2.gif" alt="User Panel Demo" width="220px">
  <img src="assets/gifs/demo3.gif" alt="Admin Panel Demo" width="220px">
</div>

<hr>

<h2>✨ תכונות עיקריות</h2>

<h3>🔹 משתמש קצה (Client)</h3>
<ul dir = "rtl">
<li>🔐 <strong>Authentication Services</strong> – הרשמה/כניסה מאובטחת (Firebase Auth)</li>
<li>💾 <strong>SharedPreferences</strong> – שמירת נתוני משתמש מקומית</li>
<li>🔄 <strong>Firebase Realtime Database</strong> – סנכרון בזמן אמת של מוצרים והזמנות</li>
<li>🔍 <strong>Smart Search</strong> – חיפוש מיידי עם סינון מתקדם</li>
<li>🎤 <strong>Speech Recognizer</strong> – חיפוש באמצעות קול</li>
<li>🛍️ <strong>Shopping Cart</strong> – סל קניות דינמי בזמן אמת</li>
<li>💳 <strong>Demo Checkout</strong> – תהליך רכישה לדמו</li>
<li>🔔 <strong>Notifications</strong> – קבלת מבצעים ועדכוני הזמנה</li>
</ul>

<h3>🔹 פאנל משתמש (User Panel)</h3>
<ul>
<li>✏️ <strong>עדכון פרטים אישיים</strong> – עריכת שם, מייל ופרטי חשבון נוספים</li>
<li>📜 <strong>היסטוריית רכישות</strong> – צפייה בהזמנות קודמות</li>
<li>🎉 <strong>מבצעים אישיים</strong> – תצוגה מרוכזת של מבצעים פעילים</li>
</ul>

<h3>🔹 פאנל אדמין (Admin Panel)</h3>
<ul>
<li>➕ <strong>הוספת מוצר חדש</strong> לקטלוג</li>
<li>👥 <strong>ניהול משתמשים</strong> – צפייה בפרטי משתמשים רשומים</li>
<li>📜 <strong>ניהול הזמנות</strong> – צפייה בהזמנות שבוצעו</li>
<li>🎉 <strong>ניהול מבצעים</strong> – יצירת מבצעים חדשים ושליחתם ללקוחות</li>
</ul>

<hr>

<h2>📊 מבנה המערכת</h2>

<table>
<tr><th>רכיב</th><th>תיאור</th></tr>
<tr><td><strong>Client App</strong></td><td>חיפוש מוצרים, סל קניות, פרופיל אישי</td></tr>
<tr><td><strong>User Panel</strong></td><td>עדכון פרטים, היסטוריית רכישות, מבצעים</td></tr>
<tr><td><strong>Admin Panel</strong></td><td>ניהול מוצרים, משתמשים והזמנות</td></tr>
<tr><td><strong>Authentication</strong></td><td>Firebase Authentication</td></tr>
<tr><td><strong>Database</strong></td><td>Firebase Realtime Database</td></tr>
<tr><td><strong>Local Storage</strong></td><td>SharedPreferences לשמירת session</td></tr>
<tr><td><strong>UI/UX</strong></td><td>Java + XML, RecyclerView, Notifications, Speech Recognizer</td></tr>
</table>

<hr>

<h2>🚀 התקנה והרצה</h2>
<ol dir = "rtl">
<li><code>git clone https://github.com/RazEini/e_commerce_shop.git</code></li>
<li>פתח את הפרויקט ב־<strong>Android Studio</strong></li>
<li>הוסף את קובץ ה־<code>google-services.json</code> תחת התיקייה <code>app/</code></li>
<li>הרץ על אמולטור או מכשיר אמיתי (מומלץ API 30+)</li>
</ol>

<hr>

<h2>📑 תיעוד נוסף</h2>
<ul>
<li>אפיון מלא (Use Cases, ERD, תרשימי זרימה)</li>
<li>הסברים על עיצוב המערכת והבחירות הטכנולוגיות</li>
<li>בדיקות ותסריטי שימוש</li>
<li>סיכום ותובנות</li>
</ul>

<p>📥 <a href="Raz%20Eini%2025.pdf">לחוברת הפרויקט המלאה (PDF)</a></p>

<hr>

<h2>🧩 מה מייחד את הפרויקט?</h2>
<ul>
<li>שילוב <strong>Client + User Panel + Admin Panel</strong> באפליקציה אחת</li>
<li>אינטגרציה מלאה עם <strong>Firebase Authentication & Realtime Database</strong></li>
<li>תמיכה ב־<strong>חיפוש חכם וקולי</strong> לצד מערכת מבצעים עם התראות</li>
<li>פאנל אישי למשתמש עם <strong>ניהול פרטים אישיים + היסטוריית רכישות + מבצעים</strong></li>
<li>כולל <strong>תיעוד מקיף</strong> כחלק מבחינת בגרות</li>
</ul>

<hr>

<hr>

<h2>🛠️ טכנולוגיות</h2>
<p dir = "rtl">Java + XML · Firebase (Auth & Realtime DB) · SharedPreferences · RecyclerView · Notifications · Speech Recognizer</p>

<hr>

<h2>📌 סטטוס</h2>
<p>גרסת דמו מלאה שפותחה כחלק מבחינת בגרות · קוד פתוח (MIT License)</p>

<hr>

<h2>📄 רישיון</h2>
<p>
    הפרויקט מופץ תחת רישיון <strong>MIT</strong> – חופשי לשימוש, שינוי והפצה, כל עוד נשמר קרדיט למחבר.
</p>
<p>למידע נוסף ראה את קובץ <a href="LICENSE">LICENSE</a></p>
<hr>

<p>👨‍💻 פותח ע"י: <strong>Raz Eini (2025)</strong></p>

</div>
