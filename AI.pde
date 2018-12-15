class AI{
    int maxDepth;
    boolean isBlack;
    GameBoard bestNextBoard;

    public AI(boolean IsBlack,int Depth){
        maxDepth = Depth;
        isBlack = IsBlack;
    }

    public void doTurn(GameBoard gb){
        println("best score: " + Max(gb,0));
        gb.setBoard(bestNextBoard.clone());
        //println("ha");
    }

    public int Max(GameBoard gb, int depth){
        //generate boards from board given
        //if depth is at maximum just return the current board's score
        //if not give all boards to min.
        //get the returned boards and pick the best one.
        //(each part of the tree will have a seperate min and max so it only needs to decide 1 move)
        //once its figured out the best move set gb to that best board
        if(depth >= maxDepth){
            return gb.EvaluateBoard(isBlack);
        }
        int maxScore = -100000;
        int maxScoreIndex = 0;
        ArrayList<GameBoard> potentialboards = new ArrayList<GameBoard>();
        potentialboards = gb.generateNewBoards(gb,isBlack);

        ArrayList<GameBoard> minBoard = new ArrayList<GameBoard>();
        for(int i = 0; i< potentialboards.size(); i++){
            if(Min(potentialboards.get(i).clone(),depth + 1) > maxscore){
                maxscore = (Min(potentialboards.get(i).clone(),depth + 1));
                maxScoreIndex = i;
            }

        }


       



        if(depth == 0){
            bestNextBoard = potentialboards.get(maxScoreIndex).clone();
        }
        //println(BestBoards.get(0));
        return maxScore;

    }

    public int Min(GameBoard gb, int depth){
                if(depth >= maxDepth){
                    return gb.EvaluateBoard(isBlack);
        }
        int worstScore = 100000;
        ArrayList<GameBoard> potentialboards = new ArrayList<GameBoard>();
        potentialboards = gb.generateNewBoards(gb,!isBlack);

        for(int i = 0; i< potentialboards.size(); i++){
            if(Max(potentialboards.get(i).clone(),depth + 1) < worstscore){
                worstScore = Max(potentialboards.get(i).clone(),depth + 1);

            }
        }



        return worstScore;
    }


}