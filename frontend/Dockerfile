FROM node:20-alpine3.21 AS build

WORKDIR /usr/app
COPY package*.json ./ 
RUN npm install
COPY . .
RUN npm run build

FROM nginx:stable-alpine3.20-perl

COPY --from=build /usr/app/dist /usr/share/nginx/html
COPY ./nginx.conf /etc/nginx/templates/nginx.conf.template
EXPOSE ${FRONTEND_PORT:-8082}