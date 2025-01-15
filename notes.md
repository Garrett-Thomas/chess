NOTES
1. ***Solving Chess Moves***
   * Make Piece Moves Calculator a superclass
   * Need to calculate and see if the board has changed since last query
   * If it has, then recalculate every possible move. And store in pretty data structure.
   * King can only move to a spot that isn't in the possible moves of the opposing team.
   * Other pieces are much easier. If there path intersects with opposing team then they can take its place
   * Cannot change the place of same team piece. 