mvn install
cd ../web-backend
mvn clean package
docker container rm -f tiger-web-backend-container
docker build -t tiger-web-backend .
docker tag tiger-web-backend:latest chaosopher.com:49153/tiger-web-backend
docker push chaosopher.com:49153/tiger-web-backend

