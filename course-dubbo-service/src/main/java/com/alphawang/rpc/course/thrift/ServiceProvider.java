package com.alphawang.rpc.course.thrift;

import com.alphawang.rpc.thrift.user.service.api.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ServiceProvider {
    
    @Value("${thrift.user.ip}")
    private String userServiceIp;
    
    @Value("${thrift.user.port}")
    private int userServicePort;
    
    
    public UserService.Client getUserService() {
        return getService(userServiceIp, userServicePort, ServiceType.USER);
    }
    
    private enum ServiceType {
        USER,
        MESSAGE
    }
    
    private <T> T getService(String ip, int port, ServiceType serviceType) {
        TSocket socket = new TSocket(ip, port, 3000);
        TTransport transport = new TFramedTransport(socket);

        try {
            transport.open();
        } catch (TTransportException e) {
            log.error("Failed to open TTransport", e);
            return null;
        }
        /**
         * 需要与服务端相同的配置
         */
        TProtocol protocol = new TBinaryProtocol(transport);
        TServiceClient serviceClient = null;
        switch (serviceType) {
            case USER:
                serviceClient = new UserService.Client(protocol);
                break;
        }
        return (T) serviceClient;
    }
}
