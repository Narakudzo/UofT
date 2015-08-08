

public class HelloSample extends Assignment2 {

	public static void main(String[] args) {

		Assignment2 pg = new Assignment2();
		pg.connectDB("jdbc:postgresql://localhost:5432/ogawafamilyintoronto","ogawafamilyintoronto","");

		if(pg.setSchema("a2")){
			System.out.println("Schema changed to a2.");
		}
		
		System.out.println("ID 4 became champions " + pg.getChampions(4) + " times.");
		
		if(pg.chgRecord(9, 9)) {
			System.out.println("Record updated for Pid=9, Globalrank=9");
		}
		
		if(pg.insertTeam(15, "Team Unionville", 1)) {
			System.out.println("Team Unionville inserted eid=15, cid=1");
		} else {
			System.out.println("Team cannot be inserted. Already exists.");
		}
		System.out.println(pg.getRinkInfo(1));
		
		if(pg.deleteMatcBetween(1, 2)) {
			System.out.println("Match betwee 1 and 2 are deleted.");
		} else {
			System.out.println("DeleteMatch failed");
		}
		
		System.out.println(pg.listPlayerRanking());
		
		System.out.println("Tri Circle = " + pg.findTriCircle());
		
		System.out.println("updateDB() returned " + pg.updateDB());
		pg.disconnectDB();
	}

}
