package BP;

public class Bipart
{
  public Bipart() {
  }

  public boolean bipartable(int numWrestlers, Rival[] rivalries) {

	  int[] goodGuys = new int[numWrestlers];
	  int[] badGuys = new int[numWrestlers];

	  for (int i = 0; i < rivalries.length; i++) {
		  if (i == 0) {
			  goodGuys[rivalries[i].x] = 1;
		  }
		  if (goodGuys[rivalries[i].x] == 1 && goodGuys[rivalries[i].y] == 1) {
			  return false;
		  } else if (badGuys[rivalries[i].x] == 1 && badGuys[rivalries[i].y] == 1) {
			  return false;
		  } else if (goodGuys[rivalries[i].x] == 1 && badGuys[rivalries[i].y] == 0) {
			  badGuys[rivalries[i].y] = 1;
		  } else if (badGuys[rivalries[i].x] == 1 && goodGuys[rivalries[i].y] == 0) {
			  goodGuys[rivalries[i].y] = 1;
		  } else if (goodGuys[rivalries[i].y] == 1 && badGuys[rivalries[i].x] == 0) {
			  badGuys[rivalries[i].x] = 1;
		  } else if (badGuys[rivalries[i].y] == 1 && goodGuys[rivalries[i].x] == 0) {
			  goodGuys[rivalries[i].x] = 1;
		  }
	  }
	  return true;
  }
}
