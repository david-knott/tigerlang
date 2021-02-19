mvn install
cd web-backend
mvn clean package
docker container rm -f tiger-web-backend-container
docker build -t tiger-web-backend .
docker run --rm -d --name tiger-web-backend-container  -p 8080:8080 tiger-web-backend

cd ../web-frontend
docker container rm -f tiger-web-frontend-container
docker build -t tiger-web-frontend .
docker run --rm -d --name tiger-web-frontend-container  -p 4200:80 tiger-web-frontend

docker tag tiger-web-frontend:latest chaosopher.com:49153/tiger-web-frontend
docker tag tiger-web-backend:latest chaosopher.com:49153/tiger-web-backend
docker push chaosopher.com:49153/tiger-web-backend
docker push chaosopher.com:49153/tiger-web-frontend

