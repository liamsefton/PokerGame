import java.io.*;
import java.util.Scanner;
public class Main {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in); //scanner 
        System.out.println("Enter game mode(0 for blackjack, or 1 for five card draw): ");
        int gamemode = Integer.parseInt(scanner.nextLine());
        if(gamemode == 1){
            System.out.println("Enter number of players(1-5): ");
        }
        else if(gamemode == 0){
            System.out.println("Enter number of players(1-5): "); 
        }
        int numPlayers = Integer.parseInt(scanner.nextLine());

        GameManager test = new GameManager(numPlayers, gamemode, "You");
        
        boolean keep_watching = false;
        while(!allBroke(test)){ //while all the players are not broke
            test.dealRound();
            if(test.players[0].balance == 0 && !keep_watching){  
                System.out.println("You are out of money, enter 0 to quit and 1 to watch the ComputerPlayers play: ");
                if(scanner.nextLine().equals("0")){
                    break;
                }
                else{
                    keep_watching = true;
                }
            }
        }

    }

    public static boolean allBroke(GameManager gManager){ //helper function
        boolean ret = true;
        for(int i = 0; i < gManager.players.length; i++){
            if(gManager.players[i].balance > 0)
                ret = false;
        }
        return ret;
    }
}
