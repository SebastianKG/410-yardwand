package analysis;
import java.util.ArrayList;
import java.util.HashMap;


public class CollaboratorsActivities {

	private ArrayList<String> authorlist;
	private HashMap<Double,Activity> activityMap;
	
		public CollaboratorsActivities()
		{
			authorlist=new ArrayList<String>();
			activityMap=new HashMap<Double,Activity>();
		}
		public void addAuhtor(String author)
		{
			authorlist.add(author);
		}
		public int getAuthorCount()
		{
			return authorlist.size();
		}
		public ArrayList<String> getAuthorList()
		{
			return authorlist;
		}
		public void updateActivity(Double date,int addition,int deletion, int commits)
		{
			Activity activity=activityMap.get(date);
			if (activity==null){
				activityMap.put(date, new Activity(addition, deletion, commits));
			}
			else
				activity.update(addition,deletion,commits);
		}
		public HashMap<Double,Activity> getActivities()
		{
			return activityMap;
		}
}	
