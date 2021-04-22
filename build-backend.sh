mvn install
cd ./web-backend
mvn clean package
docker container rm -f tiger-web-backend-container
docker build -t tiger-web-backend .
docker tag tiger-web-backend:latest tigerlang.chaosopher.com:49153/tiger-web-backend
docker push tigerlang.chaosopher.com:49153/tiger-web-backend
ssh communicraft@tigerlang.chaosopher.com "~/hosting_tigerlang.sh"
