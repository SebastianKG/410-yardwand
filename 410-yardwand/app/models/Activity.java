package models;

public class Activity {
	
	//Number of addition
	private int a;
	//Number of deletion
	private int d;
	// Number of commits
	private int c;
	
	public Activity(int a,int d, int c)
	{
		this.a=a;
		this.d=d;
		this.c=c;
	}
	public int getAddition()
	{return a;}
	public int getDeletion()
	{return d;}
	public int getCommits()
	{return c;}
	public void update(int addition,int deletion,int commits)
	{
		a +=addition;
		d +=deletion;
		c +=commits;
	}
}
