FROM golang:1.20-alpine
WORKDIR /app
COPY go.mod go.sum ./
RUN go mod download && go mod verify
COPY . .
RUN go build -v -o /app/ main.go

FROM alpine
COPY --from=0 /app/ /app/
CMD [ "/app/main" ]