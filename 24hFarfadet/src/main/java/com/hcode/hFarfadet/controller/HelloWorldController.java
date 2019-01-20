package com.hcode.hFarfadet.controller;

import com.hcode.hFarfadet.Animation;
import com.hcode.hFarfadet.Callback;
import com.hcode.hFarfadet.Model.Lampe;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HelloWorldController {

    static MqttClient myClient;
    static MqttConnectOptions connOpt;

    static final String BROKER_URL = "tcp://mpd.lan:1883";
    static final String M2MIO_DOMAIN = "laumio";

    /*@GetMapping("/testihm")
    public String testIhm(@RequestParam(name = "nameGET", required = false, defaultValue = "World")
                                   String nameGET,Model model) {
        model.addAttribute("nomTemplate", nameGET);
        return "testihm";
    }*/

    @RequestMapping(value = "/testihm", method = {RequestMethod.POST, RequestMethod.GET})
    public String testIhmSubmit(@RequestParam(name="nom", required = false, defaultValue = "none") String nom, Lampe lampe){
        lampe.setNom(nom);
        System.out.println(nom);
        return "testihm";
    }
    @RequestMapping(value = "/testihm2", method = {RequestMethod.POST, RequestMethod.GET})
    public String testIhm2Submit(@RequestParam(name="nom", required = false, defaultValue = "none") String nom,
                                @RequestParam(name="blue", required = false, defaultValue = "255") String blue,
                                @RequestParam(name="green", required = false, defaultValue = "255") String green,
                                @RequestParam(name="red", required = false, defaultValue = "255") String red,
                                Lampe lampe) throws Exception {
        int rouge = Integer.parseInt(red);
        int bleu = Integer.parseInt(blue);
        int vert = Integer.parseInt(green);

        lampe.setNom(nom);
        lampe.setRed(rouge);
        lampe.setBlue(bleu);
        lampe.setGreen(vert);
        System.out.println(lampe.getNom());
        System.out.println(lampe.getRed());
        System.out.println(lampe.getBlue());
        System.out.println(lampe.getGreen());


        connOpt = new MqttConnectOptions();
        connOpt.setCleanSession(true);
        connOpt.setKeepAliveInterval(30);

        String clientID = "";
        Callback call = new Callback();

        Animation anim = new Animation();

        myClient = new MqttClient(BROKER_URL, clientID);
        myClient.setCallback(call);
        myClient.connect(connOpt);

        List<String> mesLampes = new ArrayList<String>();
        mesLampes.add(lampe.getNom());


        anim.fill(myClient,lampe.getNom(),lampe.getRed(),lampe.getGreen(),lampe.getBlue());

        return "testihm2";
    }
    @RequestMapping(value = "/test-ihm2", method = {RequestMethod.POST, RequestMethod.GET})
    public String testIhm3Submit(@RequestParam(name="nom", required = false, defaultValue = "none") String nom, Lampe lampe) throws MqttException {


        lampe.setNom(nom);

        System.out.println(lampe.getNom());



        connOpt = new MqttConnectOptions();
        connOpt.setCleanSession(true);
        connOpt.setKeepAliveInterval(30);

        String clientID = "";
        Callback call = new Callback();

        Animation anim = new Animation();

        myClient = new MqttClient(BROKER_URL, clientID);
        myClient.setCallback(call);
        myClient.connect(connOpt);

        List<String> mesLampes = new ArrayList<String>();
        mesLampes.add(lampe.getNom());
        try {
            anim.playActions(myClient, mesLampes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "testihm2";
    }

}
