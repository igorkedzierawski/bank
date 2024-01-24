#!/bin/sh
openssl genrsa -out private-key.pem.temp 2048
openssl rsa -pubout -in private-key.pem.temp -out public-key.pem
openssl pkcs8 -topk8 -inform PEM -in private-key.pem.temp -out private-key.pem -nocrypt
printf "RSA_PUBLIC_KEY=%s\nRSA_PRIVATE_KEY=%s\n" \
    "$(cat public-key.pem | sed ':a;N;$!ba;s/\n//g')" \
    "$(cat private-key.pem | sed ':a;N;$!ba;s/\n//g')"\
    > rsa_keys.env
rm private-key.pem.temp public-key.pem private-key.pem
