FROM node:20-alpine3.18

WORKDIR /app

COPY package*.json ./

RUN npm install

EXPOSE 4200

ENTRYPOINT ["npm", "run", "start-dockerdev"]
