import java.util.Random;
public class ComputerPlayer extends Player{
    public ComputerPlayer(String name, double balance, int[] hand){
        super(name, balance, hand);
    }

    public ComputerPlayer(String name, double balance){
        super(name, balance);
    }

    public int getDecision(int game_type){
        Random rand = new Random();
        if(game_type == 0){ //Blackjack
            if(hand.getScore(game_type) < 15) //if the score is less than 15, ai will hit
                return 1;
            else if(hand.getScore(game_type) < 19){ //if the score is less than 19 (and greater than 14 because first if), there is a 50% chance to hit
                if(rand.nextInt(10) < 5)
                    return 1;
                else
                    return 0;
            }
            return 0; //otherwise we return 0 to stand
        }
        return -1; //error code
    }


    
}
