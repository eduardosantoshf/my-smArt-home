FROM nginx:alpine
RUN rm -rf /usr/share/nginx/html
COPY . /usr/share/nginx/html
#FROM nginx:alpine
#COPY . /usr/share/nginx/html
# This Dockerfile is used for a youtube tutorial
# base image - nginx with tag "latest"
#FROM nginx:latest
#COPY . /usr/share/nginx/html
#COPY . /var/www/html
# Adding read permissions to custom index.html
RUN chmod +r /usr/share/nginx/html/Login.html
# 'nginx -g daemon off" will run as default command when any container is run that uses the image that was built using this Dockerfile"
#CMD ["nginx", "-g", "daemon off;"]