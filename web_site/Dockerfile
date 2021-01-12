#FROM nginx:alpine
#COPY . /usr/share/nginx/html
# This Dockerfile is used for a youtube tutorial
# base image - nginx with tag "latest"
FROM nginx:latest
COPY . /usr/share/nginx/html
COPY . /var/www/html

COPY ./js/ /usr/share/nginx/html
COPY ./css/ /usr/share/nginx/html
COPY ./Register.html /usr/share/nginx/html
COPY ./Login.html /usr/share/nginx/html
COPY ./Profile.html /usr/share/nginx/html
COPY ./Devices.html /usr/share/nginx/html

COPY ./web-site/js/ /var/www/html
COPY ./web-site/css/ /var/www/html
COPY ./web-site/Register.html /var/www/html
COPY ./web-site/Login.html /var/www/html
COPY ./web-site/Profile.html /var/www/html
COPY ./web-site/Devices.html /var/www/html

# Adding read permissions to custom index.html
RUN chmod +r /usr/share/nginx/html/index.html
# 'nginx -g daemon off" will run as default command when any container is run that uses the image that was built using this Dockerfile"
CMD ["nginx", "-g", "daemon off;"]

