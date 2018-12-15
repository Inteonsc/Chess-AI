
class GameBoard{
  
  ArrayList<Piece> whitePieces = new ArrayList<Piece>();
  ArrayList<Piece> blackPieces = new ArrayList<Piece>();
  ArrayList<Piece> takenWhitePieces = new ArrayList<Piece>();
  ArrayList<Piece> takenBlackPieces = new ArrayList<Piece>(); 

  GameBoard clone(){
    GameBoard g = new GameBoard();
    g.whitePieces.removeAll(g.whitePieces); 
    g.blackPieces.removeAll(g.blackPieces); 
    g.takenWhitePieces.removeAll(g.takenWhitePieces); 
    g.takenBlackPieces.removeAll(g.takenBlackPieces);
    for(int i = 0; i<16;i++){
    if(i<whitePieces.size())
      if(this.whitePieces.get(i) != null)
        g.whitePieces.add( this.whitePieces.get(i).clone());
    if(i<blackPieces.size())
      if(this.blackPieces.get(i) != null)
        g.blackPieces.add( this.blackPieces.get(i).clone());
    if(i<takenBlackPieces.size())
      if(this.takenBlackPieces.get(i) != null)
        g.takenBlackPieces.add(this.takenBlackPieces.get(i).clone());
    if(i<takenWhitePieces.size())
      if(this.takenWhitePieces.get(i) != null)
        g.takenWhitePieces.add(this.takenWhitePieces.get(i).clone());
    }
    return g;
  }

  void setBoard(GameBoard g){
    whitePieces.removeAll(whitePieces); 
    blackPieces.removeAll(blackPieces); 
    takenWhitePieces.removeAll(takenWhitePieces); 
    takenBlackPieces.removeAll(takenBlackPieces); 
    for(int i = 0; i<16;i++){
      if(i<g.whitePieces.size())
        if(g.whitePieces.get(i) != null)
          this.whitePieces.add( g.whitePieces.get(i).clone());
      if(i<g.blackPieces.size())
        if(g.blackPieces.get(i) != null)
          this.blackPieces.add( g.blackPieces.get(i).clone());
      if(i<g.takenBlackPieces.size())
        if(g.takenBlackPieces.get(i) != null)
          this.takenBlackPieces.add(g.takenBlackPieces.get(i).clone());
      if(i<g.takenWhitePieces.size())
        if(g.takenWhitePieces.get(i) != null)
          this.takenWhitePieces.add(g.takenWhitePieces.get(i).clone());
    }
  }


  public GameBoard(){
    for(int i = 0; i<8; i++){
      whitePieces.add( new Piece("WPawn" ,i,6,false));
      blackPieces.add(new Piece("BPawn" ,i,1,true));

    }
    whitePieces.add(new Piece("Rook", 0,7,false));
    whitePieces.add(new Piece("Knight", 1,7,false));
    whitePieces.add(new Piece("Bishop", 2,7,false));
    whitePieces.add(new Piece("Queen", 3,7,false));
    whitePieces.add(new Piece("King", 4,7,false));    
    whitePieces.add(new Piece("Bishop", 5,7,false));
    whitePieces.add(new Piece("Knight", 6,7,false));
    whitePieces.add(new Piece("Rook", 7,7,false));
    blackPieces.add(new Piece("Rook", 0,0,true));
    blackPieces.add(new Piece("Knight", 1,0,true));
    blackPieces.add(new Piece("Bishop", 2,0,true));
    blackPieces.add(new Piece("Queen", 3,0,true));
    blackPieces.add(new Piece("King", 4,0,true));    
    blackPieces.add(new Piece("Bishop", 5,0,true));
    blackPieces.add(new Piece("Knight", 6,0,true));
    blackPieces.add(new Piece("Rook", 7,0,true));
  }


  public Piece getPieceAt(int x, int y){
    for(int i = 0; i<16; i++){
      if(blackPieces.size()> i)
        if(blackPieces.get(i) != null && blackPieces.get(i).xCoord == x && blackPieces.get(i).yCoord == y){
          //println(blackPieces.get(i).type);
          return blackPieces.get(i);
        }
      if(whitePieces.size()>i)
        if(whitePieces.get(i) != null && whitePieces.get(i).xCoord == x && whitePieces.get(i).yCoord == y)
          return whitePieces.get(i);      
    }
    return null;
  }


  public void killPieceAt(int x, int y){
    for(int i = 0; i<16; i++){
      if(i< blackPieces.size())
        if(blackPieces.get(i) != null && blackPieces.get(i).xCoord == x && blackPieces.get(i).yCoord == y){
          takenBlackPieces.add(blackPieces.get(i).clone());
          blackPieces.remove(i);
        }
      if(i< whitePieces.size())
        if(whitePieces.get(i) != null && whitePieces.get(i).xCoord == x && whitePieces.get(i).yCoord == y){
          takenWhitePieces.add(whitePieces.get(i).clone());
          whitePieces.remove(i);      
        }
    }
    
  }

  public ArrayList<GameBoard> generateNewBoards(GameBoard gb, boolean isBlack){
    ArrayList<GameBoard> boards = new ArrayList<GameBoard>();
    GameBoard clonedboard = gb.clone();
    for(int i = 0; i<16; i++){
      if(!isBlack){
      ArrayList<PVector> potentialWhiteMoves = new ArrayList<PVector>();
      if(i< clonedboard.whitePieces.size())
        potentialWhiteMoves = clonedboard.whitePieces.get(i).generateNewMoves(clonedboard);

      if(potentialWhiteMoves.size() > 0){
        for(int m = 0; m< potentialWhiteMoves.size(); m++){
         // println("check2");
         //println("Black Type:" + clonedboard.whitePieces[i].type + "coords" + potentialWhiteMoves.get(m).x + " " + potentialWhiteMoves.get(m).y );
         GameBoard newboard = clonedboard.clone();
          newboard.whitePieces.get(i).Move(floor(potentialWhiteMoves.get(m).x), floor(potentialWhiteMoves.get(m).y) , newboard);
          boards.add(newboard.clone());
        }
      }
      }
      if(isBlack){
      ArrayList<PVector> potentialBlackMoves = new ArrayList<PVector>();
      if(i< clonedboard.blackPieces.size())
        potentialBlackMoves = clonedboard.blackPieces.get(i).generateNewMoves(clonedboard);

      if(potentialBlackMoves.size() > 0){
        for(int m = 0; m< potentialBlackMoves.size(); m++){
         //println("Black Type:" + clonedboard.blackPieces[i].type + "coords" + potentialBlackMoves.get(m).x + " " + potentialBlackMoves.get(m).y );
         GameBoard newboard = clonedboard.clone();
          newboard.blackPieces.get(i).Move(floor(potentialBlackMoves.get(m).x ),floor(potentialBlackMoves.get(m).y), newboard);
          boards.add(newboard.clone());
        }
      }
      }
    }

    return boards;

  }

  public int EvaluateBoard(boolean isBlack){
    int boardscore = 0;
    for(int i = 0; i<16; i++){
      if(i< whitePieces.size())
      if(whitePieces.get(i) != null){
        switch(whitePieces.get(i).type){
          case "WPawn":
            if(isBlack){
              boardscore -= 10;
            }else{
              boardscore += 10;
            }
            break;
          case "Rook":
            if(isBlack){
              boardscore -= 50;
            }else{
              boardscore += 50;
            }
            break;
          case "Knight":
            if(isBlack){
              boardscore -= 30;
            }else{
              boardscore += 30;
            }
            break;
          case "Bishop":
            if(isBlack){
              boardscore -= 30;
            }else{
              boardscore += 30;
            }
            break;
          case "King":
            if(isBlack){
              boardscore -= 1000;
            }else{
              boardscore += 1000;
            }
            break;
          case "Queen":
            if(isBlack){
              boardscore -= 90;
            }else{
              boardscore += 90;
            }
            break;
        }
      }
      if(i< blackPieces.size())
      if(blackPieces.get(i) != null){
        switch(blackPieces.get(i).type){
          case "BPawn":
            if(!isBlack){
              boardscore -= 10;
            }else{
              boardscore += 10;
            }
            break;
          case "Rook":
            if(!isBlack){
              boardscore -= 50;
            }else{
              boardscore += 50;
            }
            break;
          case "Knight":
            if(!isBlack){
              boardscore -= 30;
            }else{
              boardscore += 30;
            }
            break;
          case "Bishop":
            if(!isBlack){
              boardscore -= 30;
            }else{
              boardscore += 30;
            }
            break;
          case "King":
            if(!isBlack){
              boardscore -= 1000;
            }else{
              boardscore += 1000;
            }
            break;
          case "Queen":
            if(!isBlack){
              boardscore -= 90;
            }else{
              boardscore += 90;
            }
            break;
        }
      }
    }
    return boardscore;
  }
}
