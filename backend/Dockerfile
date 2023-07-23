FROM golang:1.20

WORKDIR /usr/src/app

COPY go.mod go.sum ./
RUN go mod download && go mod verify

COPY . .
RUN go run build -v -o /usr/local/bin/app ./...

CMD [ "app" ]