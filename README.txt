=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 1200 Game Project README
PennKey: ksong37
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. 2-D Arrays

A 2-D Array will represent the bricks. It is the best representation for the bricks because I can use the indicies
when I am iterating over the bricks to calculate the positions of the bricks, instead of hardcoding them. The 2D
array also helps because it maintains the relative positions of the bricks, making indexing and updating specific ones
much easier.

  2. I/O

A file will be used to store the game state so the player can resume when they want to. There is no other way to store
the state of the game other than creating and saving a file, which the user can access later. I used a custom format to
be able to effectively read and write the data whenever the user wants to load the game using BufferedReader and
BufferedWriter.

  3. Inheritance and Subtyping

There will be different types of bricks that move differently. I will have an abstract Brick class which I will use for
my 2-D array, and then update the array to store specific types of bricks such as sliding brick and rotating brick. I
will override the move() method because the rotating brick will move according to the angle, while the sliding brick
will move according to the vx, which will be negated whenever the brick hits a wall. This is the best way to have
multiple types of bricks because all the other properties of the brick such as the color will be the same, I just need
to override one single method for the bricks to move differently.

  4. Collections or Maps

A ArrayList will be used to store all of the balls in the game. This is most efficient because all I need to do is
to add, remove, and update balls, and the ArrayList is the most basic data structure to do this.

===============================
=: File Structure Screenshot :=
===============================
- Include a screenshot of your project's file structure. This should include
  all of the files in your project, and the folders they are in. You can
  upload this screenshot in your homework submission to gradescope, named 
  "file_structure.png".

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.

GameObj: Abstract class for balls, bricks, and Player that allows for positions and velocities, bounces, collisions etc.
Ball: Class to create a ball that bounces and destroys bricks
Brick: Abstract class to create Bricks
- Rotating Brick: Brick that rotates with a radius according to an angle
- Sliding Brick: Brick that moves back and forth, and goes in the opposite direction when it hits a wall
- Standard Brick: Brick that doesn't move
Player: Class for the paddle, moves side to side and bounces the balls
RunBrickBreaker: Creates the game court objects and buttons to run the game and updates the game state
GameCourt: Handles functionality during the game such as keyboard input, saving/loading and updates game every frame
Direction: Enum for which direction the object will collide into another object
GameState: Enum for handling updating the text in the buttons and status at the bottom.

- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?

I had some trouble coming up with how to use inheritance in my project because I couldn't access
any of the other elements in my game (if I wanted to create a ball that destroyed bricks in a radius,
I couldn't access the array of bricks from the Ball class). So, I came up with the idea of having bricks
that move differently.

- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?

I would probably try to split up some of the functions such as loadGame and put the file reading/writing functionality
in a different function. Otherwise, I think I have fairly good program design and all of my classes
are encapsulated.

========================
=: External Resources :=
========================

- Cite any external resources (images, tutorials, etc.) that you may have used 
  while implementing your game.

Java Documentation
https://stackoverflow.com/questions/42800156/how-to-use-bufferedreader-in-a-try-catch