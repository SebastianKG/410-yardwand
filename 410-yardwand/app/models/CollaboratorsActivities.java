package models;

import java.util.LinkedHashMap;

import models.Activity;


public class CollaboratorsActivities {

	private LinkedHashMap<String,String> authorMap;
	private LinkedHashMap<Double,Activity> activityMap;
	
		public CollaboratorsActivities()
		{
			authorMap = new LinkedHashMap<String,String>();
			activityMap = new LinkedHashMap<Double,Activity>();
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
		
		public LinkedHashMap<String,String> getAuthorMap()
		{
			return authorMap;
		}
		public LinkedHashMap<Double,Activity> getActivities()
		{
			return activityMap;
		}
		
		
}	
