package it.polimi.ingsw.model;

public class GetMapFactory {

    public Map getMap(int n){
        if(n == 0)
            return null;


        if (n == 1)
            return new Map1();
        else if (n == 2)
            return new Map2();
        else if(n == 3)
            return new Map3();
        else if (n == 4)
            return new Map4();


        return null;


    }




}
