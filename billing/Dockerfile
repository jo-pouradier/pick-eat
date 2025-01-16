FROM tiangolo/uvicorn-gunicorn-fastapi:python3.10 AS base

WORKDIR /app

COPY requirements.txt /app

RUN pip install --no-cache-dir -r /app/requirements.txt

FROM base AS prod

WORKDIR /app

COPY . .

ENV PORT=8000

CMD ["uvicorn", "app.main:app", "--host", "0.0.0.0", "--port", "${PORT}"]
