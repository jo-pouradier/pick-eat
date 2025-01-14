# Use the official PostgreSQL image from the Docker Hub
FROM postgres:latest

# Set environment variables for the PostgreSQL database
ENV POSTGRES_DB=${POSTGRES_DB:-postgres}
ENV POSTGRES_USER=${POSTGRES_USER:-postgres}
ENV POSTGRES_PASSWORD=${POSTGRES_PASSWORD:-postgres}

# Expose the default PostgreSQL port
EXPOSE 5432
