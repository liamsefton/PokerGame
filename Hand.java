import java.util.Arrays;
public class Hand {
    public int[] cards;

    public Hand(int[] cards){ //regular constructor
        this.cards = new int[cards.length];
        for(int i = 0; i < cards.length; i++){
            this.cards[i] = cards[i];
        }
    }

    public Hand(int card){ //constructor for single card hand
        this.cards = new int[1];
        this.cards[0] = card;
    }

    public int getScore(int game_type){
        if(game_type == 0){
            int retVal = 0;
            boolean highAce = false;
            for(int i = 0; i < cards.length; i++){
                if((cards[i] / 4 + 1) > 10)
                    retVal += 10;
                else if((cards[i] / 4 + 1) == 1){ //if it is an ace, count the value as 11
                    if(retVal < 11){
                        retVal += 11;
                        highAce = true;
                    }
                    else
                        retVal += 1;
                }
                else
                    retVal += cards[i] / 4 + 1;

                if(retVal > 21 && highAce){ //if the score goes over 21 and there is an ace being counted as 11, decrement 10 to count it as 1
                    retVal -= 10;
                    highAce = false;
                }

            }
            return retVal;
        }
        else if(game_type == 1){
            //simple if else chain that goes high to low
            if(hasRoyalFlush())
                return 1000 + getHighCard();
            else if(hasStraightFlush())
                return 900 + getHighCard();
            else if(hasFourOfAKind())
                return 800 + getFourOfAKindHigh();
            else if(hasFullHouse())
                return 700 + getFullHouseHigh();
            else if(hasFlush())
                return 600 + getHighCard();
            else if(hasStraight())
                return 500 + getStraightHigh();
            else if(hasThreeOfAKind())
                return 400 + getThreeOfAKindHigh();
            else if(hasTwoPair())
                return 300 + getTwoPairHigh();
            else if(hasPair())
                return 200 + getPairHigh();
            else
                return getHighCard();
        }
        return -1;
    }

    public void addCards(int[] cards){
        int[] prevCards = new int[this.cards.length]; //creates copy of previous hand
        for(int i = 0; i < this.cards.length; i++){
            prevCards[i] = this.cards[i];
        }
        this.cards = new int[prevCards.length + cards.length]; //initializes new cards array
        int index = 0;
        for(int i = 0; i < prevCards.length; i++){ //adds previous hand to new hand
            this.cards[index] = prevCards[i];
            index++;
        }
        for(int i = 0; i < cards.length; i++){
            this.cards[index] = cards[i]; //adds new cards to new hand
        }
    }

    public void dealCards(int[] cards){
        for(int i = 0; i < cards.length; i++){
            this.cards[i] = cards[i];
        }
    }

    private boolean hasRoyalFlush(){
        boolean hasAceHigh = (getHighCard()/4 + 1) == 1; //assigns boolean to hasAceHigh if high card is an ace
        return hasAceHigh && hasStraight() && hasFlush(); //return true if hand has ace high, straight and flush
    }

    private boolean hasStraightFlush(){
        //this will be checked after royal flush so no need to check for ace high
        return hasStraight() && hasFlush();
    }

    private int getHighCard(){
        //simple max calculation
        int max_card = cards[0];
        for(int i = 1; i < cards.length; i++){
            if((cards[i]/4 + 1) == 1){ //if its an ace
                if(max_card/4 + 1 == 1){ //if current max card is an ace
                    if(cards[i] > max_card){
                        max_card = cards[i];
                    }
                }
                else{ //ace higher than any non-ace card
                    max_card = cards[i];
                }
            }
            else if(cards[i] > max_card){
                max_card = cards[i];
            }
        }
        return max_card;
    }

    private boolean hasStraight(){
        int[] temp_list = this.cards.clone(); //create a copy list to sort
        for(int i = 0; i < temp_list.length; i++){
            temp_list[i] = temp_list[i]/4 + 1;
        }
        Arrays.sort(temp_list); //sort the list because it makes it so much easier
        int prev_card = temp_list[0];
        for(int i = 1; i < temp_list.length; i++){
            if((temp_list[i] - prev_card) != 1) //if the next card is not 1 higher, it is not a straight
                return false;
            prev_card = temp_list[i];
        }
        return true;
    }

    private int getStraightHigh(){
        int[] temp_list = this.cards.clone(); //create a copy list to sort
        Arrays.sort(temp_list); //sort the list because it makes it so much easier
        int max = temp_list[0];
        for(int i = 1; i < temp_list.length; i++){
            if(temp_list[i] > max){
                max = temp_list[i];
            }
        }
        return max;
    }

    private boolean hasPair(){
        int[] temp_list = this.cards.clone(); //create a copy list to sort
        for(int i = 0; i < temp_list.length; i++){
            temp_list[i] = temp_list[i]/4 + 1;
        }
        Arrays.sort(temp_list); //sort the list because it makes it so much easier
        int prev_card = temp_list[0];
        for(int i = 1; i < temp_list.length; i++){
            if((temp_list[i] - prev_card) == 0){ //if two cards are the same (consecutive cards because sorted)
                return true;
            }
            prev_card = temp_list[i];
        }
        return false;
    }

    private int getPairHigh(){
        int[] temp_list = this.cards.clone(); //create a copy list to sort
        Arrays.sort(temp_list); //sort the list because it makes it so much easier
        int prev_card = temp_list[0];
        for(int i = 1; i < temp_list.length; i++){
            if(temp_list[i]/4 +1 == prev_card/4 + 1){
                if(temp_list[i] > prev_card){
                    return temp_list[i];
                }
                else{
                    return prev_card;
                }
            }
        }
        return -1;
    }

    private boolean hasTwoPair(){
        int[] temp_list = this.cards.clone(); //create a copy list to sort
        for(int i = 0; i < temp_list.length; i++){
            temp_list[i] = temp_list[i]/4 + 1;
        }
        Arrays.sort(temp_list); //sort the list because it makes it so much easier
        int num_pairs = 0;
        int prev_card = temp_list[0];
        //This code will be checked after three of a kind, so it won't return 2 pairs for 3 of a kind
        for(int i = 1; i < temp_list.length; i++){
            if(temp_list[i] == prev_card){
                num_pairs += 1;
            }
            prev_card = temp_list[i];
        }
        return num_pairs == 2;
    }

    private int getTwoPairHigh(){
        int[] temp_list = this.cards.clone(); //create a copy list to sort
        Arrays.sort(temp_list); //sort the list because it makes it so much easier
        int prev_card = temp_list[0];
        int firstHigh = 0;
        int firstPairEndIndex = 0;
        for(int i = 1; i < temp_list.length; i++){ //checks for high card of first pair
            firstPairEndIndex = i;
            if(temp_list[i]/4 +1 == prev_card/4 + 1){
                if(temp_list[i] > prev_card){
                    firstHigh = temp_list[i];
                }
                else{
                    firstHigh = prev_card;
                }
                break;
            }
        }
        prev_card = temp_list[firstPairEndIndex + 1];
        for(int i = firstPairEndIndex + 2; i < temp_list.length; i++){//checks for high card of second pair
            if(temp_list[i]/4 + 1 == prev_card/4 + 1){
                if(temp_list[i] > prev_card){
                    if(temp_list[i] > firstHigh){
                        return temp_list[i];
                    }
                    else{
                        return firstHigh;
                    }
                }
                else{
                    if(prev_card > firstHigh){
                        return prev_card;
                    }
                    else{
                        return firstHigh;
                    }
                }
            }
        }
        return -1;
        
    }

    private boolean hasFlush(){
        int first_suit = cards[0] % 4;
        for(int i = 1; i < cards.length; i++){
            if((cards[i] % 4) != first_suit){ //every card has to have same suit as the first
                return false;
            }
        }
        return true;
    }


    private boolean hasThreeOfAKind(){
        int[] temp_list = this.cards.clone(); //create a copy list to sort
        for(int i = 0; i < temp_list.length; i++){
            temp_list[i] = temp_list[i]/4 + 1;
        }
        Arrays.sort(temp_list); //sort the list because it makes it so much easier
        int prev_prev_card = cards[0];
        int prev_card = cards[1];
        for(int i = 2; i < temp_list.length; i++){
            if(temp_list[i] == prev_prev_card && temp_list[i] == prev_card){
                return true;
            }
            prev_prev_card = prev_card; //kinda cute solution, iterates to keep two previous cards and since it is sorted it will find any three of a kind
            prev_card = temp_list[i]; 
        }
        return false;
    }

    private int getThreeOfAKindHigh(){
        int[] temp_list = this.cards.clone(); //create a copy list to sort
        for(int i = 0; i < temp_list.length; i++){
            temp_list[i] = temp_list[i]/4 + 1;
        }
        Arrays.sort(temp_list); //sort the list because it makes it so much easier
        int prev_prev_card = cards[0];
        int prev_card = cards[1];
        for(int i = 2; i < temp_list.length; i++){
            if(temp_list[i] == prev_prev_card && temp_list[i] == prev_card){
                return temp_list[i];
            }
            prev_prev_card = prev_card; //kinda cute solution, iterates to keep two previous cards and since it is sorted it will find any three of a kind
            prev_card = temp_list[i]; 
        }
        return -1;
    }
    private boolean hasFourOfAKind(){
        int[] temp_list = this.cards.clone(); //create a copy list to sort
        for(int i = 0; i < temp_list.length; i++){
            temp_list[i] = temp_list[i]/4 + 1;
        }
        Arrays.sort(temp_list); //sort the list because it makes it so much easier
        int prev_prev_prev_card = cards[0];
        int prev_prev_card = cards[1];
        int prev_card = cards[2];
        for(int i = 3; i < temp_list.length; i++){
            if(temp_list[i] == prev_prev_prev_card && temp_list[i] == prev_prev_card && temp_list[i] == prev_card){
                return true;
            }
            prev_prev_prev_card = prev_prev_card; //cute solution, iterates to keep 3 previous cards and since it is sorted it will find any four of a kind
            prev_prev_card = prev_card; 
            prev_card = temp_list[i];
        }
        return false;
    }

    private int getFourOfAKindHigh(){
        int[] temp_list = this.cards.clone(); //create a copy list to sort
        for(int i = 0; i < temp_list.length; i++){
            temp_list[i] = temp_list[i]/4 + 1;
        }
        Arrays.sort(temp_list); //sort the list because it makes it so much easier
        int prev_prev_prev_card = cards[0];
        int prev_prev_card = cards[1];
        int prev_card = cards[2];
        for(int i = 3; i < temp_list.length; i++){
            if(temp_list[i] == prev_prev_prev_card && temp_list[i] == prev_prev_card && temp_list[i] == prev_card){
                return temp_list[i];
            }
            prev_prev_prev_card = prev_prev_card; //cute solution, iterates to keep 3 previous cards and since it is sorted it will find any four of a kind
            prev_prev_card = prev_card; 
            prev_card = temp_list[i];
        }
        return -1;
    }

    private boolean hasFullHouse(){
        return hasThreeOfAKind() && hasPair();
    }

    private int getFullHouseHigh(){
        if(getThreeOfAKindHigh() > getPairHigh()){
            return getThreeOfAKindHigh();
        }
        else{
            return getPairHigh();
        }
    }

    public String[] getHandString(){
        String[] retString = new String[cards.length];
        for(int i = 0; i < cards.length; i++){
            String tempString = "";
            int cardNum = (cards[i] / 4) + 1;
            if(cardNum == 1)
                tempString += "A";
            else if(cardNum == 11)
                tempString += "J";
            else if(cardNum == 12)
                tempString += "Q";
            else if(cardNum == 13)
                tempString += "K";
            else
                tempString += Integer.toString(cardNum);

            int cardSuit = cards[i] % 4;
            if(cardSuit == 0)
                tempString += "♧";
            else if(cardSuit == 1)
                tempString += "♢";
            else if(cardSuit == 2)
                tempString += "♡";
            else if(cardSuit == 3)
                tempString += "♤";

            retString[i] = tempString;
        }
        return retString;
    }
}
