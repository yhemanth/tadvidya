FROM node:10.16.1-alpine
WORKDIR /tv-web
COPY build build/
RUN npm install -g serve
EXPOSE 5000
CMD ["serve", "-s", "build"]
