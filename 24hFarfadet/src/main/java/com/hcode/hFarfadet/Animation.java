package com.hcode.hFarfadet;

import org.eclipse.paho.client.mqttv3.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class Animation {

    static MqttClient myClient;
    static MqttConnectOptions connOpt;

    static final String BROKER_URL = "tcp://mpd.lan:1883";
    static final String M2MIO_DOMAIN = "laumio";

    public static void main(String[] args) throws MqttException, Exception {

        connOpt = new MqttConnectOptions();
        connOpt.setCleanSession(true);
        connOpt.setKeepAliveInterval(30);

        String clientID = "";
        Callback call = new Callback();

        myClient = new MqttClient(BROKER_URL, clientID);
        myClient.setCallback(call);
        myClient.connect(connOpt);

        Animation anim = new Animation();

        List<String> mesLampes = new ArrayList<String>();
        mesLampes.add("Laumio_0FC168");
        mesLampes.add("Laumio_107DA8");
        //mesLampes.add("Laumio_104A13");
        mesLampes.add("Laumio_CD0522");

        anim.playActions(myClient, mesLampes);

    }

    /**
     * lightOff
     * The main functionnality is to light off a lamp
     */

    public void lightOff(MqttClient client, String lampe) throws Exception{
        String str_topic = M2MIO_DOMAIN + "/" + lampe + "/" + "json";
        String myMessage = "{\'command\' : \'fill\', \'rgb\' :[0,0,0]}";
        client.publish(str_topic, new MqttMessage(myMessage.getBytes()));
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

    public void fill(MqttClient client, String lampe, int r, int g, int b) throws Exception{
        String str_topic = M2MIO_DOMAIN + "/" + lampe + "/" + "json";
        String myMessage = "{\'command\' : \'fill\', \'rgb\' :["+r+","+g+","+b+"]}";
        client.publish(str_topic, new MqttMessage(myMessage.getBytes()));
    }

    public void temperature(MqttClient client, String lampe) throws Exception{
        String str_topic = "atmosphere/temperature";
        client.publish(str_topic, new MqttMessage());
    }

    /**
     * lightUp
     * The main functionnality is to light up a lamp
     */
    public void lightUp(MqttClient client, String lampe) throws Exception{
        String str_topic = M2MIO_DOMAIN + "/" + lampe + "/" + "json";
        String myMessage = "{\'command\' : \'fill\', \'rgb\' :[255,255,255]}";
        client.publish(str_topic, new MqttMessage(myMessage.getBytes()));
    }

    /**
     * setRing
     * The main functionnality is to change a ring's color
     */
    public void setRing(MqttClient client, String lampe,int idRing, int r, int g, int b) throws Exception{
        String str_topic = M2MIO_DOMAIN + "/" + lampe + "/" + "json";
        if(idRing<0 || idRing>2)
        {
            idRing = 0;
        }
        String myMessage = "{\'command\' : \'set_ring\', \'led\' : "+idRing +"\'rgb\' :["+r+","+g+","+b+"]}";
        client.publish(str_topic, new MqttMessage(myMessage.getBytes()));
    }

    /**
     * colorWipe
     * The main functionnality is to start a animation of progressive change of color for a defined time
     */
    public void colorWipe(MqttClient client, String lampe, int r, int g, int b,int duration) throws  Exception{
        String str_topic = M2MIO_DOMAIN + "/" + lampe + "/" + "json";
        String myMessage = "{\'command\' : \'set_ring\', \'duration\' : "+duration +"\'rgb\' :["+r+","+g+","+b+"]}";
        client.publish(str_topic, new MqttMessage(myMessage.getBytes()));
    }

    /**
     * setPixel
     * The main functionnality is to set a pixel of a lamp
     */
    public void setPixel(MqttClient client, String lampe, int pixelId, int r, int g, int b) throws Exception {
        String str_topic = M2MIO_DOMAIN + "/" + lampe + "/" + "json";
        String myMessage = "{\'command\' : \'set_pixel\', \'led\':"+pixelId+",\'rgb\' :["+r+","+g+","+b+"]}";
        client.publish(str_topic, new MqttMessage(myMessage.getBytes()));
    }

    /**
     * setColumn
     * The main functionnality is to set a ring of a lamp
     */
    public void setColumn(MqttClient client, String lampe, int ringId, int r, int g, int b) throws Exception{
        String str_topic = M2MIO_DOMAIN + "/" + lampe + "/" + "json";
        String myMessage = "{\'command\' : \'set_column\', \'ring\':"+ringId+",\'rgb\' :["+r+","+g+","+b+"]}";
        client.publish(str_topic, new MqttMessage(myMessage.getBytes()));
    }

    /**
     * animateRainbow
     * The main functionnality is to set a raimbow animation
     */
    public void animateRainbow(MqttClient client, String lampe) throws Exception{
        String str_topic = M2MIO_DOMAIN + "/" + lampe + "/" + "json";
        String myMessage = "{\'command\' : \'animate_rainbow\'}";
        client.publish(str_topic, new MqttMessage(myMessage.getBytes()));
    }

    public void playActions(MqttClient myClient, List<String> lampes) throws Exception{

        List<Integer> actions = new ArrayList<Integer>();
        Scanner sc = new Scanner(System.in);

        System.out.println("Veuillez saisir une action à réaliser : ");
        System.out.println("0 : éteindre la lampe");
        System.out.println("1 : choisir la couleur rouge");
        System.out.println("2 : allumer la couleur verte");
        System.out.println("3 : allumer la couleur bleue");
        System.out.println("4 : effet arc-en-ciel");
        System.out.println("5 : allumer la lampe");
        System.out.println("9 : fin de l\'animation");
        int choix = 0;
        while (choix != 9) {
            choix = sc.nextInt();
            if (choix != 9) {
                actions.add(choix);
            }
        }

        for(Integer i: actions) {
            for(String l: lampes){
                switch(i){
                    case 0: this.lightOff(myClient, l);
                        break;
                    case 1: this.fill(myClient, l, 255, 0, 0);
                        break;
                    case 2: this.fill(myClient, l, 0, 255, 0);
                        break;
                    case 3: this.fill(myClient, l, 0, 0,255);
                        break;
                    case 4: this.animateRainbow(myClient, l);
                        break;
                    case 5: this.lightUp(myClient, l);
                        break;
                }
            }
            sleep(1000);
        }
    }


}
