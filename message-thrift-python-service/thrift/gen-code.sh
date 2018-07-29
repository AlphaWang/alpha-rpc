#!/usr/bin/env bash

thrift --gen py -out ../ message-service.thrift
thrift --gen java -out ../../message-thrift-api-gen/src/main/java message-service.thrift