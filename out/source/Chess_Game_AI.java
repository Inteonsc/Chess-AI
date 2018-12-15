import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Chess_Game_AI extends PApplet {

//TODO

//add turns.
//add AI to look through and then choose a move.
//max then min for doing the AI. have a min and a max function.
//they keep calling each other and pass on the depth variable.


AI bob;

boolean moving = false;
Piece movingPiece;

boolean whiteturn = true;

ArrayList<PVector> potentialMoves;

int tileSize = 80;
PFont font;
GameBoard gb;
int movementtest = 0;

public void setup(){
  
  frameRate(60);
  bob = new AI(true,4);
  potentialMoves = new ArrayList<PVector>();
  font = createFont("Arial", 24, true);
  textFont(font);
  textAlign(CENTER, CENTER);
  gb = new GameBoard();
  
}

public void draw(){
  for(int i = 0; i<8;i++){
    for(int j = 0; j<8;j++){
      //draw checkerboard
      if((i+j) % 2 ==0){
        fill(255,255,255);
      }else{
        fill(0,0,0);
      }
      if(potentialMoves.size() > 0){
        for(int m = 0; m< potentialMoves.size(); m++){
         // println("check2");
          
          if(floor(potentialMoves.get(m).x) == i && floor(potentialMoves.get(m).y) == j){
            fill(0,255,0);
           // println(" p " + potentialMoves.get(m).x + "  " + potentialMoves.get(m).y);
          }
        }
      }
      rect(i* tileSize, j* tileSize, tileSize, tileSize);

      //draw 
      if((i+j) % 2 ==0){
        fill(0,0,0);
      }else{
        fill(255,255,255);
      }
      if(gb.getPieceAt(i,j) != null){
        text(gb.getPieceAt(i,j).type, (i *tileSize) +40 ,(j * tileSize) +40 );
      }
      
      }
    }
  }


public void keyPressed(){

  if(key == 'g'){
    //println(bob.Max(gb,0));
  }

}


public void mousePressed(){
if(whiteturn){
int tempx = floor(mouseX / tileSize);
int tempy = floor(mouseY / tileSize);
println("x: " +tempx + " y: " + tempy);

  if(!moving){
    if(gb.getPieceAt(tempx,tempy) != null){
      movingPiece = gb.getPieceAt(tempx,tempy);
      movingPiece.beingMoved = true;
      potentialMoves = movingPiece.generateNewMoves(gb);
      
    }else{
      
      return;
      
    }
    
  }else{
    if(movingPiece.checkMovement(tempx,tempy,gb)){
      movingPiece.Move(tempx,tempy,gb);
      movingPiece.alreadymoved = true;
      movingPiece.beingMoved = false;
      movingPiece = null;
      potentialMoves.removeAll(potentialMoves);
      whiteturn = false;
    }else{
      movingPiece.beingMoved = false;
      movingPiece = null;
      potentialMoves.removeAll(potentialMoves);
    }

    
  }
  moving = !moving;
}else{
  bob.doTurn(gb);
  whiteturn = true;
}

}
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
        int maxscore = -100000;
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
        return maxscore;

    }

    public int Min(GameBoard gb, int depth){
                if(depth >= maxDepth){
                    return gb.EvaluateBoard(isBlack);
        }
        int worstscore = 100000;
        ArrayList<GameBoard> potentialboards = new ArrayList<GameBoard>();
        potentialboards = gb.generateNewBoards(gb,!isBlack);

        for(int i = 0; i< potentialboards.size(); i++){
            if(Max(potentialboards.get(i).clone(),depth + 1) < worstscore){
                worstscore = Max(potentialboards.get(i).clone(),depth + 1);

            }
        }



        return worstscore;
    }


}

class GameBoard{
  
  ArrayList<Piece> whitePieces = new ArrayList<Piece>();
  ArrayList<Piece> blackPieces = new ArrayList<Piece>();
  ArrayList<Piece> takenWhitePieces = new ArrayList<Piece>();
  ArrayList<Piece> takenBlackPieces = new ArrayList<Piece>(); 

  public GameBoard clone(){
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

  public void setBoard(GameBoard g){
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

  public Piece clone(){
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
  public void settings() {  size(640,640); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Chess_Game_AI" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
