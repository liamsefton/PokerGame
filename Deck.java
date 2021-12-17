import java.util.Random;
public class Deck {
    public int[] cards;
    public int currentIndex;
    public Random rand;

    public Deck(){
        cards = new int[52];
        for(int i = 0; i < 52; i++){ //the cards are just integers between 0 and 51 inclusive
            cards[i] = i;
        }
        currentIndex = 0;
        rand = new Random();
        shuffle(); //shuffles the deck
    }

    public void shuffle(){
        //The for loop swaps each card at index i with a card at some random index r for all i [0-51]
        for(int i = 0; i < 52; i++){
            int temp = cards[i];
            int r = rand.nextInt(52);
            cards[i] = cards[r];
            cards[r] = temp;
        }

        currentIndex = 0; //keeps track of where the next card comes from
    }

    public int[] getCards(int n){
        //This returns n cards from the top of the deck
        int[] retCards = new int[n];
        int i = 0;
        int prevIndex = currentIndex;
        while(currentIndex < prevIndex + n){ //loops n times
            retCards[i] = cards[currentIndex]; //adds cards to return list
            i++;
            currentIndex++; //keeps track of the top of the deck
        }
        return retCards;
    }

}
