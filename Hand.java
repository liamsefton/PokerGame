public class Hand {
    public int[] cards;
    public Hand(int[] cards){
        for(int i = 0; i < cards.length; i++){
            this.cards[i] = cards[i];
        }
    }

    public int getScore(int game_type){
        if(game_type == 0){
            return (cards[0] - cards[0] % 4) + (cards[1] - cards[1] % 4);
        }
        return -1;
    }

    public void dealCards(int[] cards){
        for(int i = 0; i < cards.length; i++){
            this.cards[i] = cards[i];
        }
    }
}
