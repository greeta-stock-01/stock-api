docker-compose -f docker-app-compose.yml down

mvn clean install -DskipTests

cd ./user-service
mvn spring-boot:build-image -DskipTests \
  -Dspring-boot.build-image.imageName=user-service

cd ../product-service
mvn spring-boot:build-image -DskipTests \
  -Dspring-boot.build-image.imageName=product-service

cd ../order-service
mvn spring-boot:build-image -DskipTests \
  -Dspring-boot.build-image.imageName=order-service

cd ../gateway-service
mvn spring-boot:build-image -DskipTests \
  -Dspring-boot.build-image.imageName=gateway-service

cd ../

docker-compose -f docker-app-compose.yml up -d