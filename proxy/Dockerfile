FROM nginx:stable-alpine3.20-perl as final
#FROM ghcr.io/max-lt/nginx-jwt-module:v3.4.0 as final

ENV AUTH_PORT=8081

COPY ./front_test /usr/share/nginx/html
COPY ./nginx.conf /etc/nginx/templates/nginx.conf.template
COPY ./public.pem /etc/ssl/certs/public.pem

# Open port 8080
EXPOSE 8080
