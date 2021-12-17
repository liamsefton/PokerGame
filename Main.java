import java.util.Scanner;
public class Main {
    public static void main(String[] args){
        gameSelector();
    }

    public static void gameSelector(){
        String keepPlaying = "yes";
        Scanner input =new Scanner(System.in);
        do{
            int choice;
            System.out.println("\nAvailable Games: ");
            System.out.println("====================");

            System.out.println("1. BlackJack");
            System.out.println("2. Poker(Texas Holdem) vs Human");
            System.out.println("3. Poker vs Bot");
            System.out.println("4. Exit");

            System.out.print("\nChoice: ");
            choice=input.nextInt();
            if(choice==1){
                System.out.println("====================");
                gameOneRunner();
                
            }else if(choice==2){
                System.out.println("====================");
                gameTwoRunner();
            }else if(choice==3){
                System.out.println("===================="); 
                Scanner keyboard = new Scanner(System.in);
	            System.out.println("Hello, Welcome to Poker. You are playing with a Bot!");
	            System.out.println("Press Enter to Play! ");
		        Player2 player1 =  new Player2("You", 100);
		        keyboard.nextLine();
                Player2 computer = new Player2("CPU", 100);
                gameThreeRunner(player1, computer);

            }else if(choice==4){
                System.out.println("Sad to see you leave. Hope to see you soon! ");
                System.exit(1);
            }else{
                System.out.println("You have enter wrong input\nPlease Try again\n");
                gameSelector();
            }
            System.out.println("\n" + "Would you like to play again? Type: 'yes' or 'no'" + "\n");
            keepPlaying = input.next();
        }while(keepPlaying.equalsIgnoreCase("yes"));

    }

    public static void gameTwoRunner() {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter the number of players(1-5): ");
        int iPlayers = input.nextInt();// number of players input
        Table otable = new Table(iPlayers);// create table object
        otable.Deal();// deal the cards
        int counter = 0;

        while (otable.Playersize()>1 && counter < 3)
        {
            for (int i = 1; i<=otable.Playersize(); i++){
                if (otable.folded()==false){
                    System.out.println("Player " + i+  " has following cards in his/her hand.");
                    otable.Fold();// check to bet or fold
                    if (otable.Playersize()==1)// if there is only 1 player left
                    {
                        System.out.println("Game Over! ");
                        System.out.println("Last Player won "+ otable.getPot());
                        System.out.println("Thanks for playing! ");
                        System.exit(0);
                    }

                    otable.Nextplayer();//go to nextplayer
                    System.out.println("Total Pot: " + otable.getPot());// total pot value
                    System.out.println();//spacing
                }
            }
            otable.Drawcard();// drawcard in table
            counter +=1;
        }

        otable.Score();// check the score to who won the game
        System.out.println(otable.winnerchecker() + " wins " + otable.getPot());// print the winner.



    }

    public static void gameThreeRunner (Player2 player1, Player2 player2) {
        //Scanner input = new Scanner(System.in);
        Card[] deck = Poker.newDeck();
		int winner = 0;
		double playerWager = 0;
		double compWager = 0;
        
			Poker.displayBalance(player1);
			Poker.displayBalance(player2);
			
			Poker.shuffDeck(deck);
			Poker.dealHand(deck, player1);
			Poker.dealHand(deck, player2);
			
			System.out.println("========================================" + "");
			System.out.println(player1.getName() + "r turn");
			System.out.println("========================================" + "");
			playerWager = Poker.wager(player1, 0);
			System.out.println("\n");
			System.out.println("========================================" + "");
			System.out.println(player2.getName() + "'s' turn");
			System.out.println("========================================" + "");
			compWager = Poker.wager(player2, playerWager);
			System.out.println("\n");
			
			Poker.discardAndDeal(deck, player1);
			System.out.println("\n");
			Poker.discardAndDeal(deck, player2);
			System.out.println("\n");
			
			System.out.println(player2.getName() +"'s Cards: ");
			player2.showHand().sortByValue();
			System.out.println(player2.showHand().printHand());
			
			winner = player1.showHand().compareTo(player2.showHand());
			if(winner == 99) {
				System.out.println(player1.getName() + " have won!");
				player1.winnings(playerWager + compWager);	
			} 
			else if(winner == -99) {
				System.out.println(player2.getName() + " has won!");
				player2.winnings(playerWager + compWager);	
			} 
			else if(winner == 66) {
				player1.winnings(playerWager);
				player2.winnings(compWager);	
			}
			
			Poker.displayBalance(player1);
			Poker.displayBalance(player2);
			
			player1.showHand().clear();
			player2.showHand().clear();
			
	
	}


    

    public static void gameOneRunner(){
        Scanner input = new Scanner(System.in);
        System.out.println("Enter number of players(1-5): ");
        int numPlayers = Integer.parseInt(input.nextLine());

        GameManager test = new GameManager(numPlayers, 0, "You");

        boolean keep_watching = false;
        while(!allBroke(test)){
            test.dealRound();
            if(test.players[0].balance == 0 && !keep_watching){
                System.out.println("You are out of money, enter 0 to quit and 1 to watch the ComputerPlayers play: ");
                if(input.nextLine().equals("0")){
                    break;
                }
                else{
                    keep_watching = true;
                }
            }
        }
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