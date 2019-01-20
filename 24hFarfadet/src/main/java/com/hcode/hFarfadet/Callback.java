package com.hcode.hFarfadet;

import org.eclipse.paho.client.mqttv3.*;

public class Callback implements MqttCallback {


    @Override
    public void connectionLost(Throwable t) {
        System.out.println("Connection lost!");
        // code to reconnect to the broker would go here if desired
    }

    /**
     *
     * deliveryComplete
     * This callback is invoked when a message published by this client
     * is successfully received by the broker.
     *
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        //System.out.println("Pub complete" + new String(token.getMessage().getPayload()));
    }



    /**
     *
     * messageArrived
     * This callback is invoked when a message is received on a subscribed topic.
     *
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) {
        try {/*
        System.out.println("-------------------------------------------------");
        System.out.println("| Topic:" + topic);
        System.out.println("| Message: " + new String(message.getPayload()));
        System.out.println("-------------------------------------------------");*/

        if(topic.equals("atmosphere/temperature")){
            //Get temperature : [0] units, [1] decimals
            String temperature = (new String(message.getPayload()));
            int degree = Integer.parseInt(temperature.charAt(0)+"")*10 + Integer.parseInt(temperature.charAt(1)+"");
            Animation a = new Animation();
            if(degree<20) {
                //Laumio_439BA9 : reserved for show temperature, "ice" color
                a.fill(Animation.myClient, "Laumio_1D9486", 111, 122, 159);
            }
            else if(degree<25){
                //Laumio_439BA9 : reserved for show temperature, "normal" color
                a.fill(Animation.myClient, "Laumio_1D9486", 255, 197, 143);
            }
            else {
                //Laumio_439BA9 : reserved for show temperature, "warm" color
                a.fill(Animation.myClient, "Laumio_1D9486", 255,0,0);//247, 64, 58);
            }
            System.out.println("[INFO] temperature updated ("+ temperature +").");

        }
        else System.out.println("Unhandled topic");} catch (Exception e) {
            e.printStackTrace();
        }
    }





}
