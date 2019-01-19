package com.hcode.hFarfadet;

import org.eclipse.paho.client.mqttv3.*;

import static java.lang.Thread.sleep;

public class Animation {

    static MqttClient myClient;
    static MqttConnectOptions connOpt;

    static final String BROKER_URL = "tcp://mpd.lan:1883";
    static final String M2MIO_DOMAIN = "laumio";

    public static void main(String[] args) throws MqttException {

        connOpt = new MqttConnectOptions();

        connOpt.setCleanSession(true);
        connOpt.setKeepAliveInterval(30);

        String clientID = "";
        Callback call = new Callback();
        myClient = new MqttClient(BROKER_URL, clientID);
        myClient.setCallback(call);
        myClient.connect(connOpt);


        Animation anim = new Animation();


        anim.animation(myClient, "Laumio_10508F");
    }


    public void sendMsg(MqttTopic topic, String message){

        int pubQoS = 0;
        MqttMessage mqtt_msg = new MqttMessage(message.getBytes());
        mqtt_msg.setQos(pubQoS);
        mqtt_msg.setRetained(false);

        // Publish the message
        System.out.println("Publishing to topic \"" + topic + "\" qos " + pubQoS);
        MqttDeliveryToken token = null;
        try {
            // publish message to broker
            token = topic.publish(mqtt_msg);
            // Wait until the message has been delivered to the broker
            token.waitForCompletion();
            Thread.sleep(100);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * lightOff
     * The main functionnality is to light off a lamp
     */

    public void lightOff(MqttClient client, String lampe){
        String str_topic = M2MIO_DOMAIN + "/" + lampe + "/" + "json";
        String myMessage = "{\'command\' : \'fill\', \'rgb\' :[0,0,0]}";
        MqttTopic topic = client.getTopic(str_topic);
        // Publish the message
        sendMsg(topic, myMessage);
    }

    /**
     *
     * fill
     * The main functionnality is to change the color of the lamp
     *
     * @param client
     * @param lampe
     * @param r
     * @param g
     * @param b
     */

    public void fill(MqttClient client, String lampe, int r, int g, int b){
        String str_topic = M2MIO_DOMAIN + "/" + lampe + "/" + "json";
        String myMessage = "{\'command\' : \'fill\', \'rgb\' :["+r+","+g+","+b+"]}";
        MqttTopic topic = client.getTopic(str_topic);
        // Publish the message
        sendMsg(topic, myMessage);
    }

    /**
     * lightUp
     * The main functionnality is to light up a lamp
     */
    public void lightUp(MqttClient client, String lampe){
        String str_topic = M2MIO_DOMAIN + "/" + lampe + "/" + "json";
        String myMessage = "{\'command\' : \'fill\', \'rgb\' :[123,123,123]}";
        MqttTopic topic = client.getTopic(str_topic);
        // Publish the message
        sendMsg(topic, myMessage);
    }

    /**
     * setRing
     * The main functionnality is to change a ring's color
     */
    public void setRing(MqttClient client, String lampe,int idRing, int r, int g, int b){
        String str_topic = M2MIO_DOMAIN + "/" + lampe + "/" + "json";
        if(idRing<0 || idRing>2)
        {
            idRing = 0;
        }
        String myMessage = "{\'command\' : \'set_ring\', \'led\' : "+idRing +"\'rgb\' :["+r+","+g+","+b+"]}";

        MqttTopic topic = client.getTopic(str_topic);
        // Publish the message
        sendMsg(topic, myMessage);
    }

    /**
     * colorWipe
     * The main functionnality is to start a animation of progressive change of color for a defined time
     */
    public void colorWipe(MqttClient client, String lampe, int r, int g, int b,int duration){
        String str_topic = M2MIO_DOMAIN + "/" + lampe + "/" + "json";
        String myMessage = "{\'command\' : \'set_ring\', \'duration\' : "+duration +"\'rgb\' :["+r+","+g+","+b+"]}";
        MqttTopic topic = client.getTopic(str_topic);
        // Publish the message
        sendMsg(topic, myMessage);
    }


    /**
     * setPixel
     * The main functionnality is to set a pixel of a lamp
     */
    public void setPixel(MqttClient client, String lampe, int pixelId, int r, int g, int b){
        String str_topic = M2MIO_DOMAIN + "/" + lampe + "/" + "json";
        String myMessage = "{\'command\' : \'set_pixel\', \'led\':"+pixelId+",\'rgb\' :["+r+","+g+","+b+"]}";
        MqttTopic topic = client.getTopic(str_topic);
        // Publish the message
        sendMsg(topic, myMessage);
    }

    /**
     * setColumn
     * The main functionnality is to set a ring of a lamp
     */
    public void setColumn(MqttClient client, String lampe, int ringId, int r, int g, int b){
        String str_topic = M2MIO_DOMAIN + "/" + lampe + "/" + "json";
        String myMessage = "{\'command\' : \'set_column\', \'ring\':"+ringId+",\'rgb\' :["+r+","+g+","+b+"]}";
        MqttTopic topic = client.getTopic(str_topic);
        // Publish the message
        sendMsg(topic, myMessage);
    }

    /**
     * animateRainbow
     * The main functionnality is to set a raimbow animation
     */
    public void animateRainbow(MqttClient client, String lampe){
        String str_topic = M2MIO_DOMAIN + "/" + lampe + "/" + "json";
        String myMessage = "{\'command\' : \'animate_rainbow\'}";
        MqttTopic topic = client.getTopic(str_topic);
        // Publish the message
        sendMsg(topic, myMessage);
    }

    public void animation(MqttClient client, String lampe)
    {


        try {
            this.lightOff(client, lampe);
            sleep(1000);
            this.fill(client, lampe, 255, 0,0);
            sleep(1000);
            this.fill(client, lampe, 0, 255,0);
            sleep(1000);
            this.fill(client, lampe, 0, 0,255);
            sleep(1000);
            this.animateRainbow(client, lampe);
            sleep(1000);
            this.setRing(client, lampe, 0,255,255, 255);
            sleep(1000);
            this.setRing(client, lampe, 1,255,255, 255);
            sleep(1000);
            this.setRing(client, lampe, 2,255,255, 255);
            sleep(1000);
            this.setColumn(client, lampe, 0, 255, 255, 255);
            sleep(1000);
            this.setColumn(client, lampe, 1, 255, 255, 255);
            sleep(1000);
            this.setColumn(client, lampe, 2, 255, 255, 255);
            sleep(1000);
            this.setColumn(client, lampe, 3, 255, 255, 255);
            sleep(1000);
            this.animateRainbow(client, lampe);
            sleep(1000);
            this.fill(client, lampe, 255, 0,0);
            sleep(1000);
            this.fill(client, lampe, 0, 255,0);
            sleep(1000);
            this.fill(client, lampe, 0, 0,255);
            sleep(1000);
            this.lightOff(client, lampe);
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

}
