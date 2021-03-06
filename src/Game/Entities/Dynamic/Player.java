package Game.Entities.Dynamic;

import Main.Handler;




import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Game.GameStates.State;


/**
 * Created by AlexVR on 7/2/2018.
 */
public class Player {

    public int lenght;
    public boolean justAte;
    private Handler handler;
    public Color N =new Color((int)(Math.random() * 0x1000000)); // these lines of code make the snake change colors everytime
    public Color getN() {
		return N;
	}

	public void setN(Color n) {
		N = n;
	}

	public int xCoord;
    public int yCoord;

    public int moveCounter;

    public String direction;//is your first name one?

    public Player(Handler handler){
        this.handler = handler;
        xCoord = 0;
        yCoord = 0;
        moveCounter = 0;
        direction= "Right";
        justAte = false;
        lenght= 1;

    }
    int speed = 4;
    public void tick(){//Here I implemented BackTracking
        moveCounter++;
        if(moveCounter>= speed) {
            checkCollisionAndMove();
            moveCounter=0;
        }
       
        if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_UP)){
            if(direction != "Down") {
            	direction = "Up";
            }
        }if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_DOWN)){
            if(direction != "Up") {
            	direction = "Down";
            }
        }if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_LEFT)){
            if(direction != "Right") {
            	direction = "Left";
            }
        }if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_RIGHT)){
            if(direction != "Left") {
            	direction = "Right";
            }
           
        }
     
        ///ADD TAIL 
        if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_N)){
        	 handler.getWorld().body.addFirst(new Tail(xCoord, yCoord,handler));
        }
        
        //PAUSE
        if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_P)){
           State.setState(handler.getGame().pauseState);
        }
        
       
        
       //speed control
        if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_MINUS)){
    		speed ++;
    }
    
        if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_EQUALS)){
		speed --;
		}
        
        // Key R to return to the menu 
        
        if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_R)){
        
            State.setState(handler.getGame().menuState);
       	
       }

        

           
           

        
     
      
        
    }

    public void checkCollisionAndMove(){
        handler.getWorld().playerLocation[xCoord][yCoord]=false;
        int x = xCoord;
        int y = yCoord;
       
        switch (direction){//This kills the snake then reaching out of bounds
            case "Left":
                if(xCoord==0){
                    kill();
                }else{
                    xCoord--;
                }
                break;
            case "Right":
                if(xCoord==handler.getWorld().GridWidthHeightPixelCount-1){
                    kill();
                }else{
                    xCoord++;
                }
                break;
            case "Up":
                if(yCoord==0){
                    kill();
                }else{
                    yCoord--;
                }
                break;
            case "Down":
                if(yCoord==handler.getWorld().GridWidthHeightPixelCount-1){
                    kill();
                }else{
                    yCoord++;
                }
                break;
        }
        handler.getWorld().playerLocation[xCoord][yCoord]=true;


        if(handler.getWorld().appleLocation[xCoord][yCoord]){
            Eat();
        }

        if(!handler.getWorld().body.isEmpty()) {
            handler.getWorld().playerLocation[handler.getWorld().body.getLast().x][handler.getWorld().body.getLast().y] = false;
            handler.getWorld().body.removeLast();
            handler.getWorld().body.addFirst(new Tail(x, y,handler));
            
         
            
            
         // Game over when collides with itself 
            for (int j = 0; j <  handler.getWorld().body.size(); j++) {
            	Tail T2 = handler.getWorld().body.get(j);
            	if(this.xCoord == T2.x && this.yCoord == T2.y){
            		kill();
            	}
            }
        }
    }

    
    
    
    
    public void render(Graphics g,Boolean[][] playeLocation){
        @SuppressWarnings("unused")
		Random r = new Random();
        for (int i = 0; i < handler.getWorld().GridWidthHeightPixelCount; i++) {
            for (int j = 0; j < handler.getWorld().GridWidthHeightPixelCount; j++) {
                g.setColor(getN());

                if(playeLocation[i][j]||handler.getWorld().appleLocation[i][j]){
                    g.fillRect((i*handler.getWorld().GridPixelsize),
                            (j*handler.getWorld().GridPixelsize),
                            handler.getWorld().GridPixelsize,
                            handler.getWorld().GridPixelsize);
                }

            }
        }


    }

   public static int  score = 0;
   
   
    public int getScore() {
	return score;
}

public void setScore(int score) {
	Player.score = score;
}

	public void Eat(){
        lenght++;
        Tail tail = null;
        handler.getWorld().appleLocation[xCoord][yCoord]=false;
        handler.getWorld().appleOnBoard=false;
    
        switch (direction){
            case "Left":
                if( handler.getWorld().body.isEmpty()){
                    if(this.xCoord!=handler.getWorld().GridWidthHeightPixelCount-1){
                        tail = new Tail(this.xCoord+1,this.yCoord,handler);
                    }else{
                        if(this.yCoord!=0){
                            tail = new Tail(this.xCoord,this.yCoord-1,handler);
                        }else{
                            tail =new Tail(this.xCoord,this.yCoord+1,handler);
                        }
                    }
                }else{
                    if(handler.getWorld().body.getLast().x!=handler.getWorld().GridWidthHeightPixelCount-1){
                        tail=new Tail(handler.getWorld().body.getLast().x+1,this.yCoord,handler);
                    }else{
                        if(handler.getWorld().body.getLast().y!=0){
                            tail=new Tail(handler.getWorld().body.getLast().x,this.yCoord-1,handler);
                        }else{
                            tail=new Tail(handler.getWorld().body.getLast().x,this.yCoord+1,handler);

                        }
                    }

                }
                break;
            case "Right":
                if( handler.getWorld().body.isEmpty()){
                    if(this.xCoord!=0){
                        tail=new Tail(this.xCoord-1,this.yCoord,handler);
                    }else{
                        if(this.yCoord!=0){
                            tail=new Tail(this.xCoord,this.yCoord-1,handler);
                        }else{
                            tail=new Tail(this.xCoord,this.yCoord+1,handler);
                        }
                    }
                }else{
                    if(handler.getWorld().body.getLast().x!=0){
                        tail=(new Tail(handler.getWorld().body.getLast().x-1,this.yCoord,handler));
                    }else{
                        if(handler.getWorld().body.getLast().y!=0){
                            tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord-1,handler));
                        }else{
                            tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord+1,handler));
                        }
                        	
                        }
                    }

                
                break;
            case "Up":
                if( handler.getWorld().body.isEmpty()){
                	
                    if(this.yCoord!=handler.getWorld().GridWidthHeightPixelCount-1){
                        tail=(new Tail(this.xCoord,this.yCoord+1,handler));
                    }else{
                        if(this.xCoord!=0){
                            tail=(new Tail(this.xCoord-1,this.yCoord,handler));
                        }else{
                            tail=(new Tail(this.xCoord+1,this.yCoord,handler));
                        }
                    }
                }else{
                    if(handler.getWorld().body.getLast().y!=handler.getWorld().GridWidthHeightPixelCount-1){
                        tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord+1,handler));
                    }else{
                        if(handler.getWorld().body.getLast().x!=0){
                            tail=(new Tail(handler.getWorld().body.getLast().x-1,this.yCoord,handler));
                        }else{
                            tail=(new Tail(handler.getWorld().body.getLast().x+1,this.yCoord,handler));
                        }
                    }

                }
                break;
            case "Down":
                if( handler.getWorld().body.isEmpty()){
                    if(this.yCoord!=0){
                        tail=(new Tail(this.xCoord,this.yCoord-1,handler));
                    }else{
                        if(this.xCoord!=0){
                            tail=(new Tail(this.xCoord-1,this.yCoord,handler));
                        }else{
                            tail=(new Tail(this.xCoord+1,this.yCoord,handler));
                        }
                    }
                }else{
                    if(handler.getWorld().body.getLast().y!=0){
                        tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord-1,handler));
                    }else{
                        if(handler.getWorld().body.getLast().x!=0){
                            tail=(new Tail(handler.getWorld().body.getLast().x-1,this.yCoord,handler));
                        }else{
                            tail=(new Tail(handler.getWorld().body.getLast().x+1,this.yCoord,handler));
                        }
                    }

                }
                break;
        }//posiotion snakes tale
        handler.getWorld().body.addLast(tail);
        handler.getWorld().playerLocation[tail.x][tail.y] = true;
        
       
        // Add score 
        
        Player.score++;
        

    }

    public void kill(){ //Aqui esta el Kill!
 
        lenght = 0;
        for (int i = 0; i < handler.getWorld().GridWidthHeightPixelCount; i++) {
            for (int j = 0; j < handler.getWorld().GridWidthHeightPixelCount; j++) {

                handler.getWorld().playerLocation[i][j]=false;
              
                
                JFrame frame = new JFrame("");
                
                JOptionPane.showMessageDialog(frame, "GAME OVER");
                  System.exit(0);
               /// Game over  when collides with the a wall   

               // 

            }
        }
    }

    public boolean isJustAte() {
        return justAte;
    }

    public void setJustAte(boolean justAte) {
        this.justAte = justAte;
    }
}
