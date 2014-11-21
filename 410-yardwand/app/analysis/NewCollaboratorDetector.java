package analysis;

import org.json.JSONArray;
import org.json.JSONObject;
import models.CollaboratorsActivities;


public class NewCollaboratorDetector {
	private String url;
	private CollaboratorsActivities collaborators_activities;
	
	public NewCollaboratorDetector(){}
	// sample url: https://api.github.com/repos/zxing/zxing/stats/contributors
	public NewCollaboratorDetector(String url)
	{ 	collaborators_activities=new CollaboratorsActivities();
		this.url=url;
	}	

	public CollaboratorsActivities getAnalysis()
	{	
		String response;
		HttpConnectionWrapper http=new HttpConnectionWrapper();
		try {
			
			response=http.sendGet(url);
			JSONArray array= new JSONArray(response);
			for(int i=0; i<array.length();i++)
			{
				JSONObject jo= array.getJSONObject(i);
				JSONObject authorObject = jo.getJSONObject("author");
				String author= authorObject.getString("login");
				String avatar=authorObject.getString("avatar_url");
				collaborators_activities.createAuthorMap(author, avatar);
				
				JSONArray weeksArray= jo.getJSONArray("weeks");
				for(int j=0; j< weeksArray.length();j++)
				{
					JSONObject weekObject = weeksArray.getJSONObject(j);
					Double w=weekObject.getDouble("w");
					int a=weekObject.getInt("a");
					int d=weekObject.getInt("d");
					int c=weekObject.getInt("c");
					collaborators_activities.updateActivity(w,a,d,c);
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return collaborators_activities;
	}
}