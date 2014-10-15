package controllers;

import fusion.CommitMetricPairingModule;
import play.*;
import play.mvc.*;
import views.html.*;

public class Application extends Controller {
	public static CommitMetricPairingModule pairingModule;
	
    public static Result index() {
    	String repopath1 = "repopath1.html";
    	String repopath2 = "repopath2.html";
    	
    	pairingModule = new CommitMetricPairingModule("repopath1.html","repopath2.html");
    	
    	return ok( index.render(repopath1, pairingModule.repo1.statList, repopath2, pairingModule.repo2.statList) );
    }
}
