# Social Hub — Android پروجیکٹ (اردو)

یہ ابتدائی Android پروجیکٹ پانچ پلیٹ فارمز کے لیے ایک ہوم اسکرین فراہم کرتا ہے: Facebook، Instagram، TikTok، YouTube اور X۔ آئیکن پر ٹیپ کرنے سے متعلقہ پلیٹ فارم کا موبائل ویب صفحہ ایپ کے اندر WebView میں کھلتا ہے۔ یہ اصل پلیٹ فارمز کی ایپس کو ایک APK میں شامل نہیں کرتا، اور تمام فیچرز کی ضمانت نہیں ہے۔

## صرف Android موبائل سے APK بنانے کی کوشش

1. ZIP فائل کو موبائل میں ڈاؤن لوڈ کریں اور Extract کریں۔
2. کسی ایسے Android IDE/build tool میں `SocialHub` فولڈر کھولیں جو جدید Android Gradle Plugin اور Kotlin کی حمایت کرتا ہو۔
3. پروجیکٹ Sync/Build کریں۔ `app/build/outputs/apk/debug/app-debug.apk` عام متوقع output path ہے۔
4. اگر build error آئے تو اس کا مکمل اسکرین شاٹ یا متن محفوظ کریں؛ ممکن ہے موبائل IDE اس Gradle project کو سپورٹ نہ کرے۔

## اہم حدود

- Android SDK اور compatible Gradle/Kotlin build tools درکار ہیں۔
- بعض پلیٹ فارمز WebView میں لاگ اِن، ویڈیو، پیغامات یا دوسری خصوصیات محدود کر سکتے ہیں۔
- اپنے اکاؤنٹ کا پاس ورڈ صرف آفیشل پلیٹ فارم کے صفحے پر درج کریں۔
- یہ سورس پروجیکٹ ہے، compile شدہ یا device-tested APK نہیں۔
