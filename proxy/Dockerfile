FROM nginx:stable-alpine-perl as final

# Copy the folder front_test to the nginx folder
COPY ./front_test /usr/share/nginx/html
COPY ./nginx.conf /etc/nginx/nginx.conf

# Open port 8080
EXPOSE 8080
