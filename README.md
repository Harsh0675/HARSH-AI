# HARSH-AI — Online Chatbot

A minimal production-ready ChatGPT-style web chatbot.

## What changed

- Removed Android/Termux/local llama.cpp-specific files
- Removed unnecessary folders and scripts
- Kept only the online frontend + backend
- API key stays server-side in environment variables
- Supports any OpenAI-compatible chat API

## Deploy

### Render

Create a Python Web Service from this repository.

Build command:
```bash
pip install -r backend/requirements.txt
```

Start command:
```bash
uvicorn backend.main:app --host 0.0.0.0 --port $PORT
```

Set these environment variables in Render:

```text
LLM_BASE_URL=https://api.openai.com/v1
LLM_API_KEY=your_api_key
LLM_MODEL=your_model
```

Never put the API key in frontend JavaScript.

## Local run

```bash
pip install -r backend/requirements.txt
export LLM_BASE_URL=https://api.openai.com/v1
export LLM_API_KEY=your_api_key
export LLM_MODEL=your_model
uvicorn backend.main:app --host 127.0.0.1 --port 8000
```

Open:
`http://127.0.0.1:8000`
