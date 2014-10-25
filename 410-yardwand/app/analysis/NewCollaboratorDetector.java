package analysis;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
	public static void main(String[] args)
	{
		NewCollaboratorDetector n=new NewCollaboratorDetector("https://api.github.com/repos/zxing/zxing/stats/contributors");
		CollaboratorsActivities x=n.getAnalysis();
	
		HashMap<Double,Activity> map=x.getActivities();
		Iterator it=map.entrySet().iterator();
		
		while(it.hasNext())
		{
			 Map.Entry pairs = (Map.Entry)it.next();
			Activity a= (Activity)pairs.getValue();
				System.out.println(pairs.getKey().toString()+" "+ a.getAddition()+" "+a.getDeletion()+" "+a.getCommits() );
				
		}
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
				collaborators_activities.addAuhtor(author);
				
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