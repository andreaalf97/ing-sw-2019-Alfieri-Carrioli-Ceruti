package it.polimi.ingsw.View;

public class Response {

    public int responseInt;
    public String responseString;

    public Response(int responseInt){
        this.responseInt = responseInt;
        this.responseString = null;
    }

    public Response(String responseString){
        this.responseString= responseString;
        this.responseInt = -1;
    }
}
