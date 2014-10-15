package models;

import java.util.LinkedList;
import java.util.List;

import play.db.ebean.Model;

public class Repository extends Model {
	public List<Stat> statList;
	public String path;
	
	public Repository(String path) {
		statList = new LinkedList<Stat>();
		this.path = path;
	}
}
