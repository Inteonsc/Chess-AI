class Piece{
  String type;
  int xCoord;
  int yCoord;
  boolean isBlack;
  boolean beingMoved;
  boolean alreadymoved = false;
   
  public Piece(String Type, int XCoord, int YCoord, boolean IsBlack){
    type = Type;
    xCoord = XCoord;
    yCoord = YCoord;
    isBlack = IsBlack;
  }

  Piece clone(){
    Piece p = new Piece(type,xCoord,yCoord,isBlack);
    p.beingMoved = beingMoved;
    p.alreadymoved = alreadymoved;
    return p;
  }


//--------------------------------------- Movement Section --------------------------------------------
  public void Move(int x, int y, GameBoard gb){
    if(attackingEnemy(x,y,gb)){
      gb.killPieceAt(x,y);
    }
    xCoord = x;
    yCoord = y;
    
  }

  public boolean checkMovement(int x, int y, GameBoard gb){
    
    if(x<0 || x>7 || y <0 || y>7){
      //println("OOB");
      return false;
    }
   
    if(attackingAlly(x,y,gb)){
      //println("Attacking Ally");
      return false;
    }
    
    if(moveThroughPieces(x,y,gb) ){
      
      //println("Can't move through Piece");
      return false;
      
    }
   
    //check piecemovement need to add special movements
    switch(type){
      case "WPawn":
        if(!attackingEnemy(x,y,gb)){
          if(x != xCoord)
            return false;
          if(alreadymoved){
            if(y != yCoord - 1)
              return false;
          }else{
            if(y != yCoord - 1 && y != yCoord - 2)
              return false;
          }
          return true;
        }else{
          if(x != xCoord - 1 && x != xCoord + 1)
            return false;
          if(y != yCoord -1)
            return false;
          return true;
        }
        
      case "BPawn":
        if(!attackingEnemy(x,y,gb)){
          if(x != xCoord)
            return false;
          if(alreadymoved){
            if(y != yCoord + 1)
              return false;
          }else{
            if(y != yCoord + 1 && y != yCoord + 2)
              return false;
          }
            return true;
        }else{
          if(x != xCoord - 1 && x != xCoord + 1)
            return false;
          if(y != yCoord +1)
            return false;
          return true;
        }
        
      case "Rook":
        
        if(x == xCoord && y != yCoord)
          return true;
        if(x != xCoord && y == yCoord)
          return true;
        return false;
        
      case "Knight": 
        if(abs(x - xCoord) == 2 && abs(y - yCoord) == 1)
          return true; 
        if(abs(x - xCoord) == 1 && abs(y - yCoord) == 2)
          return true;
        return false;
      case "Bishop": 
        if(abs(x - xCoord) == abs(y - yCoord)){
          return true;
        }
        return false;
        //make it so it cannot move into check
      case "King":
        if(abs(x - xCoord ) <= 1&& abs(y - yCoord )<= 1)
          return true;
        
        return false;
      case "Queen":
        //Diagonal
        if(abs(x - xCoord) == abs(y - yCoord)){
          return true;
        }
        //Horizontal & Vertical
        if(x == xCoord || y == yCoord){
          return true;
        }
        //println("got here4");
        return false;
        
        
    }
    return false;
  }

  public boolean attackingAlly(int x, int y,GameBoard gb){
    Piece movetest = gb.getPieceAt(x,y);
    if(movetest == null){
      return false;
    }
    else if(movetest.isBlack == isBlack){
      return true;
    }
    return false;

  }
  public boolean attackingEnemy(int x, int y,GameBoard gb){
    Piece movetest = gb.getPieceAt(x,y);
    if(movetest == null){
      return false;
    }
    else if(movetest.isBlack != isBlack){
      return true;
    }
    return false;

  }

  public boolean moveThroughPieces(int x, int y, GameBoard gb){
    //see what direction it is going (current x or y - x or y. if its negative its going left/up if positive right/down)
    //check every tile in the direction its going (if queen moving left from 3,0 to 1,0 check 2,0)
    if(type == "Knight")
      return false;

    int moveDirectionX = x - xCoord;
    int moveDirectionY = y - yCoord;

    if(moveDirectionX >=1){
      moveDirectionX = 1;
    }else if(moveDirectionX <=-1){
      moveDirectionX = -1;
    }else{
      moveDirectionX = 0;
    }

    if(moveDirectionY >=1){
      moveDirectionY = 1;
    }else if(moveDirectionY <=-1){
      moveDirectionY = -1;
    }else{
      moveDirectionY = 0;
    }
    int tempX = xCoord + moveDirectionX;
    int tempY = yCoord + moveDirectionY;
    //this is what was causing the knight to freeze
    while(tempX != x || tempY != y){

      if(gb.getPieceAt(tempX, tempY) != null){
         return true;
      }
      tempX += moveDirectionX;
      tempY += moveDirectionY;
    }
    

    return false;
  }

  //-------------------------------- Move Generation/ AI section --------------------------------------------
  


  public ArrayList<PVector> generateNewMoves(GameBoard gb){
    ArrayList<PVector> moves = new ArrayList<PVector>();
    moves.removeAll(moves);


    switch(type){

      case "WPawn":
        if(checkMovement(xCoord, yCoord -1, gb))
          moves.add(new PVector(xCoord, yCoord -1));
        if(checkMovement(xCoord, yCoord -2, gb))
          moves.add(new PVector(xCoord, yCoord -2));
        if(checkMovement(xCoord + 1, yCoord -1, gb))
          moves.add(new PVector(xCoord +1, yCoord - 1 ));
        if(checkMovement(xCoord - 1, yCoord -1, gb))
          moves.add(new PVector(xCoord - 1, yCoord -1));
        break;
      case "BPawn":
        if(checkMovement(xCoord, yCoord +1, gb))
          moves.add(new PVector(xCoord, yCoord +1));
        if(checkMovement(xCoord, yCoord +2, gb))
          moves.add(new PVector(xCoord, yCoord +2));
        if(checkMovement(xCoord + 1, yCoord +1, gb))
          moves.add(new PVector(xCoord +1, yCoord + 1 ));
        if(checkMovement(xCoord - 1, yCoord +1, gb))
          moves.add(new PVector(xCoord - 1, yCoord +1));
        break;
      case "Rook":
        for(int i = 0; i<8; i++){
         //up
          if(checkMovement(xCoord, yCoord - i, gb)){
            moves.add(new PVector(xCoord ,yCoord - i));
          }
          //down
          if(checkMovement(xCoord , yCoord + i, gb)){
            moves.add(new PVector(xCoord,yCoord + i));
          }
          //left
          if(checkMovement(xCoord - i, yCoord , gb)){
            moves.add(new PVector(xCoord - i,yCoord ));
          }
          //right
          if(checkMovement(xCoord + i, yCoord , gb)){
            moves.add(new PVector(xCoord + i,yCoord ));
          }
        }
        break;

      case "Knight": 
        //left
        
        if(checkMovement(xCoord -2, yCoord + 1,gb))
          moves.add(new PVector(xCoord -2, yCoord + 1 ));
        
        if(checkMovement(xCoord -2, yCoord - 1,gb))
          moves.add(new PVector(xCoord -2, yCoord - 1 ));
        
        if(checkMovement(xCoord -1, yCoord + 2,gb))
          moves.add(new PVector(xCoord -1, yCoord + 2 ));
        
        if(checkMovement(xCoord -1, yCoord - 2,gb))
          moves.add(new PVector(xCoord - 1, yCoord - 2 ));
          
        //right
        if(checkMovement(xCoord +2, yCoord + 1,gb))
          moves.add(new PVector(xCoord +2, yCoord + 1 ));
        if(checkMovement(xCoord +2, yCoord - 1,gb))
          moves.add(new PVector(xCoord +2, yCoord - 1 ));
        if(checkMovement(xCoord +1, yCoord + 2,gb))
          moves.add(new PVector(xCoord +1, yCoord + 2 ));
        if(checkMovement(xCoord +1, yCoord - 2,gb))
          moves.add(new PVector(xCoord +1, yCoord - 2 ));
        break;

      case "Bishop": 

        for(int i= 0; i<8;i++){
          //diagonal top left
          if(checkMovement(xCoord - i, yCoord - i, gb)){
            moves.add(new PVector(xCoord - i,yCoord - i));
          }
          //diagonal top right
          if(checkMovement(xCoord + i, yCoord - i, gb)){
            moves.add(new PVector(xCoord + i,yCoord - i));
          }
          //diagonal bottom left
          if(checkMovement(xCoord - i, yCoord + i, gb)){
            moves.add(new PVector(xCoord - i,yCoord + i));
          }
          //diagonal bottom right
          
          if(checkMovement(xCoord + i, yCoord + i, gb)){
            moves.add(new PVector(xCoord + i,yCoord + i));
          }
          
        }
        break;
      case "King":
        for(int i = -1; i<2; i++){
          for(int j = -1; j<2; j++){
            if(checkMovement(xCoord - i, yCoord - j,gb)){
              moves.add(new PVector(xCoord - i,yCoord - j));
            }
          }
        }
        break;

      case "Queen":
        //
        for(int i= 0; i<8;i++){
          //diagonal top left
          if(checkMovement(xCoord - i, yCoord - i, gb)){
            moves.add(new PVector(xCoord - i,yCoord - i));
          }
          //diagonal top right
          if(checkMovement(xCoord + i, yCoord - i, gb)){
            moves.add(new PVector(xCoord + i,yCoord - i));
          }
          //diagonal bottom left
          if(checkMovement(xCoord - i, yCoord + i, gb)){
            moves.add(new PVector(xCoord - i,yCoord + i));
          }
          //diagonal bottom right
          
          if(checkMovement(xCoord + i, yCoord + i, gb)){
            moves.add(new PVector(xCoord + i,yCoord + i));
          }
          
          //up
          if(checkMovement(xCoord, yCoord - i, gb)){
            moves.add(new PVector(xCoord ,yCoord - i));
          }
          //down
          if(checkMovement(xCoord , yCoord + i, gb)){
            moves.add(new PVector(xCoord,yCoord + i));
          }
          //left
          if(checkMovement(xCoord - i, yCoord , gb)){
            moves.add(new PVector(xCoord - i,yCoord ));
            
          }
          //right
          if(checkMovement(xCoord + i, yCoord , gb)){
            moves.add(new PVector(xCoord + i,yCoord ));
          }
          
        }

        break;

    }




    return moves;
  }


}
