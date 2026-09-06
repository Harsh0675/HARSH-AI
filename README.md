# 🤖 HARSH-AI

> A locally running AI assistant powered by a custom GGUF model and llama.cpp.

**HARSH-AI** is Harsh Nagar's personal local AI project, designed to run directly on Android through Termux and llama.cpp. It can be exposed securely on a private Wi-Fi network so other devices can use the same local AI server.

## ✨ Highlights

- 🧠 Local GGUF model inference
- 📱 Android + Termux friendly
- ⚡ llama.cpp powered
- 🌐 Private LAN access
- 💬 Built-in llama.cpp web interface
- 🔒 No public model upload required
- 🛠️ Designed as an open-source showcase project

## 🚀 Run HARSH-AI on Android

The model file is intentionally **not included in GitHub** because it is large. Keep your local model at:

```text
~/harsh-ai/models/HARSH-AI.gguf
```

Start the server:

```bash
cd ~/harsh-ai/llama.cpp

./build/bin/llama-server \
  -m ~/harsh-ai/models/HARSH-AI.gguf \
  --host 0.0.0.0 \
  --port 8080 \
  -c 2048
```

When you see:

```text
model loaded
listening on http://0.0.0.0:8080
```

HARSH-AI is ready.

## 📡 Use It From Another Device

Connect the second device to the same Wi-Fi network as the Android phone running HARSH-AI.

Find the phone's Wi-Fi address:

```bash
ifconfig
```

Then open:

```text
http://PHONE_IP:8080
```

Example:

```text
http://192.168.x.x:8080
```

## 🎨 HARSH-AI Branding

The web interface is presented as **HARSH-AI**, making the project easy to identify when demonstrated from the local server.

## ⚠️ Security

The default LAN configuration is intended for private Wi-Fi testing. Do **not** expose port 8080 directly to the public internet without authentication, access controls, and a secure deployment setup.

## 📦 Repository Contents

This repository contains the project documentation and supporting configuration/scripts. The large `.gguf` model is kept outside GitHub.

Recommended `.gitignore` entries:

```gitignore
*.gguf
models/
*.bin
```

## 🧰 Technology

- C++ / llama.cpp
- GGUF model format
- Termux on Android
- HTTP web server
- Local network access

## 👨‍💻 Creator

**Harsh Nagar**  
GitHub: [Harsh0675](https://github.com/Harsh0675)

---

⭐ If you find HARSH-AI interesting, consider starring the repository.
