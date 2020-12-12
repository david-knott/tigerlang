# tigerlang

## Deploy compiler web application

```
cd tigerlang
mvn install
cd web-backend
mvn clean package
docker image rm tiger-web-backend
docker build -t tiger-web-backend .
docker run --rm -p -d 10000:8080 tiger-web-backend
cd ../web-frontend
docker image rm tiger-front-end:latest
docker build -t tiger-front-end .
docker run --rm -d --name tiger-front-end-container  -p 4200:80 tiger-front-end
```
