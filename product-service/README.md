>curl -X POST   http://localhost:8080/api/product  -H "Content-Type: application/json"    -d '{"name":"Elba Oven","description":"Convection oven","price":85000}'

>curl  http://localhost:8080/api/product

>sudo systemctl start mongod


to refresh
>curl -X POST  http://localhost:35171/actuator/refresh
> curl -X POST http://localhost:37703/actuator/busrefresh

if we want to make changes in many microservices this won't scale
therefore we can make use of spring-cloud-bus-*/amqp to broadcast git changes
and each micro service to config to listen

http://localhost:<port>/actuator/busrefresh