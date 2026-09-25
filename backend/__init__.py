import os
from pathlib import Path

import httpx
from dotenv import load_dotenv
from fastapi import FastAPI, HTTPException
from fastapi.responses import FileResponse, StreamingResponse
from fastapi.staticfiles import StaticFiles
from pydantic import BaseModel

load_dotenv()

ROOT = Path(__file__).resolve().parent.parent
LLM_BASE_URL = os.getenv("LLM_BASE_URL", "https://api.openai.com/v1").rstrip("/")
LLM_API_KEY = os.getenv("LLM_API_KEY", "")
LLM_MODEL = os.getenv("LLM_MODEL", "")
MAX_HISTORY = int(os.getenv("MAX_HISTORY_MESSAGES", "30"))

app = FastAPI(title="HARSH-AI")
app.mount("/static", StaticFiles(directory=str(ROOT / "frontend" / "static")), name="static")


class ChatRequest(BaseModel):
    messages: list[dict]
    system_prompt: str = "You are HARSH-AI, a helpful AI assistant."


@app.get("/")
async def home():
    return FileResponse(ROOT / "frontend" / "index.html")


@app.get("/health")
async def health():
    return {
        "ok": bool(LLM_API_KEY and LLM_MODEL),
        "provider_configured": bool(LLM_API_KEY and LLM_MODEL),
    }


@app.post("/api/chat")
async def chat(payload: ChatRequest):
    if not LLM_API_KEY or not LLM_MODEL:
        raise HTTPException(500, "LLM_API_KEY and LLM_MODEL are not configured")

    messages = [{"role": "system", "content": payload.system_prompt}]
    messages.extend(payload.messages[-MAX_HISTORY:])

    headers = {
        "Authorization": f"Bearer {LLM_API_KEY}",
        "Content-Type": "application/json",
    }
    body = {
        "model": LLM_MODEL,
        "messages": messages,
        "stream": True,
    }

    async def stream():
        try:
            async with httpx.AsyncClient(timeout=None) as client:
                async with client.stream(
                    "POST",
                    f"{LLM_BASE_URL}/chat/completions",
                    headers=headers,
                    json=body,
                ) as r:
                    if r.status_code >= 400:
                        detail = (await r.aread()).decode(errors="replace")
                        yield f"data: ERROR: {detail}\n\n"
                        yield "data: [DONE]\n\n"
                        return

                    async for line in r.aiter_lines():
                        if line:
                            yield line + "\n\n"
        except Exception as e:
            yield f"data: ERROR: {e}\n\n"
            yield "data: [DONE]\n\n"

    return StreamingResponse(stream(), media_type="text/event-stream")
