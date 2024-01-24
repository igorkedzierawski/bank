#!/bin/sh
# przykładowy wizard tworzący lokalny CA i podpisujący nim certyfikat dla serwera
# IK 2023

# Ścieżki do plików:
# Klucz prywatny dla CA
CA_PRIVKEY="ca_privkey.key"
# Samopodpisany certyfikat dla CA
CA_CERT="ca_cert.crt"
# Klucz prywatny dla serwera
SV_PRIVKEY="sv_privkey.key"
# Request o podpisanie certyfikatu dla serwera
SV_SIGN_REQ="sv_sign_req.csr"
# Podpisany certyfikat dla serwera
SV_CERT="sv_cert.crt"
# Plik keystore w formacie PKCS12 dla serwera
SV_KEYSTORE="sv_keystore.p12"
# Plik konfiguracyjny certyfikatu serwera
OPENSSL_CONF="openssl.conf"


# Sprzątacz śmieci powstałych po odpaleniu tego skryptu
# Wywołanie może być konieczne przy wielokrotnych uruchomieniach wizarda
if [ "$1" = "clear" ]; then
    rm "$CA_PRIVKEY" "$CA_CERT" "$SV_PRIVKEY" \
       "$SV_SIGN_REQ" "$SV_CERT" "$SV_KEYSTORE" \
       "$OPENSSL_CONF" "$(basename -s .crt "$CA_CERT").srl"
    echo "Usunięto wszystkie wygenerowane pliki"
    exit 0
fi


#
# Generacja certyfikatu dla naszego Certificate Authority z jakimiś przykładowymi danymi 
# (Wygenerowany CA może być użyty do podpisu wielu certyfikatów serwerów)
#

# Generowanie klucza prywatnego dla CA
openssl genpkey -algorithm RSA -out "$CA_PRIVKEY"
[ "$?" = 0 ] || exit 1

# Generowanie samopodpisanego certyfikatu dla CA
openssl req -new -x509 -key "$CA_PRIVKEY" -out "$CA_CERT" <<EOF
PL
nopCA
nopCA
nopCA
nopCA
nopCA
nopca@nopca.pl
EOF
[ "$?" = 0 ] || exit 1

#
# Generacja certyfikatu dla naszego serwera za pomocą wcześniej stworzonego Certificate Authority
#

# Generowanie klucza prywatnego dla serwera
openssl genpkey -algorithm RSA -out "$SV_PRIVKEY"
[ "$?" = 0 ] || exit 1

# Generowanie pliku konfiguracyjnego certyfikatu serwera 
# (Ważne jest tu ustawienie poprawnych domen)
cat > "$OPENSSL_CONF" <<EOF
[req]
distinguished_name = req_distinguished_name
req_extensions = v3_req
prompt = no
[req_distinguished_name]
C = PL
ST = Bank
L = Bank
O = Bank
OU = Bank
CN = Bank
[v3_req]
#keyUsage = keyEncipherment, dataEncipherment
keyUsage = digitalSignature, keyEncipherment
extendedKeyUsage = serverAuth
subjectAltName = @alt_names
[alt_names]
DNS.1 = localhost
DNS.2 = bank.verysecure
DNS.3 = *.bank.verysecure
EOF
[ "$?" = 0 ] || exit 1

# Generowanie requesta o podpisanie certyfikatu dla serwera
openssl req -new -key "$SV_PRIVKEY" -config "$OPENSSL_CONF" -out "$SV_SIGN_REQ"
[ "$?" = 0 ] || exit 1

# Podpisanie requesta przez nasze CA
openssl x509 -req -in "$SV_SIGN_REQ" -extfile "$OPENSSL_CONF" -extensions v3_req \
    -CA "$CA_CERT" -CAkey "$CA_PRIVKEY" -CAcreateserial -out "$SV_CERT" -days 365 -sha512
[ "$?" = 0 ] || exit 1


#
# Opcjonalne spakowanie tego do PKCS12
#

# Spakowanie klucza i certyfikatu serwera do pliku keystore w formacie PKCS12
while true; do
    read -p "Czy chcesz spakować certyfikat dla serwera w PKCS12? (y/n)" choice
    [ "n" = "$choice" ] || [ "N" = "$choice" ] && break
    openssl pkcs12 -export -out "$SV_KEYSTORE" -inkey "$SV_PRIVKEY" -in "$SV_CERT" && break
done

echo "Gotowe, utworzono:
    Certificate Authority:
        Klucz:          $CA_PRIVKEY
        Certyfikat:     $CA_CERT (<- zainstalować w przeglądarce)
    Podpisano certyfikat dla serwera za pomocą powyższego CA:
        Klucz:          $SV_PRIVKEY
        Certyfikat:     $SV_CERT"
if [ "y" = "$choice" ] || [ "Y" = "$choice" ]; then
    echo "        (key,cert).p12: $SV_KEYSTORE (<- pamiętać o haśle)"
fi

# Przykładowe użycie:
#   - Spring/application.yml:
#       server.ssl:
#         key-store-type: PKCS12
#         key-store: classpath:cp.to.server_keystore.p12
#         key-store-password: "your password"
#         enabled: true
#   - nginx/*.conf:
#       ssl_certificate     /path/to/server.crt;
#       ssl_certificate_key /path/to/server.key;
