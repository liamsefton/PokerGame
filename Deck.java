import java.util.Random;
public class Deck {
    public int[] cards;
    public int currentIndex;
    public Random rand;

    public Deck(){
        cards = new int[52];
        for(int i = 1; i <= 52; i++){
            cards[i] = i;
        }
        currentIndex = 0;
        rand = new Random();
        shuffle();
    }

    public void shuffle(){
        for(int i = 0; i < 52; i++){
            int temp = cards[i];
            int r = rand.nextInt(52);
            cards[i] = cards[r];
            cards[r] = temp;
        }

        currentIndex = 0;
    }

    public int[] getCards(int n){
        int[] retCards = new int[n];
        int i = 0;
        while(currentIndex < currentIndex + n){
            retCards[i] = cards[currentIndex];
            currentIndex += 1;
        }
        return retCards;
    }

}
