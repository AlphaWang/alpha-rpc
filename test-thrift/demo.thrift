namespace java com.alphawang.thrift.demo
namespace py thrift.demo

service DemoService {
    void sayHello(1:string name);
}