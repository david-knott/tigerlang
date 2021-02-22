mvn install
cd web-backend
mvn clean package
docker container rm -f tiger-web-backend-container
docker build -t tiger-web-backend .
cd ../web-frontend
docker container rm -f tiger-web-frontend-container
docker build -t tiger-web-frontend .
docker tag tiger-web-frontend:latest chaosopher.com:49153/tiger-web-frontend
docker tag tiger-web-backend:latest chaosopher.com:49153/tiger-web-backend
docker push chaosopher.com:49153/tiger-web-backend
docker push chaosopher.com:49153/tiger-web-frontend
ssh hosting@chaosopher.com "~/hosting_tigerlang.sh"
