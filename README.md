# bing image
get & downloading bing images, also set cron for auto download to you folder.

docker run shell

```shell script
docker run --restart=always -d -p 1000:1000 -v {user.home}/pictures/bingimages/images:/usr/local/bing/images -v {user.home}/pictures/bingimages/logs/:/usr/local/bing/logs/ -m 200M --name bingimage bingimage:1.0
```

default cron : image.auto.cron=0 0 11 * * ? 

* Dockerfile 中使用的是官方的 maven 源，如需使用阿里的 maven 仓库，在 Dockerfile 中将阿里的 maven 仓库复制到 MAVEN_BUILD 中即可
