import os
from pathlib import Path
from typing import Any

import httpx
from dotenv import load_dotenv
from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import FileResponse, StreamingResponse
from fastapi.staticfiles import StaticFiles
from pydantic import BaseModel, Field

load_dotenv()

ROOT = Path(__file__).resolve().parent.parent
FRONTEND_DIR = ROOT / "frontend"
STATIC_DIR = FRONTEND_DIR / "static"

LLM_BASE_URL = os.getenv("LLM_BASE_URL", "https://api.openai.com/v1").rstrip("/")
LLM_API_KEY = os.getenv("LLM_API_KEY", "")
LLM_MODEL = os.getenv("LLM_MODEL", "")
MAX_HISTORY = max(1, int(os.getenv("MAX_HISTORY_MESSAGES", "30")))

app = FastAPI(title="HARSH-AI")
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)
app.mount("/static", StaticFiles(directory=str(STATIC_DIR)), name="static")


class ChatMessage(BaseModel):
    role: str = Field(..., min_length=1)
    content: str = ""
    name: str | None = None


class ChatRequest(BaseModel):
    messages: list[ChatMessage]
    system_prompt: str = "You are HARSH-AI, a helpful AI assistant."


@app.get("/")
async def home():
    return FileResponse(FRONTEND_DIR / "index.html")


@app.get("/health")
async def health():
    configured = bool(LLM_API_KEY and LLM_MODEL)
    return {
        "ok": configured,
        "provider_configured": configured,
        "model": LLM_MODEL or None,
        "base_url": LLM_BASE_URL,
    }


@app.get("/api/config")
async def config():
    return {
        "configured": bool(LLM_API_KEY and LLM_MODEL),
        "model": LLM_MODEL,
        "base_url": LLM_BASE_URL,
        "max_history": MAX_HISTORY,
    }


@app.post("/api/chat")
async def chat(payload: ChatRequest):
    if not LLM_API_KEY or not LLM_MODEL:
        raise HTTPException(500, "LLM_API_KEY and LLM_MODEL are not configured")

    cleaned_messages: list[dict[str, Any]] = []
    for item in payload.messages[-MAX_HISTORY:]:
        if not item.content:
            continue
        msg: dict[str, Any] = {"role": item.role, "content": item.content}
        if item.name:
            msg["name"] = item.name
        cleaned_messages.append(msg)

    messages = [{"role": "system", "content": payload.system_prompt}]
    messages.extend(cleaned_messages)

    headers = {
        "Authorization": f"Bearer {LLM_API_KEY}",
        "Content-Type": "application/json",
    }
    body = {
        "model": LLM_MODEL,
        "messages": messages,
        "stream": True,
        "temperature": 0.7,
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
                        if not line:
                            continue
                        yield line + "\n\n"
        except httpx.HTTPError as exc:
            yield f"data: ERROR: {exc}\n\n"
            yield "data: [DONE]\n\n"
        except Exception as exc:
            yield f"data: ERROR: {exc}\n\n"
            yield "data: [DONE]\n\n"

    return StreamingResponse(stream(), media_type="text/event-stream")
