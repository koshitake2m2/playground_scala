## playground scala

### purpose
- To learn scala and library
  - scala2
    - cats
    - circe
    - doobie
    - http4s
  - scala3

### modules
- scala2
- scala3

### Usage

#### docker

```
docker-compose build
docker-compose up -d
docker-compose restart
docker-compose logs -f mysql8

mysql -h 127.0.0.1 -P3310 -u root -ppassword
```

#### sbt

```
sbt scalafmt
sbt scala2/test
sbt scala3/test
```
