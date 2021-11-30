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
        if(game_type == 0){
            if(hand.getScore(game_type) < 15)
                return 1;
            else if(hand.getScore(game_type) < 19){
                if(rand.nextInt(10) < 5)
                    return 1;
                else
                    return 0;
            }
            return 0;
        }
        return -1;
    }


    
}
