import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class GameManager {
    public Player[] players;
    public int currentPlayer;
    public int currentRound;
    public int gameType;
    public Deck deck;

    public GameManager(int numPlayers, int gameType, String playerName){
        players = new Player[numPlayers];
        players[0] = new HumanPlayer("You", 1000);
        for(int i = 1; i < numPlayers; i++){
            players[i] = new ComputerPlayer("CPU" + Integer.toString(i), 1000);
        }
        currentPlayer = 0;
        currentRound = 1;
        this.gameType = gameType;
        deck = new Deck();
    }

    public void dealRound(){
        //reset all players to be in this round
        for(int i = 0; i < players.length; i++){
            players[i].out_this_round = false;
            players[i].standing = false;
        }

        Scanner scanner = new Scanner(System.in);
        printPlayerBalances();
        if(gameType == 0){
            int playersOutOrStanding = 0;
            //Get starting bets
            int bet = 0;
            for(int i = 0; i < players.length; i++){
                if(players[i].balance == 0){
                    players[i].out_this_round = true;
                    playersOutOrStanding++;
                }
            }
            if(!players[0].out_this_round){
                System.out.println("Enter amount to bet (ante is 100): ");
                bet = Integer.parseInt(scanner.nextLine());
            }
            if(bet < 100)
                bet = 100;

            //Deal cards
            System.out.println("Dealing... ");
            for(int i = 0; i < players.length; i++){
                if(!players[i].out_this_round){
                    players[i].bet(bet);
                    players[i].hand = new Hand(deck.getCards(2));
                }
                bet = 100;
            }
            Hand houseHand = new Hand(deck.getCards(2)); //dealer's hand

            printBlackjack();
            System.out.println("Dealer hand is: " + houseHand.getHandString()[0] + " and 1 card face down.");

            int dealer_score = (new Hand(houseHand.cards[0]).getScore(gameType));
            if(!players[0].out_this_round && dealer_score == 10 || dealer_score == 11){
                System.out.println("Enter 1 to buy insurance or 0 to do nothing: ");
                if(scanner.nextLine().equals("1")){
                    System.out.println("Enter amount to bet: ");
                    ((HumanPlayer)players[0]).betInsurance(Double.parseDouble(scanner.nextLine()));
                }
            }

            if(houseHand.getScore(gameType) == 21){
                System.out.println("Dealer has blackjack with the hand: " + Arrays.toString(houseHand.getHandString()));
                System.out.println("All players without a blackjack or insurance lose.");
                for(int i = 0; i < players.length; i++){
                    if(!players[i].out_this_round){
                        if(i == 0 && ((HumanPlayer)players[0]).has_insurance){
                            ((HumanPlayer)players[0]).winInsurance();
                            players[0].out_this_round = true;
                        }
                        else if(players[i].hand.getScore(gameType) == 21){
                            players[i].tie();
                        }
                        else{
                            players[i].lose();
                        }
                        playersOutOrStanding++;
                    }
                }
            }

            if(!players[0].out_this_round && ((HumanPlayer)players[0]).has_insurance){
                ((HumanPlayer)players[0]).loseInsurance();
            }
            
            //checking for natural blackjack
            for(int i = 0; i < players.length; i++){
                if(players[i].hand.getScore(gameType) == 21 && !players[i].out_this_round){
                    players[i].win(players[i].current_bet * 1.5);
                    playersOutOrStanding++;
                }
            }

            if(!players[0].out_this_round){
                System.out.println("Enter 1 to double down or 0 to do nothing: ");
                if(scanner.nextLine().equals("1")){
                    players[0].hand.addCards(deck.getCards(1));
                    players[0].bet(players[0].current_bet);
                    players[0].standing = true;
                    playersOutOrStanding++;
                }
                if(players[0].hand.getScore(gameType) > 21){
                    players[0].lose();
                    playersOutOrStanding++;
                }
            }

            for(int i = 1; i < players.length; i++){
                if(players[i].hand.getScore(gameType) == 11){
                    players[i].bet(players[i].current_bet);
                    players[i].hand.addCards(deck.getCards(1));
                    players[i].standing = true;
                    playersOutOrStanding++;
                    System.out.println("CPU " + Integer.toString(i) + " has doubled down!");
                }
            }

            while(playersOutOrStanding < players.length){ //batch updating for each hit/stand
                try{
                    TimeUnit.MILLISECONDS.sleep(500);
                }
                catch(InterruptedException e){
                    e.printStackTrace();
                }
                if(!players[0].out_this_round && !players[0].standing && players[0].hand.getScore(gameType) < 21){
                    System.out.println("Enter 0 to stand or 1 to hit: ");
                    if(scanner.nextLine().equals("1")){
                        players[0].hand.addCards(deck.getCards(1));
                    }
                    else{
                        players[0].standing = true;
                        playersOutOrStanding++;
                    }
                }
                for(int i = 1; i < players.length; i++){
                    if(!players[i].out_this_round && !players[i].standing){
                        if(((ComputerPlayer)players[i]).getDecision(gameType) == 1){
                            players[i].hand.addCards(deck.getCards(1));
                        }
                        else{
                            players[i].standing = true;
                            playersOutOrStanding++;
                        }
                    }
                }
                for(int i = 0; i < players.length; i++){
                    if(players[i].hand.getScore(gameType) == 21 && !players[i].out_this_round){
                        players[i].win(players[i].current_bet);
                        if(!players[i].standing)
                            playersOutOrStanding++;
                    }
                    else if(players[i].hand.getScore(gameType) > 21 && !players[i].out_this_round){
                        players[i].lose();
                        playersOutOrStanding++;
                    }

                }
                System.out.println("Hands are: ");
                printBlackjack();
            }
            try{
                TimeUnit.MILLISECONDS.sleep(1000);
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }
            

        
            /*
            if(houseHand.getScore(gameType) == 21){
                System.out.println("Dealer has blackjack with the hand: " + Arrays.toString(houseHand.getHandString()));
                System.out.println("All players without a blackjack lose.");
                for(int i = 0; i < players.length; i++){
                    if(players[i].hand.getScore(gameType) == 21){
                        players[i].tie();
                    }
                    else{
                        players[i].lose();
                    }
                }
            */

            System.out.println("Dealer hand is: " + Arrays.toString(houseHand.getHandString()));
            while(houseHand.getScore(gameType) < 17){
                System.out.println("Dealer hand worth less than 17, hitting...");
                houseHand.addCards(deck.getCards(1));
                System.out.println("Dealer hand is: " + Arrays.toString(houseHand.getHandString()) + " with a score of " + Integer.toString(houseHand.getScore(gameType)));
            }

            try{
                TimeUnit.MILLISECONDS.sleep(200);
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }

            if(houseHand.getScore(gameType) > 21){
                System.out.println("Dealer hand worth: " + Integer.toString(houseHand.getScore(gameType)));
                System.out.println("Dealer loses, everyone wins!");
                for(int i = 0; i < players.length; i++){
                    if(!players[i].out_this_round)
                        players[i].win(players[i].current_bet);
                }
            }

            for(int i = 0; i < players.length; i++){
                try{
                    TimeUnit.MILLISECONDS.sleep(500);
                }
                catch(InterruptedException e){
                    e.printStackTrace();
                }
                if(!players[i].out_this_round){
                    if(players[i].hand.getScore(gameType) == houseHand.getScore(gameType)){
                        players[i].tie();
                    }
                    else if(players[i].hand.getScore(gameType) > houseHand.getScore(gameType)){
                        players[i].win(players[i].current_bet);
                    }
                    else{
                        players[i].lose();
                    }
                }
            }
            try{
                TimeUnit.MILLISECONDS.sleep(500);
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }
            System.out.println("Round over.");
            System.out.println("----------------------------------------------------------");
            deck.shuffle();
            System.out.println("Deck has been shuffled.");
        }
    }

    public void printBlackjack(){  //Prints the players hands and scores, dealer hand must be printed separately
        try{
            TimeUnit.MILLISECONDS.sleep(200);
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
        if(players[0].balance == 0){
            //System.out.println("You are out of money to bet, play again to reset balance.");
            System.out.print("");
        }
        else if(players[0].out_this_round){
            //System.out.println("You are out this round with a hand of: " + Arrays.toString(players[0].hand.getHandString()));
            System.out.print("");
        }
        else{
            System.out.println("Your hand is: " + Arrays.toString(players[0].hand.getHandString()) + " with score of " + Integer.toString(players[0].hand.getScore(gameType)));
        }

        for(int i = 1; i < players.length; i++){
            try{
                TimeUnit.MILLISECONDS.sleep(200);
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }
            if(players[i].balance == 0){
                System.out.println("CPU" + Integer.toString(i) + " is out of money.");
            }
            else if(players[i].out_this_round){
                //System.out.println("CPU" + Integer.toString(i) + " is out this round with a hand of: " + Arrays.toString(players[i].hand.getHandString()));
                System.out.print("");
            }
            else{
                System.out.println("CPU" + Integer.toString(i) + " hand is: " + Arrays.toString(players[i].hand.getHandString()) + " with score of " + Integer.toString(players[i].hand.getScore(gameType)));
            }
        }
    }

    public void printPlayerBalances(){ //prints player balances
        System.out.println("Your remaining balance is: " + Double.toString(players[0].balance));
        for(int i = 1; i < players.length; i++){
            System.out.println("CPU" + Integer.toString(i) + " has a remaining balance of: " + Double.toString(players[i].balance));
        }
    }
}