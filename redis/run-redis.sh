#!/usr/bin/env bash
docker stop alpha-rpc-redis
docker rm alpha-rpc-redis
docker run --name alpha-rpc-redis -idt -p 6379:6379 -v `pwd`/data:/data -v `pwd`/conf/redis.conf:/etc/redis/redis_default.conf redis:latest