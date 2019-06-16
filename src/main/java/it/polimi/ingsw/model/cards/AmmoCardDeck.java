package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Color;

import java.util.ArrayList;

public class AmmoCardDeck {

    private static final int AMMOCARDNUMBER = 36;


    /**
     * The list of powerups in this deck
     */
    private static ArrayList<AmmoCard> ammoCardList;


    /**
     * Creates AmmoCardDeck with all the ammos and powerUps inside
     */
    public AmmoCardDeck(){

        ArrayList<Color> colors = new ArrayList<>();

        for ( int i = 0; i < AMMOCARDNUMBER; i++ ){

            String  ammoCardImagePath = "/graphics/ammo/ammo_" + i+1 + ".png";

            switch (i+1){

                case 1:
                    for (Color c : colors) colors.remove(c);
                    colors.add(Color.YELLOW);colors.add(Color.YELLOW);colors.add(Color.BLUE);
                    AmmoCard ammoCard1 = new AmmoCard(ammoCardImagePath, colors, false);ammoCardList.add(ammoCard1);
                case 2:
                    for (Color c : colors) colors.remove(c);
                    colors.add(Color.YELLOW);colors.add(Color.RED);colors.add(Color.RED);
                    AmmoCard ammoCard2 = new AmmoCard(ammoCardImagePath, colors, false);ammoCardList.add(ammoCard2);
                case 3:
                    for (Color c : colors) colors.remove(c);
                    colors.add(Color.RED);colors.add(Color.BLUE);colors.add(Color.BLUE);
                    AmmoCard ammoCard3 = new AmmoCard(ammoCardImagePath, colors, false);ammoCardList.add(ammoCard3);
                case 4:
                    for (Color c : colors) colors.remove(c);
                    colors.add(Color.RED);colors.add(Color.YELLOW);colors.add(Color.YELLOW);
                    AmmoCard ammoCard4= new AmmoCard(ammoCardImagePath, colors, false);ammoCardList.add(ammoCard4);
                case 5:
                    for (Color c : colors) colors.remove(c);
                    colors.add(Color.BLUE);colors.add(Color.YELLOW);colors.add(Color.YELLOW);
                    AmmoCard ammoCard5= new AmmoCard(ammoCardImagePath, colors, false);ammoCardList.add(ammoCard5);
                case 6:
                    for (Color c : colors) colors.remove(c);
                    colors.add(Color.BLUE);colors.add(Color.RED);colors.add(Color.RED);
                    AmmoCard ammoCard6= new AmmoCard(ammoCardImagePath, colors, false);ammoCardList.add(ammoCard6);
                case 7:
                    for (Color c : colors) colors.remove(c);
                    colors.add(Color.YELLOW);colors.add(Color.BLUE);colors.add(Color.BLUE);
                    AmmoCard ammoCard7= new AmmoCard(ammoCardImagePath, colors, false);ammoCardList.add(ammoCard7);
                case 8:
                    for (Color c : colors) colors.remove(c);
                    colors.add(Color.YELLOW);colors.add(Color.RED);colors.add(Color.RED);
                    AmmoCard ammoCard8= new AmmoCard(ammoCardImagePath, colors, false);ammoCardList.add(ammoCard8);
                case 9:
                    for (Color c : colors) colors.remove(c);
                    colors.add(Color.RED);colors.add(Color.BLUE);colors.add(Color.BLUE);
                    AmmoCard ammoCard9= new AmmoCard(ammoCardImagePath, colors, false);ammoCardList.add(ammoCard9);
                case 10:
                    for (Color c : colors) colors.remove(c);
                    colors.add(Color.RED);colors.add(Color.YELLOW);colors.add(Color.YELLOW);
                    AmmoCard ammoCard10= new AmmoCard(ammoCardImagePath, colors, false);ammoCardList.add(ammoCard10);
                case 11:
                    for (Color c : colors) colors.remove(c);
                    colors.add(Color.BLUE);colors.add(Color.YELLOW);colors.add(Color.YELLOW);
                    AmmoCard ammoCard11= new AmmoCard(ammoCardImagePath, colors, false);ammoCardList.add(ammoCard11);
                case 12:
                    for (Color c : colors) colors.remove(c);
                    colors.add(Color.BLUE);colors.add(Color.RED);colors.add(Color.RED);
                    AmmoCard ammoCard12 = new AmmoCard(ammoCardImagePath, colors, false);ammoCardList.add(ammoCard12);
                case 13:
                    for (Color c : colors) colors.remove(c);
                    colors.add(Color.YELLOW);colors.add(Color.BLUE);colors.add(Color.BLUE);
                    AmmoCard ammoCard13= new AmmoCard(ammoCardImagePath, colors, false);ammoCardList.add(ammoCard13);
                case 14:
                    for (Color c : colors) colors.remove(c);
                    colors.add(Color.YELLOW);colors.add(Color.RED);colors.add(Color.RED);
                    AmmoCard ammoCard14= new AmmoCard(ammoCardImagePath, colors, false);ammoCardList.add(ammoCard14);
                case 15:
                    for (Color c : colors) colors.remove(c);
                    colors.add(Color.RED);colors.add(Color.BLUE);colors.add(Color.BLUE);
                    AmmoCard ammoCard15= new AmmoCard(ammoCardImagePath, colors, false);ammoCardList.add(ammoCard15);
                case 16:
                    for (Color c : colors) colors.remove(c);
                    colors.add(Color.RED);colors.add(Color.YELLOW);colors.add(Color.YELLOW);
                    AmmoCard ammoCard16= new AmmoCard(ammoCardImagePath, colors, false);ammoCardList.add(ammoCard16);
                case 17:
                    for (Color c : colors) colors.remove(c);
                    colors.add(Color.BLUE);colors.add(Color.YELLOW);colors.add(Color.YELLOW);
                    AmmoCard ammoCard17= new AmmoCard(ammoCardImagePath, colors, false);ammoCardList.add(ammoCard17);
                case 18:
                    for (Color c : colors) colors.remove(c);
                    colors.add(Color.BLUE);colors.add(Color.RED);colors.add(Color.RED);
                    AmmoCard ammoCard18= new AmmoCard(ammoCardImagePath, colors, false);ammoCardList.add(ammoCard18);
                case 19:
                    for (Color c : colors) colors.remove(c);colors.add(Color.YELLOW);colors.add(Color.YELLOW);
                    AmmoCard ammoCard19= new AmmoCard(ammoCardImagePath, colors, true);ammoCardList.add(ammoCard19);
                case 20:
                    for (Color c : colors) colors.remove(c);
                    colors.add(Color.RED);colors.add(Color.RED);
                    AmmoCard ammoCard20= new AmmoCard(ammoCardImagePath, colors, true);ammoCardList.add(ammoCard20);
                case 21:
                    for (Color c : colors) colors.remove(c);
                    colors.add(Color.BLUE);colors.add(Color.BLUE);
                    AmmoCard ammoCard21= new AmmoCard(ammoCardImagePath, colors, true);ammoCardList.add(ammoCard21);
                case 22:
                    for (Color c : colors) colors.remove(c);
                    colors.add(Color.YELLOW);colors.add(Color.RED);
                    AmmoCard ammoCard22= new AmmoCard(ammoCardImagePath, colors, true);ammoCardList.add(ammoCard22);
                case 23:
                    for (Color c : colors) colors.remove(c);
                    colors.add(Color.YELLOW);colors.add(Color.BLUE);
                    AmmoCard ammoCard23= new AmmoCard(ammoCardImagePath, colors, true);ammoCardList.add(ammoCard23);
                case 24:
                    for (Color c : colors) colors.remove(c);
                    colors.add(Color.RED);colors.add(Color.BLUE);
                    AmmoCard ammoCard24= new AmmoCard(ammoCardImagePath, colors, true);ammoCardList.add(ammoCard24);
                case 25:
                    for (Color c : colors) colors.remove(c);
                    colors.add(Color.YELLOW);colors.add(Color.RED);
                    AmmoCard ammoCard25= new AmmoCard(ammoCardImagePath, colors, true);ammoCardList.add(ammoCard25);
                case 26:
                    for (Color c : colors) colors.remove(c);
                    colors.add(Color.YELLOW);colors.add(Color.BLUE);
                    AmmoCard ammoCard26= new AmmoCard(ammoCardImagePath, colors, true);ammoCardList.add(ammoCard26);
                case 27:
                    for (Color c : colors) colors.remove(c);
                    colors.add(Color.RED);colors.add(Color.BLUE);
                    AmmoCard ammoCard27= new AmmoCard(ammoCardImagePath, colors, true);ammoCardList.add(ammoCard27);
                case 28:
                    for (Color c : colors) colors.remove(c);
                    colors.add(Color.YELLOW);colors.add(Color.YELLOW);
                    AmmoCard ammoCard28= new AmmoCard(ammoCardImagePath, colors, true);ammoCardList.add(ammoCard28);
                case 29:
                    for (Color c : colors) colors.remove(c);
                    colors.add(Color.RED);colors.add(Color.RED);
                    AmmoCard ammoCard29= new AmmoCard(ammoCardImagePath, colors, true);ammoCardList.add(ammoCard29);
                case 30:
                    for (Color c : colors) colors.remove(c);
                    colors.add(Color.BLUE);colors.add(Color.BLUE);
                    AmmoCard ammoCard30= new AmmoCard(ammoCardImagePath, colors, true);ammoCardList.add(ammoCard30);
                case 31:
                    for (Color c : colors) colors.remove(c);
                    colors.add(Color.YELLOW);colors.add(Color.RED);
                    AmmoCard ammoCard31 = new AmmoCard(ammoCardImagePath, colors, true);ammoCardList.add(ammoCard31);
                case 32:
                    for (Color c : colors) colors.remove(c);
                    colors.add(Color.YELLOW);colors.add(Color.BLUE);
                    AmmoCard ammoCard32 = new AmmoCard(ammoCardImagePath, colors, true);ammoCardList.add(ammoCard32);
                case 33:
                    for (Color c : colors) colors.remove(c);
                    colors.add(Color.RED);colors.add(Color.BLUE);
                    AmmoCard ammoCard33= new AmmoCard(ammoCardImagePath, colors, true);ammoCardList.add(ammoCard33);
                case 34:
                    for (Color c : colors) colors.remove(c);
                    colors.add(Color.YELLOW);colors.add(Color.RED);
                    AmmoCard ammoCard34= new AmmoCard(ammoCardImagePath, colors, true);ammoCardList.add(ammoCard34);
                case 35:
                    for (Color c : colors) colors.remove(c);
                    colors.add(Color.YELLOW);colors.add(Color.BLUE);
                    AmmoCard ammoCard35= new AmmoCard(ammoCardImagePath, colors, true);ammoCardList.add(ammoCard35);
                case 36:
                    for (Color c : colors) colors.remove(c);
                    colors.add(Color.RED);colors.add(Color.BLUE);
                    AmmoCard ammoCard36= new AmmoCard(ammoCardImagePath, colors, true);ammoCardList.add(ammoCard36);
            }

        }

    }

    public ArrayList<AmmoCard> getAmmoCardList() {
        return ammoCardList;
    }

}
