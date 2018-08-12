#!/usr/bin/env bash
docker stop alpha-rpc-zk
docker rm alpha-rpc-zk
docker run --name alpha-rpc-zk -p2181:2181 --restart always -d zookeeper:3.5