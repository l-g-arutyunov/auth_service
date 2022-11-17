mvn "-DDATASOURCE_HOST=db.bromen.fun" "-DDATASOURCE_PORT=5432" clean package
docker build . -t devlifestartup/auth_service:1.0.0
docker push devlifestartup/auth_service:1.0.0