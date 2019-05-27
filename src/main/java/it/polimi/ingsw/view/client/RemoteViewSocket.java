package it.polimi.ingsw.view.client;

import it.polimi.ingsw.client.UserInterface;
import it.polimi.ingsw.view.ServerQuestion;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class RemoteViewSocket implements Runnable {

    /**
     * The socket connected to the server
     */
    Socket socket;

    /**
     * The interface used to talk with the user
     */
    UserInterface userInterface;

    /**
     *
     */
    final String SPLITTER = "\\$";

    /**
     * Constructor
     * @param socket the open socket with the server
     */
    public RemoteViewSocket(Socket socket, UserInterface userInterface){

        this.socket = socket;
        this.userInterface = userInterface;

    }

    @Override
    public void run() {

        try {
            //Creates a new scanner to read from the socket stream
            Scanner scanner = new Scanner(socket.getInputStream());

            while (true){

                //reads a new line
                String line = scanner.nextLine();

                //Divides the message into the message type and the actual message
                String messageType = line.split(SPLITTER)[0];

                if(!(messageType.equals("MESSAGE") || messageType.equals("QUESTION") || messageType.equals("NOTIFY"))) {
                    System.err.println("Received wrong message format:");
                    System.err.println("Line --> " + line);
                    System.err.println("MessageType --> " + messageType);
                    continue;
                }

                String message = line.split(SPLITTER)[1];

                if(messageType.equals("MESSAGE")) {
                    System.out.println("[*] Server MESSAGE --> " + message);
                }
                else if(messageType.equals("QUESTION")){

                    System.out.println("[*] Server QUESTION --> " + message);

                    //Creates a server question object from the JSON message
                    ServerQuestion serverQuestion = new ServerQuestion(message);
                    String[] possibleAnswers = fromArrayListToArray(serverQuestion.possibleAnswers);
                    int answerIndex = -1;

                    switch (serverQuestion.questionType){

                        case Action:
                            answerIndex = userInterface.askQuestionAction(possibleAnswers);
                            break;

                        case WhereToMove:
                            answerIndex = userInterface.askQuestionWhereToMove(possibleAnswers);
                            break;

                        case WhereToMoveAndGrab:
                            answerIndex = userInterface.askQuestionWhereToMoveAndGrab(possibleAnswers);
                            break;

                        case ChoosePowerUpToRespawn:
                            answerIndex = userInterface.askQuestionChoosePowerUpToRespawn(possibleAnswers);
                            break;

                        case ChoosePowerUpToUse:
                            answerIndex = userInterface.askQuestionChoosePowerUpToUse(possibleAnswers);
                            break;

                        case ChooseWeaponToAttack:
                            answerIndex = userInterface.askQuestionChooseWeaponToAttack(possibleAnswers);
                            break;

                        case ChooseWeaponToSwitch:
                            answerIndex = userInterface.askQuestionChooseWeaponToSwitch(possibleAnswers);
                            break;

                        case ChooseWeaponToReload:
                            answerIndex = userInterface.askQuestionChooseWeaponToReload(possibleAnswers);
                            break;

                        case PayWith:
                            answerIndex = userInterface.askQuestionPayWith(possibleAnswers);
                            break;

                        case Shoot:
                            answerIndex = userInterface.askQuestionShoot(possibleAnswers);
                            break;

                        case UseTurnPowerUp:
                            answerIndex = userInterface.askQuestionUseTurnPowerUp(possibleAnswers);
                            break;

                        case UseAsyncPowerUp:
                            answerIndex = userInterface.askQuestionUseAsyncPowerUp(possibleAnswers);
                            break;

                        default:
                            throw new RuntimeException("Error while parsing JSON question from RemoteViewSocket, no such QuestionType");
                    }

                    if(answerIndex == -1)
                        throw new RuntimeException("-1 is not a valid index for a client answer");

                    String jsonResponse = jsonResponse(serverQuestion, answerIndex);

                    sendMessage(jsonResponse);


                }
                else if(messageType.equals("NOTIFY")) {
                    System.out.println("[*] Server NOTIFY --> " + message);
                }
                else {
                    throw new RuntimeException("Not a valid message");
                }

            }
        }
        catch (IOException e){
            System.err.println("Error while creating the scanner");
            e.printStackTrace();
            return;
        }
        catch (Exception e){
            System.err.println("DISCONNECTED FROM SERVER");
            e.printStackTrace();
        }

    }

    private String jsonResponse(ServerQuestion serverQuestion, int answerIndex) {

        String json = "";

        json += "{";
        json += "\"" + "QuestionType" + "\"" + ": ";
        json += "\"" + serverQuestion.questionType.toString() + "\", ";

        json += "\"" + "PossibleAnswers" + "\"" + ": [";

        int i;
        for(i = 0; i < serverQuestion.possibleAnswers.size() - 1; i++)
            json += "\"" + serverQuestion.possibleAnswers.get(i) + "\", ";

        json += "\"" + serverQuestion.possibleAnswers.get(i) + "\"], ";

        json += "\"" + "Index" + "\"" + ": ";

        json += answerIndex;

        json += "}";


        return json;

    }

    private String[] fromArrayListToArray(ArrayList<String> possibleAnswers) {

        String[] tempString = new String[possibleAnswers.size()];

        for(int i = 0; i < possibleAnswers.size(); i++)
            tempString[i] = possibleAnswers.get(i);

        return tempString;

    }

    /**
     * Sends a message through the socket
     * @param message the message
     */
    public void sendMessage(String message){

        try {
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
            printWriter.println(message);
            printWriter.flush();
        }
        catch (IOException e){
            System.err.println(e.getMessage());
        }

    }
}
