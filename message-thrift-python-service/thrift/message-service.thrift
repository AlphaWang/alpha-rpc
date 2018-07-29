namespace java com.alphawang.rpc.thrift.message.service
namespace py message.service.api

service MessageService {
    bool sendMobileMessage(1:string mobile, 2:string message);
    bool sendEmailMessage(1:string email, 2:string message);
}