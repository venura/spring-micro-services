Use vault to store secrets.Run vault with docker
>docker run -d -p 8200:8200 --name vault -e 'VAULT_DEV_ROOT_TOKEN_ID=myroot'  -e 'VAULT_DEV_LISTEN_ADDRESS=0.0.0.0:8200' vault

or
>docker run -it --rm --name vault -p 8200:8200 -e 'VAULT_DEV_ROOT_TOKEN_ID=myroot'  -e 'VAULT_DEV_LISTEN_ADDRESS=0.0.0.0:8200'

http://0.0.0.0:8200/ui/vault/auth

run keycloak
> docker run --rm --name keycloak -e KEYCLOAK_USER=admin -e KEYCLOAK_PASSWORD=admin -p 8180:8180 -it quay.io/keycloak/keycloak:14.0.0 -b 0.0.0.0 -Djboss.http.port=8180
 
run rabbit mq
>docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management


