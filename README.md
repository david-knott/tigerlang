# tigerlang

## Deploy compiler web application

```
cd tigerlang
mvn install
cd web-backend
mvn clean package
docker image rm tiger-web-backend
docker build -t tiger-web-backend .
docker container rm -f tiger-web-backend-container
docker container rm -f tiger-web-frontend-container
docker run --rm -d --name tiger-backend-container  -p 10000:8080 tiger-web-backend
cd ../web-frontend
docker image rm tiger-front-end:latest
docker build -t tiger-front-end .
docker run --rm -d --name tiger-frontend-container  -p 4200:80 tiger-front-end
```


## Diary

Getting options to work for angular app.
