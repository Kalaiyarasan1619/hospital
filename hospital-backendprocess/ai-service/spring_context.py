"""
Server-side fetch of live hospital data from all Spring Boot services (via API Gateway).

Set AI_INTERNAL_KEY to the same value as app.aiInternalKey in:
user-service, patient, doctor, and pharmacy.
"""
import os

import requests

DEFAULT_GATEWAY = "http://localhost:9090"


def fetch_live_hospital_context() -> str:
    gateway = os.getenv("SPRING_GATEWAY_URL", DEFAULT_GATEWAY).rstrip("/")
    key = (os.getenv("AI_INTERNAL_KEY") or "").strip()
    if not key:
        return ""

    headers = {"X-AI-Internal-Key": key}
    parts: list[str] = []

    for path, label in (
        ("/api/users/internal/ai-context", "User accounts (Spring / user-service DB)"),
        ("/api/patients/internal/ai-context", "Patient registry (Spring / patient DB)"),
        ("/api/doctors/internal/ai-context", "Doctors (Spring / doctor DB)"),
        ("/api/pharmacy/internal/ai-context", "Pharmacy (Spring / pharmacy service)"),
    ):
        try:
            r = requests.get(f"{gateway}{path}", headers=headers, timeout=12)
            if r.status_code == 401:
                parts.append(f"({label}: unauthorized — check AI_INTERNAL_KEY matches Spring app.aiInternalKey)")
                continue
            if not r.ok:
                parts.append(f"({label}: HTTP {r.status_code})")
                continue
            text = (r.text or "").strip()
            if text:
                parts.append(f"## {label}\n{text}")
        except requests.RequestException as e:
            parts.append(f"({label}: unreachable — {e})")

    return "\n\n".join(parts) if parts else ""
