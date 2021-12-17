public class Hand {
    public int[] cards;

    public Hand(int[] cards){
        this.cards = new int[cards.length];
        for(int i = 0; i < cards.length; i++){
            this.cards[i] = cards[i];
        }
    }

    public Hand(int card){
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
                else if((cards[i] / 4 + 1) == 1){
                    if(retVal < 11){
                        retVal += 11;
                        highAce = true;
                    }
                    else
                        retVal += 1;
                }
                else
                    retVal += cards[i] / 4 + 1;

                if(retVal > 21 && highAce){
                    retVal -= 10;
                    highAce = false;
                }

            }
            return retVal;
        }
        return -1;
    }

    public void addCards(int[] cards){
        int[] prevCards = new int[this.cards.length];
        for(int i = 0; i < this.cards.length; i++){
            prevCards[i] = this.cards[i];
        }
        this.cards = new int[prevCards.length + cards.length];
        int index = 0;
        for(int i = 0; i < prevCards.length; i++){
            this.cards[index] = prevCards[i];
            index++;
        }
        for(int i = 0; i < cards.length; i++){
            this.cards[index] = cards[i];
        }
    }

    public void dealCards(int[] cards){
        for(int i = 0; i < cards.length; i++){
            this.cards[i] = cards[i];
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