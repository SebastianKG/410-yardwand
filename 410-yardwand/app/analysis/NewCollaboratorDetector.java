package analysis;

import org.json.JSONArray;
import org.json.JSONObject;



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
				collaborators_activities.addAuthor(author);
				
				JSONArray weeksArray= jo.getJSONArray("weeks");
				for(int j=0; j< weeksArray.length();j++)
				{
					JSONObject weekObject = weeksArray.getJSONObject(j);
					String w=weekObject.getString("w");
					String a=weekObject.getString("a");
					String d=weekObject.getString("d");
					String c=weekObject.getString("c");
					collaborators_activities.updateActivity(Double.parseDouble(w),Integer.parseInt(a),Integer.parseInt(d),Integer.parseInt(c));
					
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return collaborators_activities;
	}
}