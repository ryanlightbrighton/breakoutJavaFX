    version tracker
    
    27/2/2019
    
    1 - bodged in some collision detection for the ball hitting side of bricks
        ---------------
        affected files:
        ---------------
        new function whereHitBrick in GameObj.java
        called in updateGame() (Model.java)
    
    2 - Changed bat movement to be Key on/ key off (but EHup doesn't seem to work)
        gets rid of janky bat movement (was depending on Windows key handler)
        ---------------
        affected files:
        ---------------
        New property BAT_DIR in Model.java (values = -1,0,1) // should this be a property in the bat object (new class for bat)
        scene.setOnKeyReleased(this); added to View.java
        model.moveBat(model.BAT_DIR); in View.java  - WHY IS THIS?? ALL OTHER GAME UPDATES IN UPDATEGAME() >>MODEL.JAVA
        
    3 - Made bat respect sides of screen
        ---------------
        affected files:
        ---------------
        new check in update() View.java
        
    4 - Created BrickObj class - WHY DO I NEED TO DEFINE EMPTY CONSTRUCTOR IN GAMEOBJ???
        ---------------
        affected files:
        ---------------
        BrickObj.java
        
    5 - Added brick damage value & colour change
        ---------------
        affected files:
        ---------------
        BrickObj.java (int damage = 3;)
        Model.java >> updateGame() function
        
    6 - Test code added to alter ball x velocity using bat  (REMOVED - DODGY)
        
        ---------------
        affected files:
        ---------------
        GameObj.java public int dirX   = 1;  becomes a double
        Model.java >> updateGame() function
        
    7 - BatObj class created (not used properly yet)
        
        ---------------
        affected files:
        ---------------
        BatObj.java 
        
        ////////////////////////////////////////////////////////////////////////////
        
        Need to update the dirY value.  This also means 
        
        
        
