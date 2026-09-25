# HARSH-AI — Online Chatbot

A production-ready chat web app with a FastAPI backend and OpenAI-compatible LLM proxy.

## Features

- FastAPI backend with streaming chat responses
- OpenAI-compatible API proxying
- Server-side secret handling only
- Frontend chat UI with local history
- Health check and deployment-ready config

## Project structure

```text
.
├── backend/
│   ├── __init__.py
│   ├── main.py
│   └── requirements.txt
├── frontend/
│   ├── index.html
│   └── static/
│       ├── app.js
│       └── style.css
├── .env.example
├── README.md
├── requirements.txt
├── render.yaml
└── main.py
```

## Local development

```bash
python -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
cp .env.example .env
# edit .env with your keys
uvicorn backend.main:app --reload --host 0.0.0.0 --port 8000
```

## Production deployment

Use Render, Railway, Fly.io, or any Python web host:

```bash
pip install -r backend/requirements.txt
uvicorn backend.main:app --host 0.0.0.0 --port $PORT
```

Set these environment variables:

```bash
LLM_BASE_URL=https://api.openai.com/v1
LLM_API_KEY=your_api_key
LLM_MODEL=your_model
MAX_HISTORY_MESSAGES=30
```

Never expose API keys in frontend JavaScript.
