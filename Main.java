import java.io.*;
import java.util.Scanner;
public class Main {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter game mode(0 for blackjack, 1 for game 1, 2 for game 2): ");
        int gamemode = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter number of players(1-5): ");
        int numPlayers = Integer.parseInt(scanner.nextLine());

        GameManager test = new GameManager(numPlayers, gamemode, "You");
        
        boolean keep_watching = false;
        while(!allBroke(test)){
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


        //System.out.println("Everyone is broke!");
    }

    public static boolean allBroke(GameManager gManager){
        boolean ret = true;
        for(int i = 0; i < gManager.players.length; i++){
            if(gManager.players[i].balance > 0)
                ret = false;
        }
        return ret;
    }
}
