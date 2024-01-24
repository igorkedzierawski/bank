FROM nginx:1.25.3-alpine3.18

COPY ./nginx.conf /etc/nginx/nginx.conf
COPY ./sv_cert.crt /etc/nginx/ssl_bundle/sv_cert.crt
COPY ./sv_privkey.key /etc/nginx/ssl_bundle/sv_privkey.key

EXPOSE 80
