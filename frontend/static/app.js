const $ = (s) => document.querySelector(s);
const box = $("#messages"), input = $("#input"), history = $("#history");
let messages = JSON.parse(localStorage.getItem("harsh_messages") || "[]");

function save() {
  localStorage.setItem("harsh_messages", JSON.stringify(messages));
}

function render() {
  box.innerHTML = "";
  if (!messages.length) {
    box.innerHTML = '<div class="welcome"><h1>How can I help?</h1><p>Chat with HARSH-AI.</p></div>';
    return;
  }
  messages.forEach((m) => add(m.role, m.content));
  box.scrollTop = box.scrollHeight;
}

function add(role, text) {
  const row = document.createElement("div");
  row.className = "msg " + role;
  row.innerHTML = '<div class="avatar">' + (role === "user" ? "U" : "🤖") + '</div><div class="bubble"></div>';
  row.querySelector(".bubble").textContent = text;
  box.appendChild(row);
  return row.querySelector(".bubble");
}

async function send(text) {
  if (!text.trim()) return;

  messages.push({ role: "user", content: text });
  save();
  render();

  const out = add("assistant", "");
  let answer = "";

  const r = await fetch("/api/chat", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ messages }),
  });

  const reader = r.body.getReader();
  const decoder = new TextDecoder();
  let buf = "";

  while (true) {
    const { value, done } = await reader.read();
    if (done) break;

    buf += decoder.decode(value, { stream: true });
    const parts = buf.split("\n\n");
    buf = parts.pop();

    for (const part of parts) {
      if (!part.startsWith("data:")) continue;

      const data = part.slice(5).trim();
      if (data === "[DONE]") continue;
      if (data.startsWith("ERROR:")) {
        answer = data;
        out.textContent = answer;
        continue;
      }

      try {
        const obj = JSON.parse(data);
        const token = obj.choices?.[0]?.delta?.content || "";
        answer += token;
        out.textContent = answer;
        box.scrollTop = box.scrollHeight;
      } catch {}
    }
  }

  messages.push({ role: "assistant", content: answer });
  save();
}

$("#form").onsubmit = (e) => {
  e.preventDefault();
  const v = input.value;
  input.value = "";
  send(v);
};

$("#new").onclick = () => {
  messages = [];
  save();
  render();
};

$("#menu").onclick = () => $("#sidebar").classList.toggle("open");
input.onkeydown = (e) => {
  if (e.key === "Enter" && !e.shiftKey) {
    e.preventDefault();
    $("#form").requestSubmit();
  }
};

async function health() {
  try {
    const r = await fetch("/health");
    const x = await r.json();
    $("#status").style.color = x.ok ? "#45c477" : "#888";
  } catch {}
}

render();
health();
