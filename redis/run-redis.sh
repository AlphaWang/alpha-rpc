#!/usr/bin/env bash
docker stop redis
docker rm redis
docker run -idt -p6379:6379 -v `pwd`/data:/data  --name redis -v `pwd`/conf/redis.conf:/etc/redis/redis_default.conf hub.c.163.com/public/redis:2.8.4