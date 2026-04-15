Prompt 1:

I'm building a Snake game in Java using Swing. Create a single file called SnakeGame.java. It should have a main method that opens a JFrame window that is 600 by 600 pixels and titled Snake. Inside the frame, add a JPanel subclass called GamePanel. Do not add any game logic yet. Just get the window to open correctly.

Prompt 2:

Now extend SnakeGame.java. Keep it as one file. Add a dark background grid and draw a starting snake that is three segments long near the center of the board, facing right. Each cell should be a 30x30 pixel square. Draw the snake in green and the background in dark gray. Do not add movement yet.

Prompt 3:

Make the snake move automatically using a Swing timer that ticks every 150 milliseconds. Add arrow key controls so the player can steer, but don't allow the snake to reverse direction. For now, have the snake wrap around the edges instead of dying. Make sure the panel can receive keyboard input.

Prompt 4:

Add a food pellet that spawns at a random empty cell. When the snake eats it, grow by one segment and spawn new food. Add collision detection: hitting a wall or the snake's own body should end the game, stop movement, and show a "Game Over" message with the final score. Display the current score in the top-left corner during play. When the game is over, let the player press R to reset everything and play again.

Prompt 5: 


Could you change the game over borders and fill color borders to match the 700 by 700 pixels as declared in the main method.

Prompt 6: 

The gameOver triggers at 600 by 600 pixels. Can you Adjust this to match the JFrame size of 700 by 700 pixels. Add some additional comments so I can see where things are.

Prompt 7:

I would like to make the snake more appealing. Give the snake beady black eyes with white spots as it moves around the screen. Alternate the snake color between a random color for every length it gains. Change the starting snake body to be a dark green as well.

Prompt 8:

Time to make the game more interesting. After the score reaches 10, add an object to the screen that is able to be collided with and ends the game once that happens. After every 10 score, add another object to the screen. In addition to this, once an object is added, make text appear on the screen for 5 seconds that states "A Rock has fallen from the sky, Watch Out!". make the objects color a light grey.

Prompt 9:

Now, change the apple sprite to look like an actual apple instead of just a red square. Make sure that it has a black outline so it is easy to see for the player