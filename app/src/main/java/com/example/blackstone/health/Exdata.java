package com.example.blackstone.health;

public class Exdata {

    String exName;
    double upThreshold, downThreshold;
    String angleName[];

    Exdata(String name){
        exName = name;

        switch(exName){
            case "squat" :
                upThreshold = 160f;
                downThreshold = 70f;
                angleName = new String[]{"RightKnee", "LeftKnee"};
                break;
            case "pushUp" :
                upThreshold = 150f;
                downThreshold = 90f;
                angleName = new String[]{"RightElbow", "LeftElbow"};
                break;
            case "plank" :
                upThreshold = 160f;
                downThreshold = 90f;
                angleName = new String[]{"RightElbow", "LeftElbow"};
        }
    }
}
