FROM tiangolo/uvicorn-gunicorn-fastapi:python3.10 AS base

WORKDIR /app

COPY requirements.txt /app

RUN pip install --no-cache-dir -r /app/requirements.txt
RUN apt update && apt install -y libgl1-mesa-glx

FROM base AS prod

WORKDIR /app

COPY . .

ENV BILLING_PORT=8000
ENV BROKER_HOST=active_mq
ENV BROKER_PORT=61613
ENV BROKER_USER=admin
ENV BROKER_PASS=admin

CMD ["sh", "-c", "uvicorn app.main:app --host 0.0.0.0 --port ${BILLING_PORT}"]
