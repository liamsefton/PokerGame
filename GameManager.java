import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class GameManager {
    public Player[] players;
    public int gameType;
    public Deck deck;

    public GameManager(int numPlayers, int gameType, String playerName){
        players = new Player[numPlayers]; //creates list of players
        players[0] = new HumanPlayer("You", 1000); //players[0] is the human player
        for(int i = 1; i < numPlayers; i++){
            players[i] = new ComputerPlayer("CPU" + Integer.toString(i), 1000); //rest are computer players
        }
        this.gameType = gameType;
        deck = new Deck(); //initializes the deck object
    }

    public void dealRound(){
        //reset all players to be in this round
        for(int i = 0; i < players.length; i++){
            players[i].out_this_round = false;
            players[i].standing = false;
        }

        Scanner scanner = new Scanner(System.in);
        printPlayerBalances(); //prints current balances
        if(gameType == 0){ //blackjack
            int playersOutOrStanding = 0;
            //Get starting bets
            int bet = 0;
            for(int i = 0; i < players.length; i++){
                if(players[i].balance == 0){ //all players with a balance of 0 are made out this round
                    players[i].out_this_round = true;
                    playersOutOrStanding++;
                }
            }
            if(!players[0].out_this_round){ //this if statement is used all over the place, it checks to see if the player is in this round
                System.out.println("Enter amount to bet (ante is 100): ");
                bet = Integer.parseInt(scanner.nextLine());
            }
            if(bet < 100)
                bet = 100;

            //Deal cards
            System.out.println("Dealing... ");
            for(int i = 0; i < players.length; i++){
                if(!players[i].out_this_round){
                    players[i].bet(bet);  //first iteration uses human input bet
                    players[i].hand = new Hand(deck.getCards(2)); //deals 2 cards to all players
                }
                bet = 100; //bet set to 100 for CPU players
            }
            Hand houseHand = new Hand(deck.getCards(2)); //dealer's hand

            printBlackjack();
            System.out.println("Dealer hand is: " + houseHand.getHandString()[0] + " and 1 card face down."); //prints dealers hand

            int dealer_score = (new Hand(houseHand.cards[0]).getScore(gameType));
            if(!players[0].out_this_round && dealer_score == 10 || dealer_score == 11){ //if the dealer has 10 or ace as their first card, gives the player the choice to buy insurace
                System.out.println("Enter 1 to buy insurance or 0 to do nothing: ");
                if(scanner.nextLine().equals("1")){
                    System.out.println("Enter amount to bet: ");
                    ((HumanPlayer)players[0]).betInsurance(Double.parseDouble(scanner.nextLine())); //bets insurance
                }
            }

            if(houseHand.getScore(gameType) == 21){ //if the dealer has natural blackjack all players without insurance or blackjack lose
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
                ((HumanPlayer)players[0]).loseInsurance(); //if dealer didn't have blackjack, player loses insurance
            }
            
            //checking for natural blackjack
            for(int i = 0; i < players.length; i++){ //player natural blackjack checks here
                if(players[i].hand.getScore(gameType) == 21 && !players[i].out_this_round){
                    players[i].win(players[i].current_bet * 1.5);
                    playersOutOrStanding++;
                }
            }

            if(!players[0].out_this_round){ //this if statement is to give the player the choice to double down
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

            for(int i = 1; i < players.length; i++){ //this is an AI loop for the computer players to double down or not
                if(players[i].hand.getScore(gameType) == 11){
                    players[i].bet(players[i].current_bet);
                    players[i].hand.addCards(deck.getCards(1));
                    players[i].standing = true;
                    playersOutOrStanding++;
                    System.out.println("CPU " + Integer.toString(i) + " has doubled down!");
                }
            }

            while(playersOutOrStanding < players.length){ //loops until all players are out or standing
                try{
                    TimeUnit.MILLISECONDS.sleep(500); //these sleeps are used to slow down the printing for better UI
                }
                catch(InterruptedException e){
                    e.printStackTrace();
                }
                if(!players[0].out_this_round && !players[0].standing && players[0].hand.getScore(gameType) < 21){ //if player is not out and not standing 
                    System.out.println("Enter 0 to stand or 1 to hit: "); //ask if player wants to hit or stand
                    if(scanner.nextLine().equals("1")){
                        players[0].hand.addCards(deck.getCards(1));
                    }
                    else{
                        players[0].standing = true;
                        playersOutOrStanding++; //once a player stands they are "out" until the win/lose phase
                    }
                }
                for(int i = 1; i < players.length; i++){ //implementes the hitting for the CPU players
                    if(!players[i].out_this_round && !players[i].standing){
                        if(((ComputerPlayer)players[i]).getDecision(gameType) == 1){ //calls the getDecision function for AI decision
                            players[i].hand.addCards(deck.getCards(1));
                        }
                        else{
                            players[i].standing = true;
                            playersOutOrStanding++;
                        }
                    }
                }
                for(int i = 0; i < players.length; i++){ //this loop checks for wins or losses
                    if(players[i].hand.getScore(gameType) == 21 && !players[i].out_this_round){ //win with blackjack
                        players[i].win(players[i].current_bet);
                        if(!players[i].standing)
                            playersOutOrStanding++;
                    }
                    else if(players[i].hand.getScore(gameType) > 21 && !players[i].out_this_round){ //bust by going over
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

            System.out.println("Dealer hand is: " + Arrays.toString(houseHand.getHandString()));
            while(houseHand.getScore(gameType) < 17){ //loops until dealers hand is worth more than 17, per the rules of blackjack
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

            if(houseHand.getScore(gameType) > 21){ //if the dealer busts
                System.out.println("Dealer hand worth: " + Integer.toString(houseHand.getScore(gameType)));
                System.out.println("Dealer loses, everyone wins!");
                for(int i = 0; i < players.length; i++){ //all players not already out win
                    if(!players[i].out_this_round)
                        players[i].win(players[i].current_bet);
                }
            }

            for(int i = 0; i < players.length; i++){ //final win/loss check loop that compares player scores to house score
                try{
                    TimeUnit.MILLISECONDS.sleep(500);
                }
                catch(InterruptedException e){
                    e.printStackTrace();
                }
                if(!players[i].out_this_round){
                    if(players[i].hand.getScore(gameType) == houseHand.getScore(gameType)){ //tie
                        players[i].tie();
                    }
                    else if(players[i].hand.getScore(gameType) > houseHand.getScore(gameType)){ //win
                        players[i].win(players[i].current_bet);
                    }
                    else{ //lose
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
            //that is the end of 1 round, so it shuffles the deck
            System.out.println("Round over.");
            System.out.println("----------------------------------------------------------");
            deck.shuffle();
            System.out.println("Deck has been shuffled.");
        }
        else if(gameType == 1){ //5 card draw
            /*THE FOLLOWING CODE WAS MADE QUICKLY TO ILLUSTRATE THAT IMPLEMENTING POKER IS POSSIBLE AND EASY*/
            int playersOut = 0;
            //Get starting bets
            int bet = 0;
            for(int i = 0; i < players.length; i++){ //all players with a balance of 0 are out
                if(players[i].balance == 0){
                    players[i].out_this_round = true;
                    playersOut++;
                }
            }
            

            //Deal cards
            System.out.println("Dealing (ante of 100 placed)... ");
            for(int i = 0; i < players.length; i++){
                if(!players[i].out_this_round){
                    players[i].bet(100);
                    players[i].hand = new Hand(deck.getCards(5)); //deal 5 cards to all players
                }
                bet = 100;
            }
            printPoker();

            System.out.println("Do you want to fold? (0 for no, 1 for yes)"); //first fold oppurtunity
            if(scanner.nextLine().equals("1")){
                players[0].lose();
            }

            if(!players[0].out_this_round){
                System.out.println("Enter amount to bet (minimum amount is 100): "); //first round bet
                bet = Integer.parseInt(scanner.nextLine());
            }
            if(bet < 100)
                bet = 100;

            //Placing bets
            System.out.println("Bets have been placed.");
            for(int i = 0; i < players.length; i++){ //loop to place the bets
                if(!players[i].out_this_round){
                    players[i].bet(bet);
                }
                bet = 100;
            }
            
            int numDiscard = 0;
            if(!players[0].out_this_round){ //ask how many cards the player wants to discard
                System.out.println("How many cards do you want to discard(0-3): ");
                numDiscard = Integer.parseInt(scanner.nextLine());
            }

            if(numDiscard > 3 || numDiscard < 0){ //bounds check for funny guys who enter wrong numbers
                System.out.println("Funny guy eh?");
                numDiscard = 0;
            }

            if(!players[0].out_this_round){ //main discard code
                if(numDiscard > 0){ //if we need to discard at least 1 card
                    System.out.println("Your hand is: " + Arrays.toString(players[0].hand.getHandString())); //print hand for player
                    if(numDiscard == 1){ //only 1 discard
                        System.out.println("Enter which card you want to discard(1-5): ");
                        int cardToDiscard = Integer.parseInt(scanner.nextLine());
                        if(cardToDiscard > 5 || cardToDiscard < 1){ //bounds check
                            System.out.println("Why are you like this?");
                            cardToDiscard = 1; //default of 1
                        }
                        players[0].hand.cards[cardToDiscard - 1] = deck.getCards(1)[0]; //replaces card with new one
                    }
                    else if(numDiscard == 2){ //2 discards
                        int[] cardsToDiscard = new int[2]; //stores indices for cards to be discarded
                        System.out.println("Enter first card you want to discard(1-5): ");
                        int cardToDiscard = Integer.parseInt(scanner.nextLine());
                        if(cardToDiscard > 5 || cardToDiscard < 1){
                            System.out.println("Why are you like this?");
                            cardToDiscard = 1;
                        }
                        cardsToDiscard[0] = cardToDiscard;
                        System.out.println("Enter second card you want to discard(1-5 and not " + Integer.toString(cardToDiscard) + "): ");
                        int secondCardToDiscard = Integer.parseInt(scanner.nextLine()); //second card to discard is checked
                        if(secondCardToDiscard > 5 || secondCardToDiscard < 1 || secondCardToDiscard == cardToDiscard){ //make sure player doesnt discard the same card twice
                            System.out.println("Why are you like this?");
                            if(cardToDiscard == 5)
                                secondCardToDiscard = 4;
                            else{
                                secondCardToDiscard = cardToDiscard + 1;
                            }
                        }
                        cardsToDiscard[1] = secondCardToDiscard;
                        for(int i = 0; i < cardsToDiscard.length; i++){ //loop to discard all cards in the cardsToDiscard array
                            players[0].hand.cards[cardsToDiscard[i] - 1] = deck.getCards(1)[0];
                        }
                    }
                    else{ //3 discards
                        int[] cardsToDiscard = new int[3]; //stores the indices of the cards to be discarded
                        System.out.println("Enter first card you want to discard(1-5): ");
                        int cardToDiscard = Integer.parseInt(scanner.nextLine());
                        if(cardToDiscard > 5 || cardToDiscard < 1){
                            System.out.println("Why are you like this?");
                            cardToDiscard = 1;
                        }
                        cardsToDiscard[0] = cardToDiscard;
                        System.out.println("Enter second card you want to discard(1-5 and not " + Integer.toString(cardToDiscard) + "): ");
                        int secondCardToDiscard = Integer.parseInt(scanner.nextLine());
                        if(secondCardToDiscard > 5 || secondCardToDiscard < 1 || secondCardToDiscard == cardToDiscard){ //makes sure player doesnt choose bad number
                            System.out.println("Why are you like this?");
                            if(cardToDiscard == 5)
                                secondCardToDiscard = 4;
                            else{
                                secondCardToDiscard = cardToDiscard + 1;
                            }
                        }
                        cardsToDiscard[1] = secondCardToDiscard;
                        System.out.println("Enter third card you want to discard(1-5 and not " + Integer.toString(cardToDiscard) + " or " + Integer.toString(secondCardToDiscard) + "): ");
                        int thirdCardToDiscard = Integer.parseInt(scanner.nextLine());
                        if(thirdCardToDiscard > 5 || thirdCardToDiscard< 1 || thirdCardToDiscard == cardToDiscard || thirdCardToDiscard == secondCardToDiscard){ //makes sure player doesnt choose bad number
                            System.out.println("Why are you like this?");
                            for(int i = 1; i < 6; i++){
                                if(i != cardToDiscard && i != secondCardToDiscard){
                                    thirdCardToDiscard = i;
                                }
                            }
                        }
                        cardsToDiscard[2] = thirdCardToDiscard;
                        for(int i = 0; i < cardsToDiscard.length; i++){ //loops through cardsToDiscard and replaces those cards
                            players[0].hand.cards[cardsToDiscard[i] - 1] = deck.getCards(1)[0];
                        }
                    }
                }
            }
            printPoker(); //print new hand
            System.out.println("Do you want to fold(0 for no, 1 for yes)? ");
            if(scanner.nextLine().equals("1")){ //second fold oppurtunity
                players[0].lose();
            }

            if(!players[0].out_this_round){ //second round of betting
                System.out.println("Enter amount to bet (minimum amount is 100): ");
                bet = Integer.parseInt(scanner.nextLine());
            }
            if(bet < 100)
                bet = 100;

            players[0].bet(bet);
            double currentPot = 0;
            int winner = 0;
            int winnerIndex = 0;
            if(!players[0].out_this_round){
                winner = players[0].hand.getScore(gameType);
                currentPot = players[0].current_bet;
            }
            for(int i = 1; i < players.length; i++){ //this loop checks for which player has the highest score
                currentPot += players[i].current_bet; //current pot is the current bets of all players
                if(players[i].hand.getScore(gameType) > winner){ //new winner
                    winner = players[i].hand.getScore(gameType);
                    players[winnerIndex].lose(); //old winner loses
                    winnerIndex = i;
                }
                else{
                    players[i].lose(); //anyone who doesnt win, loses
                }
            }
            players[winnerIndex].win(currentPot); //winner wins the pot
            System.out.println("Round over.");
            System.out.println("----------------------------------------------------------");
            deck.shuffle();
            System.out.println("Deck has been shuffled.");
        }
    }

    public void printPoker(){
        try{
            TimeUnit.MILLISECONDS.sleep(200);
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
        if(players[0].balance == 0){
            System.out.print("");
        }
        else if(players[0].out_this_round){
            System.out.print("");
        }
        else{
            System.out.println("Your hand is: " + Arrays.toString(players[0].hand.getHandString()));
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
