# HARSH-AI — Online Chatbot

A production-ready chat web app with a FastAPI backend and OpenAI-compatible LLM proxy.

## Deploy on Render

1. Open the Render dashboard and choose **New → Blueprint**.
2. Connect the GitHub repository `Harsh0675/HARSH-AI`.
3. Select the `main` branch and apply the Blueprint.
4. In the service environment variables, set:

```text
LLM_BASE_URL=https://api.openai.com/v1
LLM_API_KEY=your_api_key
LLM_MODEL=your_model
```

The included `render.yaml` configures the Python build command, Uvicorn start command, `/health` health check, and automatic deploys from `main`.

Never expose API keys in frontend JavaScript.

## Features

- FastAPI backend with streaming chat responses
- OpenAI-compatible API proxying
- Server-side secret handling only
- Frontend chat UI with local history
- Health check and deployment-ready config

## Local development

```bash
python -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
cp .env.example .env
# edit .env with your keys
uvicorn backend.main:app --reload --host 0.0.0.0 --port 8000
```
