import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttPublishSample {

    public static void main(String[] args) {

        String topic        = "------序列号-----";
        String CONTROL_MARK = "ctr";
        String RECV_MARK    = "state";
        String content      = "----命令-------如：setr=2222222222";
        int qos             = 0;
        String broker       = "tcp://api.netrelay.cn:1883";
        String appsecret     = "----appsecret----";
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            MqttClient sampleClient = new MqttClient(broker, appsecret, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            sampleClient.connect(connOpts);
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            sampleClient.publish(topic+CONTROL_MARK, message);

            // 订阅
            sampleClient.subscribe(topic+RECV_MARK);
            // 设置回调
            sampleClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable throwable) {

                }

                @Override
                public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                    System.out.println("Topic: " + s);
                    System.out.println(" Message: " + mqttMessage.toString());

                    //断开连接根据实际决定是否需要
                    sampleClient.disconnect();
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

                }
            });
        } catch(MqttException me) {
            me.printStackTrace();
        }
    }
}