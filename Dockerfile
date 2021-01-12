FROM httpd:2.4

COPY ./js/ /usr/local/apache2/htdocs/
COPY ./css/ /usr/local/apache2/htdocs/
COPY ./Register.html /usr/local/apache2/htdocs/
COPY ./Login.html /usr/local/apache2/htdocs/
COPY ./Profile.html /usr/local/apache2/htdocs/
COPY ./Devices.html /usr/local/apache2/htdocs/

