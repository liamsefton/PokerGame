public class Hand {
    public int[] cards;
    public Hand(int[] cards){
        this.cards = cards.clone();
    }

    public int getScore(int game_type){
        if(game_type == 0){
            return (cards[0] - cards[0] % 4) + (cards[1] - cards[1] % 4);
        }
        return -1;
    }
}
