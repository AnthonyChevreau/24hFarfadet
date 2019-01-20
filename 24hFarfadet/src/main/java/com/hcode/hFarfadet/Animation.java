package com.hcode.hFarfadet;

import org.eclipse.paho.client.mqttv3.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
        List<Integer> mesActions = new ArrayList<Integer>();

        System.out.println("************ LAUMIOS CONSOLE APPLICATION *************");
        System.out.println("***      Group: Les Farfadets Pimpants             ***");
        System.out.println("***      Author: P. Le Berre                       ***");
        System.out.println("***              R. Bansard                        ***");
        System.out.println("***              D. Mauget                         ***");
        System.out.println("***              A. Chevreau                       ***");
        System.out.println("***              C. Talarmin                       ***");
        System.out.println("******************************************************");

        System.out.println("Syntaxe :");
        System.out.println(" - SELECT [(lampes_id) | (lampes_list)]");
        System.out.println(" - LAUNCH [(action_id) | (actions_list)]");
        System.out.println(" - DETECT [(category)] [(sensor)]");
        System.out.println(" - QUIT");

        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        String cmd = "";
        String[] arguments;

        while(!(cmd = bf.readLine()).equals("QUIT")) {
            arguments = cmd.split(" ");
            switch (arguments[0]) {
                case "SELECT" :
                    if(arguments.length > 1) {
                        //Clear lights recorded
                        mesLampes.clear();
                        //Add selected light(s)
                        for(int i=1; i<arguments.length; i++) {
                            mesLampes.add(arguments[i]);
                        }
                        //Inform user
                        System.out.println("Ligths succefully saved");
                    }
                    else {
                        System.out.println("Error : missing arguments (id_lampes)...");
                    }
                    break;

                case "LAUNCH" :
                    if(arguments.length > 1) {
                        //Add selected action(s)
                        for(int i=1; i<arguments.length; i++) {
                            mesActions.add(Integer.parseInt(arguments[i]));
                        }
                        anim.playActions(myClient, mesLampes, mesActions);
                        //Clear actions recorded
                        mesActions.clear();
                        //Inform user
                        System.out.println("Animation terminated.");
                    }
                    else {
                        System.out.println("Error : missing arguments (id_actions)...");
                    }
                    break;

                case "DETECT" :
                    if(arguments.length > 1) {
                        String str_topic = "atmosphere/temperature";
                        // subscribe to topic
                        try {
                            myClient.subscribe(str_topic);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //Inform user
                        System.out.println("Start to get temperature data.");

                    }
                    else {
                        System.out.println("Error : missing arguments (id_actions)");
                    }
                    break;

                default: System.out.println("Error : syntax error...");
            }

        }
        System.out.println("End of application.");
        System.exit(0);

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

    public void playActions(MqttClient myClient, List<String> lampes, List<Integer> actions) throws Exception{

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

    public void playActions2(MqttClient myClient, List<String> lampes, List<Integer> actions) throws Exception{

        for(Integer i: actions) {
            for(String l: lampes){
                switch(i){
                    case 0: this.lightOff(myClient, l);
                        break;
                    case 1: this.fill(myClient, l, 255, 0, 0);
                        break;
                    case 2: this.fill(myClient, l, 255, 255, 255);
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
