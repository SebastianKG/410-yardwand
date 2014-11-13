package analysis;

import java.util.HashMap;


public class CollaboratorsActivities {

	private HashMap<String,String> authorMap;
	private HashMap<Double,Activity> activityMap;
	
		public CollaboratorsActivities()
		{
			authorMap = new HashMap<String,String>();
			activityMap = new HashMap<Double,Activity>();
		}
	
		public void updateActivity(Double date,int addition,int deletion, int commits)
		{
			Activity activity=activityMap.get(date);
			if (activity == null){
				activityMap.put(date, new Activity(addition, deletion, commits));
			}
			else
				activity.update(addition,deletion,commits);
		}
		public void createAuthorMap(String author,String url)
		{	
			authorMap.put(author,url);	
		}
		
		public HashMap<String,String> getAuthorMap()
		{
			return authorMap;
		}
		public HashMap<Double,Activity> getActivities()
		{
			return activityMap;
		}
		
		
}	
