package it.polimi.ingsw.client.cli;

public class MapPrinting {

    private static final int maxHorizontalLength = 55;
    private  static final int maxVerticalLength = 21;
    private static String[][] mapFire = new String[maxVerticalLength][maxHorizontalLength];

    private static final String ansiBlue = "\u001b[34m";
    private static final String ansiRed = "\u001b[31m";
    private static final String ansiYellow = "\u001b[33m";
    private static final String ansiReset = "\u001b[0m";
    private static final String ansiWhite = "\u001b[37m";

    //stupid example
    private static final String oldMap =
            "╔══════════════════════════════════════╗\n" +
            "║" + "\u001b[44m" + "            │            │            " + "\u001b[0m" + "║\n" +
            "║" + "\u001b[44m" + "            │            │            " + "\u001b[0m" + "║\n" +
            "║" + "\u001b[44m" + "            │            │            " + "\u001b[0m" + "║\n" +
            "║" + "\u001b[44m" + "            │            │            " + "\u001b[0m" + "║\n" +
            "║" + "\u001b[44m" + "            │            │            " + "\u001b[0m" + "║\n" +
            "╠════  ═════════════════════════  ═════╬════════════╗\n" +
            "║" + "\u001b[41m" + "            │            │            " + "\u001b[0m" + "║" + "\u001b[43m" + "            " + "\u001b[0m" + "║\n" +
            "║" + "\u001b[41m" + "            │            │            " + "\u001b[0m" + "║" + "\u001b[43m" + "            " + "\u001b[0m" + "║\n" +
            "║" + "\u001b[41m" + "            │            │            " + "\u001b[0m" + " " + "\u001b[43m" + "            " + "\u001b[0m" + "║\n" +
            "║" + "\u001b[41m" + "            │            │            " + "\u001b[0m" + "║" + "\u001b[43m" + "            " + "\u001b[0m" + "║\n" +
            "║" + "\u001b[41m" + "            │            │            " + "\u001b[0m" + "║" + "\u001b[43m" + "            " + "\u001b[0m" + "║\n" +
            "╚════════════╦═════  ══════════════════╣" + "\u001b[43m" + "────────────" + "\u001b[0m" + "║\n" +
            "\t\t\t ║" + "\u001b[47m" +"            │            " + "\u001b[0m" + "║" + "\u001b[43m" + "            " + "\u001b[0m" + "║\n" +
            "\t\t\t ║" + "\u001b[47m" +"            │            " + "\u001b[0m" + "║" + "\u001b[43m" + "            " + "\u001b[0m" + "║\n" +
            "\t\t\t ║" + "\u001b[47m" +"            │            " + "\u001b[0m" + "║" + "\u001b[43m" + "            " + "\u001b[0m" + "║\n" +
            "\t\t\t ║" + "\u001b[47m" +"            │            " + "\u001b[0m" + "║" + "\u001b[43m" + "            " + "\u001b[0m" + "║\n" +
            "\t\t\t ║" + "\u001b[47m" +"            │            " + "\u001b[0m" + "║" + "\u001b[43m" + "            " + "\u001b[0m" + "║\n" +
            "\t\t\t ╚═════════════════════════╩════════════╝\n";


    public static void fillMapFire(){

        mapFire[0][0] = ansiBlue + "╔" + ansiReset;

        for(int c = 1; c < maxHorizontalLength - 14; c++){
            mapFire[0][c] = ansiBlue + "═" + ansiReset;
        }

        mapFire[0][maxHorizontalLength - 14] = ansiBlue + "╗" + ansiReset;

        for(int c = maxHorizontalLength - 13; c < maxHorizontalLength; c++){
            mapFire[0][c] = "";
        }  //first line filled

        for(int r = 1 ; r < 6; r++){

            mapFire[r][0] = ansiBlue + "║" + ansiReset;

            for(int c = 1 ; c < maxHorizontalLength - 14; c++){

                if(c == 13 || c == 26){
                    mapFire[r][c] = ansiBlue + "│" + ansiReset;
                }
                else {
                    mapFire[r][c] = " ";
                }

            }

            mapFire[r][maxHorizontalLength - 14] = ansiBlue + "║" + ansiReset;

            for(int c = maxHorizontalLength - 13; c < maxHorizontalLength; c++)
                mapFire[r][c] = "";
        }
        //first row with all columns

        mapFire[6][0] =ansiBlue + "╚" + ansiReset;

        for(int c = 1; c < maxHorizontalLength - 14; c++) {
            if(c == 6 || c == 35){
                mapFire[6][c] = ansiBlue + "╕" + ansiReset;
            }
            else if (c == 7 || c == 36){
                mapFire[6][c] = " ";
            }
            else if(c == 8 || c == 37){
                mapFire[6][c] = ansiBlue + "╒" + ansiReset;
            }
            else{
                mapFire[6][c] = ansiBlue + "═" + ansiReset;
            }
        }

        mapFire[6][maxHorizontalLength - 14] = ansiBlue + "╝" + ansiReset;

        for(int c = maxHorizontalLength - 13; c < maxHorizontalLength; c++)
            mapFire[6][c] = "";

        //finish blue room


        mapFire[7][0] = ansiRed + "╔" + ansiReset;

        for(int c = 1; c < maxHorizontalLength - 14; c++){
            if(c == 6 || c == 35){
                mapFire[7][c] = ansiRed + "╛" + ansiReset;
            }
            else if( c == 7 || c == 36){
                mapFire[7][c] = " ";
            }
            else if (c == 8 || c == 37){
                mapFire[7][c] = ansiRed + "╘" + ansiReset;
            }
            else{
                mapFire[7][c] = ansiRed + "═" + ansiReset;
            }
        }

        mapFire[7][maxHorizontalLength - 14] = ansiRed + "╗" + ansiReset;
        mapFire[7][maxHorizontalLength - 13] = ansiYellow + "╔" + ansiReset;

        for ( int c = maxHorizontalLength - 12; c < maxHorizontalLength - 1; c++){
            mapFire[7][c] = ansiYellow + "═" + ansiReset;
        }
        mapFire[7][maxHorizontalLength - 1] = ansiYellow + "╗" + ansiReset;

        for(int r = 8 ; r < 13 ; r++ ){

            mapFire[r][0] = ansiRed + "║" + ansiReset;

            for(int c = 1; c < maxHorizontalLength - 14; c++){
                if (c == 13 || c == 26){
                    mapFire[r][c] = ansiRed + "│" + ansiReset;
                }
                else{
                    mapFire[r][c] = " ";
                }
            }

            if( r == 9) {
                mapFire[r][maxHorizontalLength - 14] = ansiRed + "╙" + ansiReset;
                mapFire[r][maxHorizontalLength - 13] = ansiYellow + "╜" + ansiReset;
            }
            else if(r == 10){
                mapFire[r][maxHorizontalLength - 14] = ansiRed + "╓" + ansiReset;
                mapFire[r][maxHorizontalLength - 13] = ansiYellow + "╖" + ansiReset;
            }
            else{
                mapFire[r][maxHorizontalLength - 14] = ansiRed + "║" + ansiReset;
                mapFire[r][maxHorizontalLength - 13] = ansiYellow + "║" + ansiReset;
            }

            for(int c = maxHorizontalLength - 12; c < maxHorizontalLength - 1; c++)
                mapFire[r][c] = " ";

            mapFire[r][maxHorizontalLength - 1] = ansiYellow + "║" + ansiReset;

        }

        mapFire[13][0] = ansiRed + "╚" + ansiReset;
        for(int c = 1 ; c < maxHorizontalLength - 14; c++){
            if( c == 15){
                mapFire[13][c] = ansiRed + "╕" + ansiReset;
            }
            else if( c == 16){
                mapFire[13][c] = " ";
            }
            else if (c == 17){
                mapFire[13][c] = ansiRed + "╒" + ansiReset;

            }
            else {
                mapFire[13][c] = ansiRed + "═" + ansiReset;
            }
        }

        mapFire[13][maxHorizontalLength - 14] = ansiRed + "╝" + ansiReset;
        mapFire[13][maxHorizontalLength - 13] = ansiYellow + "║" + ansiReset;

        for(int c = maxHorizontalLength - 12; c < maxHorizontalLength - 1; c++ ){
            mapFire[13][c] =  ansiYellow + "_" + ansiReset;
        }

        mapFire[13][maxHorizontalLength - 1] = ansiYellow + "║" + ansiReset;

        //14 row
        for (int c = 0; c < 13; c++){
            mapFire[14][c] = " ";
        }

        mapFire[14][13] = ansiWhite + "╔" + ansiReset;
        for(int c = 14; c < maxHorizontalLength - 14; c++){
            if (c == 15){
                mapFire[14][c] = ansiWhite + "╛" + ansiReset;
            }
            else if( c == 16){
                mapFire[14][c] = ansiWhite + " " + ansiReset;
            }
            else if( c == 17){
                mapFire[14][c] = ansiWhite + "╘" + ansiReset;
            }
            else
                mapFire[14][c] = ansiWhite + "═" + ansiReset;
        }

        mapFire[14][maxHorizontalLength - 14] = ansiWhite + "╗" + ansiReset;
        mapFire[14][maxHorizontalLength - 13] = ansiYellow + "║" + ansiReset;

        for(int c = maxHorizontalLength - 12; c < maxHorizontalLength - 1; c++){
            mapFire[14][c] = " ";
        }
        mapFire[14][maxHorizontalLength - 1] = ansiYellow + "║" + ansiReset;

        for(int r = 15; r < 20; r++){
            for(int c = 0; c < 13; c++){
                mapFire[r][c] = " ";
            }

            mapFire[r][13] = ansiWhite + "║" + ansiReset;

            for(int c = 14; c < maxHorizontalLength - 14; c++){
                if (c == 26){
                    mapFire[r][c] = ansiWhite + "│" + ansiReset;
                }
                else{
                    mapFire[r][c] = " ";
                }
            }

            if( r == 16) {
                mapFire[r][maxHorizontalLength - 14] = ansiWhite + "╙" + ansiReset;
                mapFire[r][maxHorizontalLength - 13] = ansiYellow + "╜" + ansiReset;
            }
            else if(r == 17){
                mapFire[r][maxHorizontalLength - 14] = ansiWhite + "╓" + ansiReset;
                mapFire[r][maxHorizontalLength - 13] = ansiYellow + "╖" + ansiReset;
            }
            else{
                mapFire[r][maxHorizontalLength - 14] = ansiWhite + "║" + ansiReset;
                mapFire[r][maxHorizontalLength - 13] = ansiYellow + "║" + ansiReset;
            }

            for (int c = maxHorizontalLength - 12; c < maxHorizontalLength - 1; c++)
                mapFire[r][c] = " ";

            mapFire[r][maxHorizontalLength - 1] = ansiYellow + "║" + ansiReset;
        }


        for (int c = 0; c < 13; c++){
            mapFire[20][c] = " ";
        }

        mapFire[20][13] = ansiWhite + "╚" + ansiReset;

        for(int c = 14; c < maxHorizontalLength - 14; c++)
            mapFire[20][c] = ansiWhite + "═" + ansiReset;

        mapFire[20][maxHorizontalLength - 14] = ansiWhite + "╝" + ansiReset;

        mapFire[20][maxHorizontalLength - 13] = ansiYellow + "╚" + ansiReset;

        for(int c = maxHorizontalLength - 12; c < maxHorizontalLength - 1; c++)
            mapFire[20][c] = ansiYellow + "═" + ansiReset;

        mapFire[20][maxHorizontalLength - 1] = ansiYellow + "╝" + ansiReset;
    }

    public static void printMyShit(){
        for (int r = 0; r < maxVerticalLength; r++) {

            System.out.println();

            for (int c = 0; c < maxHorizontalLength; c++) {

                System.out.print(mapFire[r][c]);

            }

        }
    }




    public static void printMapFire(){
        System.out.print(oldMap);
        fillMapFire();
        printMyShit();
    }

    public static void main(String[] args){
        printMapFire();
    }

}
