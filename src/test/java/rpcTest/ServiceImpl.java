package rpcTest;

public class ServiceImpl implements Service {
    @Override
    public Double add(Integer num1, Double num2) {
        return num1 + num2;
    }
}
