FROM nginx:stable-alpine3.20-perl AS final
#FROM ghcr.io/max-lt/nginx-jwt-module:v3.4.0 as final

COPY ./nginx.conf.template /etc/nginx/templates/default.conf.template
COPY ./public.pem /etc/ssl/certs/public.pem
COPY ./certs /etc/ssl/certs
COPY ./private /etc/ssl/private
COPY ./restaurants_photos /mnt/storage/restaurants_photos
COPY ./icons /mnt/storage/icons

EXPOSE 80
EXPOSE 443
