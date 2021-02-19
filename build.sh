mvn install
cd web-backend
mvn clean package
docker container rm -f tiger-web-backend-container
docker build -t tiger-web-backend .
docker run --rm -d --name tiger-web-backend-container  -p 8080:8080 tiger-web-backend

cd ../web-frontend
docker container rm -f tiger-web-frontend-container
docker build -t tiger-front-end .
docker run --rm -d --name tiger-web-frontend-container  -p 4200:80 tiger-front-end

docker tag tiger-frontend:latest 46.22.128.219:49153/my-tiger-web-frontend
docker tag tiger-front-end:latest 46.22.128.219:49153/my-tiger-web-frontend
docker push 46.22.128.219:49153/my-tiger-web-backend
docker push  46.22.128.219:49153/my-tiger-web-frontend

