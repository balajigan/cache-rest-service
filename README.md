# cache-rest-service

## Cassandra-cache-service

curl -X GET http://localhost:8080/cassandra/order/get?key=3000


## start the springboot with the following 

java -Djava.security.egd=file:/dev/./urandom -jar target/cassandra-cache-service-1.0.0.jar --logging.file=output.log


Other option:

Open $JAVA_HOME/jre/lib/security/java.security 
Find the line: securerandom.source=file:/dev/random
Change to: securerandom.source=file:/dev/urandom


## to upload file

curl -F 'file=@/home/user/test.txt' http://localhost:8080/upload

