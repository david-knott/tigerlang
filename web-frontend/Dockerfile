### STAGE 1: Build ###
FROM node:13.3.0 AS compile-image
WORKDIR /usr/src/app
COPY package.json ./
RUN npm install
COPY . .
RUN npm run build --configuration=production

### STAGE 2: Run ###
FROM nginx:1.17.1-alpine
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=compile-image /usr/src/app/dist/ace-tiger-ui /usr/share/nginx/html
EXPOSE 80
