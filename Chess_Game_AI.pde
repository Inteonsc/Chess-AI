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

void setup(){
  size(640,640);
  frameRate(60);
  bob = new AI(true,4);
  potentialMoves = new ArrayList<PVector>();
  font = createFont("Arial", 24, true);
  textFont(font);
  textAlign(CENTER, CENTER);
  gb = new GameBoard();
  
}

void draw(){
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


void keyPressed(){

  if(key == 'g'){
    //println(bob.Max(gb,0));
  }

}


void mousePressed(){
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