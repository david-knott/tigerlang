cd ./web-frontend
docker image rm tiger-web-frontend
docker build -t tiger-web-frontend .
docker tag tiger-web-frontend:latest chaosopher.com:49153/tiger-web-frontend
docker push chaosopher.com:49153/tiger-web-frontend

